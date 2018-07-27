package com.nextfaze.devfun.overlay.logger

import android.app.Activity
import android.app.Application
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.text.SpannableStringBuilder
import com.nextfaze.devfun.annotations.DeveloperCategory
import com.nextfaze.devfun.annotations.DeveloperFunction
import com.nextfaze.devfun.annotations.DeveloperLogger
import com.nextfaze.devfun.core.*
import com.nextfaze.devfun.error.ErrorHandler
import com.nextfaze.devfun.inject.Constructable
import com.nextfaze.devfun.inject.isSubclassOf
import com.nextfaze.devfun.internal.ReflectedMethod
import com.nextfaze.devfun.internal.ReflectedProperty
import com.nextfaze.devfun.internal.android.*
import com.nextfaze.devfun.internal.splitCamelCase
import com.nextfaze.devfun.internal.string.*
import com.nextfaze.devfun.internal.toReflected
import com.nextfaze.devfun.overlay.Dock
import com.nextfaze.devfun.overlay.OverlayManager
import com.nextfaze.devfun.overlay.OverlayWindow
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

/** Handles the creation, maintenance, and permissions of [OverlayLogger] instances. */
interface OverlayLogging {
    /**
     * Creates an over logger instance. Call [OverlayLogger.start] to add to window and start updating.
     *
     * Instances created from this will *not* be managed automatically.
     * This function is used internally to create instances that *are* managed automatically however.
     *
     * @see OverlayLogger.stop
     */
    fun createLogger(name: String, updateCallback: UpdateCallback, onClick: OnClick? = null): OverlayLogger
}

