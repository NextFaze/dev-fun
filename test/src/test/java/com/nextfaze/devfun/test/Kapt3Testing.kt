package com.nextfaze.devfun.test

import android.os.Build
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner
import com.nextfaze.devfun.category.DeveloperCategory
import com.nextfaze.devfun.compiler.FLAG_DEBUG_VERBOSE
import com.nextfaze.devfun.compiler.PACKAGE_ROOT
import com.nextfaze.devfun.compiler.PACKAGE_SUFFIX
import com.nextfaze.devfun.core.DevFun
import com.nextfaze.devfun.internal.android.*
import com.nhaarman.mockito_kotlin.KStubbing
import org.jetbrains.kotlin.kapt3.base.incremental.DeclaredProcType
import org.jetbrains.kotlin.kapt3.base.incremental.IncrementalProcessor
import org.jetbrains.kotlin.utils.PathUtil
import org.mockito.Mockito
import org.testng.ITestResult
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import javax.annotation.processing.Processor
import kotlin.collections.set
import kotlin.reflect.KClass
import kotlin.reflect.full.NoSuchPropertyException
import kotlin.test.Asserter
import com.sun.tools.javac.util.List as JCList

const val TEST_PACKAGE_ROOT = "tested"

fun singleFileTests(
    testMethod: Method,
    vararg classes: KClass<*>,
    useSdkInt: Int? = null,
    performKapt: Boolean = true,
    performCompile: Boolean = true
) =
    classes.map {
        arrayOf(
            TestContext(
                testMethod.name,
                listOf(it),
                sdkInt = useSdkInt,
                performKapt = performKapt,
                performCompile = performCompile
            )
        )
    }.toTypedArray()

fun devFunKaptOptions(
    debugVerbose: Boolean? = true,
    packageRoot: String? = TEST_PACKAGE_ROOT,
    packageSuffix: String? = null
): Map<String, String> =
    mutableMapOf<String, String>().apply {
        debugVerbose?.let { this[FLAG_DEBUG_VERBOSE] = it.toString() }
        packageRoot?.let { this[PACKAGE_ROOT] = it }
        packageSuffix?.let { this[PACKAGE_SUFFIX] = it }
    }

abstract class AbstractKotlinKapt3Tester {
    protected val processors: List<IncrementalProcessor>
        get() = listOf("com.nextfaze.devfun.compiler.DevFunProcessor", "com.nextfaze.devfun.compiler.DevAnnotationProcessor")
            .map { IncrementalProcessor(Class.forName(it).newInstance() as Processor, DeclaredProcType.NON_INCREMENTAL) }

    protected val compileClasspath = listOf(
        AnnotationRetention::class, // kotlin-stdlib
        NoSuchPropertyException::class, // kotlin-reflect
        Asserter::class, // kotlin-test
        DeveloperCategory::class, // devfun-annotations
        AbstractActivityLifecycleCallbacks::class, // devfun-internal
        DevFun::class, // devfun-core,
        Build::class, // android,
        ActivityCompat::class, // androidx-core
        NonNull::class, // androidx-annotation
        FragmentActivity::class, // androidx-fragment
        ViewModelStoreOwner::class, // androidx-lifecycle
        LifecycleOwner::class, // androidx-lifecycle-common,
        Mockito::class, // mockito
        KStubbing::class // mockito-kotlin
    ).map { PathUtil.getResourcePathForClass(it.java) }

    private var origLoader: ClassLoader? = null

    @BeforeMethod(alwaysRun = true)
    fun beforeMethod(args: Array<Any>) {
        val test = args.getOrNull(0) as? TestContext ?: return
        beforeRunTest(test)
        runKaptAndCompile(test)
    }

    @AfterMethod(alwaysRun = true)
    fun afterMethod(result: ITestResult, args: Array<Any>) {
        val test = args.getOrNull(0) as? TestContext ?: return
        afterRunTest(test, result.isSuccess)
    }

    fun beforeRunTest(test: TestContext) {
        test.beforeRunTest()
    }

    fun runKaptAndCompile(test: TestContext) {
        if (!test.performKapt && !test.performCompile) return

        test.sdkInt?.also { setSdkInt(it) }

        if (test.performKapt) {
            test.runKapt(compileClasspath, processors)
        }
        if (test.performCompile) {
            test.runCompile(compileClasspath)
        }

        origLoader = Thread.currentThread().contextClassLoader
        Thread.currentThread().contextClassLoader = test.classLoader
    }

    fun afterRunTest(test: TestContext, successful: Boolean) {
        origLoader?.let {
            Thread.currentThread().contextClassLoader = it
            origLoader = null
        }

        test.sdkInt?.also { setSdkInt(0) }
        test.afterRunTest(successful)
    }

    inline fun TestContext.use(body: TestContext.() -> Unit) {
        var successful = false
        try {
            beforeRunTest(this)
            runKaptAndCompile(this)
            body()
            successful = true
        } finally {
            afterRunTest(this, successful)
        }
    }

    fun TestContext.execute() = this.use { }
}

//
// Reflectively set SDK_INT
//

private val MODIFIERS_FIELD by lazy { Field::class.java.getDeclaredField("modifiers").apply { isAccessible = true } }
private val SDK_INT_FIELD by lazy {
    Build.VERSION::class.java.getDeclaredField("SDK_INT").apply {
        isAccessible = true
        MODIFIERS_FIELD.setInt(this, this.modifiers and Modifier.FINAL.inv())
    }
}

private fun setSdkInt(sdkInt: Int) = SDK_INT_FIELD.set(null, sdkInt)
