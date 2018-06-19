@file:SuppressLint("SetTextI18n")

package com.nextfaze.devfun.demo

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nextfaze.devfun.annotations.DeveloperFunction
import com.nextfaze.devfun.core.devFun
import com.nextfaze.devfun.core.devFunVerbose
import com.nextfaze.devfun.demo.inject.BuildTypeFragmentInjector
import com.nextfaze.devfun.demo.inject.FragmentInjector
import com.nextfaze.devfun.demo.kotlin.*
import com.nextfaze.devfun.error.ErrorHandler
import kotlinx.android.synthetic.debug.kotlin_bug_fragment.*
import java.io.PrintWriter
import java.io.StringWriter
import java.lang.reflect.Method
import java.util.Random
import kotlin.reflect.*
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaMethod
import kotlin.reflect.jvm.kotlinProperty

class KotlinBugActivity : BaseActivity() {
    companion object {
        @DeveloperFunction
        fun start(context: Context) = context.startActivity<KotlinBugActivity>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_activity)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.activity_fragment_view, findOrCreate { KotlinBugFragment() }).commit()
        }
    }
}

/** To simulate normal DevFun behaviour (we want the `property$annotations()` function) */
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.BINARY)
private annotation class DummyAnnotation

class KotlinBugFragment : BaseFragment() {
    init {
        devFunVerbose = true // increases likelihood of failure
    }

    @DummyAnnotation
    @Suppress("unused")
    private val someBoolean: String? by lazy { "sdf" }

    private val s = PropertyProcessor()
    private val r = Random()
    private val handler = Handler(Looper.getMainLooper())
    private var testRunning = false
    private var successCount = 0

    override fun inject(injector: FragmentInjector) = (injector as BuildTypeFragmentInjector).inject(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.kotlin_bug_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        runTestButton.setOnClickListener {
            runTestButton.text = "Running..."
            runTestButton.enabled = false
            textView.text = ""
            stackTraceTextView.text = ""
            handler.post {
                if (!testRunning) {
                    testRunning = true
                    updateSuccessCount()
                    runTest()
                }
            }
        }
    }

    /** Doesn't actually work since main thread is being thrashed, but this invocation adds to that thrashing and increases likelihood of failure. */
    private fun updateSuccessCount() {
        handler.postDelayed({
            textView.text = "Invocations succeeded before exception: $successCount"
            if (testRunning) {
                updateSuccessCount()
            }
        }, 10)
    }

    private val prop = object : PropertyMethod {
        override val method = KotlinBugFragment::class.java.getDeclaredMethod("someBoolean\$annotations").apply { isAccessible = true }
    }

    private fun runTest() {
        successCount = 0
        test()
    }

    private fun stopTest(t: Throwable) {
        testRunning = false
        runTestButton.text = "Re-run Test"
        runTestButton.enabled = true
        textView.text = "Invocations succeeded before exception: $successCount"
        stackTraceTextView.text = t.stackTraceAsString
        devFun.get<ErrorHandler>().onError(t, "Kotlin Reflect Bug", textView.text)
    }

    private fun logSuccessCount() {
        successCount++
        logger().d { "successCount=$successCount" }
    }

    private fun test() {
        if (!testRunning) return

        try {
            val desc = s.getDescriptor(prop)
            println("desc=${desc.name}")
        } catch (t: Throwable) {
            stopTest(t)
            return
        }

        handler.postDelayed({
            test()
            logSuccessCount()
        }, r.nextInt(100).toLong())
        handler.postAtFrontOfQueue {
            test()
            logSuccessCount()
        }
        handler.post {
            test()
            logSuccessCount()
        }
    }
}

private interface PropertyMethod {
    val method: Method
    val clazz: KClass<out Any> get() = method.declaringClass.kotlin
}

private interface Descriptor {
    val name: CharSequence
}

private class PropertyProcessor {
    fun getDescriptor(propertyMethod: PropertyMethod): Descriptor {
        return object : Descriptor {
            val fieldName by lazy { propertyMethod.method.name.substringBefore('$') }
            val propertyField by lazy {
                try {
                    propertyMethod.clazz.java.getDeclaredField(fieldName).apply { isAccessible = true }
                } catch (ignore: NoSuchFieldException) {
                    null // is property without backing field (i.e. has custom getter/setter)
                }
            }
            val property by lazy {
                val propertyField = propertyField
                when {
                    propertyField != null -> propertyField.kotlinProperty!!
                    else -> propertyMethod.clazz.declaredMemberProperties.first { it.name == fieldName }
                }.apply { isAccessible = true }
            }

            // Kotlin reflection has weird accessibility issues when invoking get/set/getter/setter .call()
            // it only seems to work the first time with subsequent calls failing with illegal access exceptions and the like
            val getter by lazy { property.getter.javaMethod?.apply { isAccessible = true } }
            val setter by lazy {
                val property = property
                if (property is KMutableProperty<*>) property.setter.javaMethod?.apply { isAccessible = true } else null
            }

            val propertyDesc by lazy {
                val lateInit = if (property.isLateinit) "lateinit " else ""
                val varType = if (property is KMutableProperty<*>) "var" else "val"
                "$lateInit$varType $fieldName: ${property.simpleName}"
            }

            private val receiver by lazy { devFun.instanceOf(propertyMethod.clazz) }
            private val isUninitialized by lazy {
                property.isUninitialized
            }

            private var value: Any?
                get() = when {
                    getter != null -> getter!!.invoke(receiver)
                    else -> propertyField?.get(receiver)
                }
                set(value) {
                    when {
                        setter != null -> setter!!.invoke(receiver, value)
                        else -> propertyField?.set(receiver, value)
                    }
                }
            override val name by lazy {
                SpannableStringBuilder().apply {
                    this += propertyDesc
                    this += " = "
                    when {
                        property.isLateinit && value == null -> this += i("undefined")
                        isUninitialized -> this += i("uninitialized")
                        property.type == String::class && value != null -> this += """"$value""""
                        else -> this += "$value"
                    }
                    if (isUninitialized) {
                        this += "\n"
                        this += color(scale(i("\t(tap will initialize)"), 0.85f), 0xFFAAAAAA.toInt())
                    }
                }
            }

            @Suppress("UNCHECKED_CAST")
            private val KProperty<*>.isUninitialized: Boolean
                get() {
                    isAccessible = true
                    return when (this) {
                        is KProperty0<*> -> (getDelegate() as? Lazy<*>)?.isInitialized() == false
                        is KProperty1<*, *> -> ((this as KProperty1<Any?, Any>).getDelegate(receiver) as? Lazy<*>)?.isInitialized() == false
                        else -> false
                    }
                }
        }
    }

    private val KProperty<*>.simpleName get() = "${type.simpleName}${if (returnType.isMarkedNullable) "?" else ""}"
    private val KProperty<*>.type get() = returnType.classifier as KClass<*>
}

private val Throwable.stackTraceAsString get() = StringWriter().apply { printStackTrace(PrintWriter(this)) }.toString()
