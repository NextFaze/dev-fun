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
import com.nextfaze.devfun.inject.Constructable
import com.nextfaze.devfun.inject.isSubclassOf
import com.nextfaze.devfun.internal.ReflectedProperty
import com.nextfaze.devfun.internal.android.*
import com.nextfaze.devfun.internal.splitCamelCase
import com.nextfaze.devfun.internal.string.*
import com.nextfaze.devfun.internal.toReflected
import com.nextfaze.devfun.invoke.Invoker
import com.nextfaze.devfun.overlay.OverlayManager

interface OverlayLogging {
    fun createLogger(name: String, updateCallback: UpdateCallback, onClick: OnClick? = null): OverlayLogger
}

@DeveloperCategory("Logging", order = 90_000)
@Constructable(singleton = true)
internal class OverlayLoggingImpl(
    private val application: Application,
    private val overlayManager: OverlayManager,
    private val invoker: Invoker,
    private val activityProvider: ActivityProvider
) : OverlayLogging {
    private val loggers = devFun.developerReferences<DeveloperLogger>().map { Logger(it) }

    private val overlays = mutableMapOf<Logger, OverlayLogger>().apply {
        loggers.forEach { ref ->
            if (!ref.isContextual) {
                this[ref] = createLogger(ref.prefsName, ref.updateCallback)
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
                    overlays.getOrPut(it) { createLogger(it.prefsName, it.updateCallback, it.onClick) }
                } else {
                    overlays.remove(it)?.also { it.destroy() }
                }
            }
        }
    }

    private inner class Logger(ref: DeveloperReference) {
        val reflected = ref.method!!.toReflected()
        val prefsName = "${reflected.declaringClass.simpleName}.${reflected.name.substringBefore('$')}"

        val isInActivity = Activity::class.java.isAssignableFrom(reflected.declaringClass)
        val isInFragment = reflected.clazz.isSubclassOf<Fragment>()
        val isContextual = isInActivity || isInFragment
        val isInContext
            get() =
                when {
                    isInActivity -> activityProvider()?.let { it::class.isSubclassOf(reflected.clazz) } == true
                    isInFragment -> activityProvider()?.let { it as? FragmentActivity }?.let {
                        it.supportFragmentManager.fragments.orEmpty().any {
                            // not sure how/why, but under some circumstances some of them are null
                            it != null && it.isAdded && reflected.declaringClass.isAssignableFrom(it::class.java)
                        }
                    } == true
                    else -> false
                }

        val displayName by lazy { if (reflected is ReflectedProperty) reflected.desc else ".${reflected.name}()" }

        val updateCallback: UpdateCallback by lazy {
            if (reflected is ReflectedProperty) {
                return@lazy {
                    val isUninitialized by lazy { reflected.isUninitialized }
                    val value = reflected.value

                    SpannableStringBuilder().apply {
                        this += reflected.desc
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
                return@lazy { reflected.invoke().toCharSequence() }
            }
        }

        val onClick: OnClick = onClick@{
            if (true) return@onClick // TODO this is real slow (DevFun optimizations needed!)
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

        override fun equals(other: Any?) = if (other is Logger) reflected == other.reflected else false
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
                    override val group = ref.reflected.declaringClass.simpleName.splitCamelCase()
                    override val args = listOf(logger)
                }
            }
        }
    }

    override fun createLogger(name: String, updateCallback: UpdateCallback, onClick: OnClick?) =
        OverlayLogger(overlayManager, invoker, name, updateCallback, onClick)

    @DeveloperFunction(transformer = LoggersTransformer::class)
    private fun configureLogger(logger: OverlayLogger) {
        logger.showConfigDialog()
    }
}