@DeveloperCategory("Logging", order = 90_000)
@Constructable(singleton = true)
internal class OverlayLoggingImpl(
    private val application: Application,
    private val overlayManager: OverlayManager,
    private val activityProvider: ActivityProvider
) : OverlayLogging {
    private val loggers =
        devFun.developerReferences<DeveloperLogger>().mapNotNull {
            when (it) {
                is MethodReference -> MethodLogger(it)
                is TypeReference -> TypeLogger(it)
                else -> {
                    devFun.get<ErrorHandler>().onWarn(
                        "Overlay Logging",
                        "Unexpected DeveloperReference type: ${it::class.java.interfaces.joinToString()}\nThis should not happen - please make an issue!"
                    )
                    null
                }
            }
        }

    private val overlays = mutableMapOf<Logger, OverlayLogger>().apply {
        loggers.forEach { ref ->
            if (!ref.isContextual) {
                this[ref] = ref.createLogger().apply { start() }
            }
        }
    }

    fun init() {
        application.registerActivityCallbacks(
            onCreated = { activity, _ ->
                updateContextualLoggers()
                if (activity is FragmentActivity) {
                    activity.supportFragmentManager.registerFragmentLifecycleCallbacks(
                        object : FragmentManager.FragmentLifecycleCallbacks() {
                            override fun onFragmentResumed(fm: FragmentManager?, f: Fragment?) = updateContextualLoggers()
                            override fun onFragmentDetached(fm: FragmentManager?, f: Fragment?) = updateContextualLoggers()
                        },
                        true
                    )
                }
            },
            onDestroyed = { updateContextualLoggers() }
        )

        updateContextualLoggers()
    }

    private fun updateContextualLoggers() {
        loggers.forEach {
            if (it.isContextual) {
                if (it.isInContext) {
                    overlays.getOrPut(it) { it.createLogger().apply { start() } }
                } else {
                    overlays.remove(it)?.apply {
                        stop()
                        overlayManager.destroyOverlay(overlay)
                    }
                }
            }
        }
    }

    private class AnnotationProperties(private val properties: Map<String, *>) {
        inline fun <reified T> propertyMap(default: T) =
            object : ReadOnlyProperty<Any, T> {
                override fun getValue(thisRef: Any, property: KProperty<*>) = properties[property.name] as T? ?: default
            }

        val enabled: Boolean by propertyMap(true)
        val refreshRate: Long by propertyMap(1000L)
        val snapToEdge: Boolean by propertyMap(false)
        val dock: Dock by propertyMap(Dock.TOP_LEFT)
        val delta: Float by propertyMap(0f)
        val top: Float by propertyMap(0f)
        val left: Float by propertyMap(0f)
    }

    private abstract inner class Logger(val clazz: KClass<*>, properties: Map<String, *>?) {
        abstract val displayName: CharSequence
        abstract val prefsName: String
        abstract val group: CharSequence

        abstract val updateCallback: UpdateCallback
        abstract val onClick: OnClick?

        val isInActivity = clazz.isSubclassOf<Activity>()
        val isInFragment = clazz.isSubclassOf<Fragment>()
        val isContextual = isInActivity || isInFragment
        val isInContext
            get() = when {
                isInActivity -> activityProvider()?.let { it::class.isSubclassOf(clazz) } == true
                isInFragment -> activityProvider()?.let { it as? FragmentActivity }?.let {
                    it.supportFragmentManager.fragments.orEmpty().any {
                        // not sure how/why, but under some circumstances some of them are null
                        it != null && it.isAdded && clazz.java.isAssignableFrom(it::class.java)
                    }
                } == true
                else -> false
            }

        val props = AnnotationProperties(properties ?: emptyMap<String, Any>())

        fun createLogger() =
            createLogger(
                createOverlay(
                    prefsName,
                    snapToEdge = props.snapToEdge,
                    dock = props.dock,
                    delta = props.delta,
                    top = props.top,
                    left = props.left,
                    enabled = props.enabled
                ),
                updateCallback,
                onClick,
                props.refreshRate
            )
    }

    private inner class TypeLogger(ref: TypeReference) : Logger(ref.type, ref.properties) {
        override val displayName: CharSequence = clazz.java.name
        override val prefsName: String = clazz.java.simpleName
        override val group = clazz.java.simpleName.splitCamelCase()

        override val updateCallback: UpdateCallback by lazy { { devFun.instanceOf(clazz).toString() } }
        override val onClick: OnClick? = null

        override fun equals(other: Any?) = if (other is TypeLogger) clazz == other.clazz else false
        override fun hashCode() = clazz.hashCode()
        override fun toString() = clazz.toString()
    }

    private inner class MethodLogger(ref: MethodReference, private val reflected: ReflectedMethod = ref.method.toReflected()) :
        Logger(reflected.clazz, ref.properties) {
        override val displayName by lazy { if (reflected is ReflectedProperty) reflected.desc else ".${reflected.name}()" }
        override val prefsName = "${clazz.java.simpleName}.${reflected.name.substringBefore('$')}"
        override val group = clazz.java.simpleName.splitCamelCase()

        override val updateCallback: UpdateCallback by lazy {
            if (reflected is ReflectedProperty) {
                return@lazy {
                    val isUninitialized by lazy { reflected.isUninitialized }
                    val value = reflected.value

                    SpannableStringBuilder().apply {
                        this += reflected.getDesc(!isContextual)
                        this += " = "
                        when {
                            reflected.isLateinit && value == null -> this += i("undefined")
                            isUninitialized -> this += i("uninitialized")
                            reflected.type == String::class && value != null -> this += """"$value""""
                            else -> this += "$value"
                        }
                        if (isUninitialized) {
                            this += "\n"
                            this += color(scale(i("\t(tap will initialize)"), 0.85f), 0xFFAAAAAA.toInt())
                        }
                    }
                }
            } else {
                return@lazy { "${if (!isContextual) "${clazz.simpleName}." else ""}${reflected.name}()=${reflected.invoke().toCharSequence()}" }
            }
        }

        override val onClick: OnClick = onClick@{
            if (reflected is ReflectedProperty) {
                devFun.categories.forEach {
                    it.items.forEach {
                        if (it.function.method == reflected.method) {
                            it.invoke(null, emptyList()) // if it's a property then it's handled by the UI
                            return@onClick
                        }
                    }
                }
            }
        }

        private fun Any?.toCharSequence() =
            when (this) {
                is CharSequence -> this
                else -> toString()
            }

        override fun equals(other: Any?) = if (other is MethodLogger) reflected == other.reflected else false
        override fun hashCode() = reflected.hashCode()
        override fun toString() = reflected.toString()
    }

    @Constructable(singleton = true)
    private inner class LoggersTransformer : FunctionTransformer {
        override fun apply(functionDefinition: FunctionDefinition, categoryDefinition: CategoryDefinition): Collection<FunctionItem>? {
            return overlays.map { (ref, logger) ->
                object : SimpleFunctionItem(functionDefinition, categoryDefinition) {
                    override val name =
                        SpannableStringBuilder().apply {
                            this += ref.displayName
                            this += "\n"
                            this += color(
                                scale(
                                    i("\t(enabled=${logger.enabled}, refresh=${logger.refreshRate}, visibilityScope=${logger.visibilityScope})"),
                                    0.85f
                                ), 0xFFAAAAAA.toInt()
                            )
                        }
                    override val group = ref.group
                    override val args = listOf(logger)
                }
            }
        }
    }

    override fun createLogger(name: String, updateCallback: UpdateCallback, onClick: OnClick?): OverlayLogger =
        createLogger(createOverlay(name), updateCallback, onClick)

    private fun createOverlay(
        name: String,
        prefsName: String = "OverlayLogger_$name",
        snapToEdge: Boolean = false,
        dock: Dock = Dock.TOP_LEFT,
        delta: Float = 0.0f,
        top: Float = 0.0f,
        left: Float = 0.0f,
        enabled: Boolean = true
    ) = overlayManager.createOverlay(
        layoutId = R.layout.df_devfun_logger_overlay,
        name = "Overlay Logger $name",
        prefsName = prefsName,
        reason = { "Show overlay logger for $prefsName" },
        snapToEdge = snapToEdge,
        initialDock = dock,
        initialDelta = delta,
        initialTop = top,
        initialLeft = left
    ).also { it.enabled = enabled }

    private fun createLogger(
        overlay: OverlayWindow,
        updateCallback: UpdateCallback,
        onClick: OnClick?,
        refreshRate: Long = 1000L
    ) = OverlayLoggerImpl(application, overlay, updateCallback, onClick, initialRefreshRate = refreshRate)
        .also { logger -> overlay.onLongClickListener = { configureLogger(logger) } }

    @DeveloperFunction(transformer = LoggersTransformer::class)
    private fun configureLogger(logger: OverlayLogger) =
        overlayManager.configureOverlay(logger.overlay, logger.configurationOptions) { logger.resetPositionAndState() }
}
