package com.nextfaze.devfun.test

import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.app.KeyguardManager
import android.content.Context
import android.view.WindowManager
import com.nextfaze.devfun.compiler.PACKAGE_OVERRIDE
import com.nextfaze.devfun.compiler.PACKAGE_ROOT
import com.nextfaze.devfun.core.*
import com.nextfaze.devfun.error.ErrorDetails
import com.nextfaze.devfun.error.ErrorHandler
import com.nextfaze.devfun.generated.DevFunGenerated
import com.nextfaze.devfun.inject.*
import com.nextfaze.devfun.internal.log.*
import com.nextfaze.devfun.invoke.doInvoke
import com.nextfaze.devfun.invoke.parameterInstances
import com.nextfaze.devfun.invoke.receiverInstance
import com.nextfaze.devfun.test.kotlin.KotlinCore
import com.nhaarman.mockito_kotlin.KStubbing
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import org.mockito.Mockito
import org.slf4j.Logger
import java.io.File
import java.lang.reflect.Method
import java.net.URL
import java.net.URLClassLoader
import java.nio.file.Files
import java.util.Collections
import java.util.Enumeration
import javax.annotation.processing.Processor
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaMethod
import kotlin.reflect.jvm.kotlinProperty
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

private val TEST_SOURCES_DIR = File("src/test/java")
private val TEST_DATA_DIR = File("src/testData/kotlin")

class TestContext(
    val testMethodName: String,
    val testFiles: List<KClass<*>>,
    private val testDirSuffix: String = testFiles.joinToString("_") { it.simpleName!! },
    val testDir: File = Files.createTempDirectory("devfun_testing.$testMethodName.$testDirSuffix").toFile(),
    val applicationId: String = "tested.com.nextfaze.devfun",
    val buildType: String = "kapt3Test",
    val flavor: String = "",
    private val testDataDir: File = TEST_DATA_DIR,
    val performKapt: Boolean = true,
    val performCompile: Boolean = true,
    val sdkInt: Int? = null,
    val kaptOptions: Map<String, String> = devFunKaptOptions(packageSuffix = testDir.name),
    private val copyFailedTests: Boolean = true,
    private val copySuccessfulTests: Boolean = false,
    val keepFailedTestOutputs: Boolean = true,
    val keepSuccessfulTestOutputs: Boolean = false
) {
    private val log = logger()
    override fun toString() = "$testMethodName.$testDirSuffix"

    val variantDir = when {
        flavor.isEmpty() -> buildType
        buildType.isEmpty() -> flavor
        else -> "$flavor/$buildType"
    }
    private val packageRoot = kaptOptions.getOrDefault(PACKAGE_ROOT, TEST_PACKAGE_ROOT)
    private val packageOverride = kaptOptions[PACKAGE_OVERRIDE]

    private val testDataFiles = testFiles.map { File(testDataDir, "${it.qualifiedName!!.replace('.', File.separatorChar)}.kt") }
    private val providedFiles = listOf(TestInstanceProviders::class, Assertions::class, TestUtil::class)
        .map { File(TEST_SOURCES_DIR, "${it.qualifiedName!!.replace('.', File.separatorChar)}.kt") }

    val kotlinFiles = testDataFiles + providedFiles
    val javaFiles = testDataFiles.flatMap { it.parentFile.walkTopDown().filter { it.extension == "java" }.toList() }

    val testInstanceProviders = testFiles
        .filter { it.isSubclassOf(TestInstanceProviders::class) }
        .map { it.objectInstance as TestInstanceProviders }
        .flatMap { it.testProviders.map { it.qualifiedName!! } }
        .toSet()

    private val kotlinCore by lazy {
        KotlinCore(
            testDir = testDir,
            variantDirName = variantDir,
            kaptOptions = kaptOptions
        )
    }

    val classLoader: ClassLoader by lazy {
        val classpath = listOf(kotlinCore.classesOutputDir).map { it.toURI().toURL() }
        WrappingUrlClassLoader(classpath, Thread.currentThread().contextClassLoader, packageOverride ?: packageRoot)
    }

    fun beforeRunTest() {
        log.d {
            """
            |
            |===== Test =====
            |testMethodName: $testMethodName
            |testDir: $testDir
            |testDirSuffix: $testDirSuffix
            |variantDir: $variantDir
            |
            |performKapt: $performKapt
            |performCompile: $performCompile
            |compileDir: ${kotlinCore.compileDir}
            |classesOutputDir: ${kotlinCore.classesOutputDir}
            |""".trimMargin()
        }
    }

    fun afterRunTest(successful: Boolean) {
        if (keepSuccessfulTestOutputs || keepFailedTestOutputs) {
            kotlinCore.logTestOutputs(successful, toString().replace(", ", "\n> "), kotlinFiles, javaFiles)
        }

        if (successful) {
            if (copySuccessfulTests) {
                kotlinCore.copyGenerated(File(testDataDir, "_succeeded"))
            }
            if (!keepSuccessfulTestOutputs) {
                testDir.deleteRecursively()
            }
        } else {
            if (copyFailedTests) {
                kotlinCore.copyGenerated(File(testDataDir, "_failed"))
            }
            if (!keepFailedTestOutputs) {
                testDir.deleteRecursively()
            }
        }
    }

    fun runKapt(compileClasspath: List<File>, processors: List<Processor>) {
        kotlinCore.runKapt(compileClasspath, processors, kotlinFiles, javaFiles)
    }

    fun runCompile(compileClasspath: List<File>) {
        kotlinCore.runCompile(compileClasspath, kotlinFiles, javaFiles)
    }

    fun writeGeneratedFile(path: String, contents: String) {
        File(kotlinCore.compileDir, path).apply {
            parentFile.mkdirs()
            writeText(contents)
        }
    }

    val devFun: DevFun by lazy {
        val application = mock<Application>(defaultAnswer = Mockito.RETURNS_DEEP_STUBS)
        KStubbing(application).apply {
            on { applicationContext } doReturn application
            on { getSystemService(Context.ACTIVITY_SERVICE) } doReturn mock<ActivityManager>()
            on { getSystemService(Context.KEYGUARD_SERVICE) } doReturn mock<KeyguardManager>()
            on { getSystemService(Context.WINDOW_SERVICE) } doReturn mock<WindowManager>()
        }

        val context = mock<Context> {
            on { applicationContext } doReturn application
        }
        val activity = mock<Activity> {
            on { applicationContext } doReturn application
        }
        val activityTracker = mock<ActivityProvider> {
            on { this.invoke() } doReturn activity
        }

        DevFun().apply {
            devFunVerbose = false
            initialize(context)
            instanceProviders.apply {
                this[ConstructingInstanceProvider::class].requireConstructable = false
                this += captureInstance { activityTracker.invoke() }
                this += PrimitivesInstanceProvider()
                this += SimpleTypesInstanceProvider()
                this += singletonInstance<ErrorHandler> {
                    object : ErrorHandler {
                        override fun onWarn(title: CharSequence, body: CharSequence) = Unit
                        override fun onError(t: Throwable, title: CharSequence, body: CharSequence, functionItem: FunctionItem?) = throw t
                        override fun onError(error: ErrorDetails) = throw error.t
                        override fun markSeen(key: Any) = Unit
                        override fun remove(key: Any) = Unit
                        override fun clearAll() = Unit
                    }
                }
                classLoader.loadClasses<InstanceProvider>(testInstanceProviders).forEach {
                    this += instanceProviders[it]
                }
            }
        }
    }

    val funDefs by lazy { devFun.definitions.flatMap { it.functionDefinitions }.toSet() }
    val catDefs by lazy { devFun.definitions.flatMap { it.categoryDefinitions }.toSet() }
    val devRefs by lazy { devFun.definitions.flatMap { it.developerReferences }.toSet() }

    private val categories by lazy { devFun.categories }
    private val funDefItems by lazy { categories.flatMap { it.items }.toSet().groupBy { it.function } }

    fun testInvocations(logger: Logger) {
        funDefs.forEach { fd ->
            logger.d { "Invoke $fd" }

            val dfIp = DevFunItemInstanceProvider(functionItem = null, functionDefinition = fd)
            devFun.instanceProviders += dfIp

            val receiver = fd.receiverInstance(devFun.instanceProviders)
            if (fd.method.name.startsWith("test")) {
                when {
                    fd.method.name.endsWith("\$annotations") -> fd.getterMethod!!.invoke(receiver) // is property
                    else -> fd.invoke(receiver, fd.parameterInstances(devFun.instanceProviders, null))
                }
            } else {
                val v = when {
                    fd.method.name.endsWith("\$annotations") -> fd.getterMethod!!.invoke(receiver) // is property
                    else -> fd.invoke(receiver, fd.parameterInstances(devFun.instanceProviders, null))
                }
                val value = if (v is Pair<*, *>) v.first else v
                val testable = when (value) {
                    is List<*> -> value
                    else -> listOf(value)
                }
                logger.d { "Test $testable for $fd" }
                testable.filterIsInstance<Assertable>().forEach {
                    val result = it.invoke(fd, funDefItems[fd].orEmpty())
                    logger.d { "> $result" }
                }
            }

            logger.d { "\n" }
            devFun.instanceProviders -= dfIp
        }

        categories.forEach { cat ->
            cat.items.forEach {
                logger.d { "Invoke $it" }

                val dfIp = DevFunItemInstanceProvider(functionItem = it, categoryItem = cat)
                devFun.instanceProviders += dfIp

                val fd = it.function
                val receiver = fd.receiverInstance(devFun.instanceProviders)
                if (it.function.method.name.startsWith("test")) {
                    when {
                        fd.method.name.endsWith("\$annotations") -> fd.getterMethod!!.invoke(receiver) // is property
                        else -> it.invoke(
                            fd.receiverInstance(devFun.instanceProviders),
                            fd.parameterInstances(devFun.instanceProviders, it.args)
                        )
                    }
                } else {
                    val v = when {
                        fd.method.name.endsWith("\$annotations") -> fd.getterMethod!!.invoke(receiver) // is property
                        else -> it.invoke(
                            fd.receiverInstance(devFun.instanceProviders),
                            fd.parameterInstances(devFun.instanceProviders, it.args)
                        )
                    }
                    val value = if (v is Pair<*, *>) v.second else v
                    val testable = when (value) {
                        is List<*> -> value
                        else -> listOf(value)
                    }
                    logger.d { "Test $testable for $it" }
                    testable.filterIsInstance<Assertable>().forEach {
                        val result = it.invoke(fd, funDefItems[fd].orEmpty())
                        logger.d { "> $result" }
                    }
                }

                logger.d { "\n" }
                devFun.instanceProviders -= dfIp
            }
        }

        devRefs.forEach { ref ->
            when (ref) {
                is DeveloperMethodReference -> {
                    logger.d { "Invoke developer reference ${ref.method} ..." }
                    if (ref.method.name.startsWith("test")) {
                        ref.method.doInvoke(devFun.instanceProviders, listOf(ref))
                    } else {
                        assertEquals(
                            true,
                            ref.method.doInvoke(devFun.instanceProviders),
                            "Unexpected return value for dev method reference."
                        )
                    }
                }
                is DeveloperTypeReference -> {
                    logger.d { "Get instance of developer type reference ${ref.type} ..." }
                    assertNotNull(devFun.instanceOf(ref.type), "Failed to get instance of referenced type: ${ref.type}")
                }
                is DeveloperFieldReference -> {
                    logger.d { "Get value of developer field reference ${ref.field} ..." }
                    assertEquals(true, ref.field.get(devFun.instanceOf(ref.field.declaringClass.kotlin)), "Referenced field did not match.")
                }
                else -> throw RuntimeException("Unexpected ref type $ref (${ref::class})")
            }
        }
    }

    private val FunctionDefinition.getterMethod: Method?
        get() {
            val fieldName = method.name.substringBefore('$')
            val propertyField = try {
                clazz.java.getDeclaredField(fieldName).apply { isAccessible = true }
            } catch (ignore: NoSuchFieldException) {
                null // is property without backing field (i.e. has custom getter/setter)
            }
            val property = when {
                propertyField != null -> propertyField.kotlinProperty!!
                else -> clazz.declaredMemberProperties.first { it.name == fieldName }
            }.apply { isAccessible = true }


            // Kotlin reflection has weird accessibility issues when invoking get/set/getter/setter .call()
            // it only seems to work the first time with subsequent calls failing with illegal access exceptions and the like
            return property.getter.javaMethod?.apply { isAccessible = true }
        }
}

// TODO add functionality to DevFun?
class DevFunItemInstanceProvider(
    private val functionItem: FunctionItem?,
    private val functionDefinition: FunctionDefinition? = functionItem?.function,
    private val categoryDefinition: CategoryDefinition? = functionItem?.category,
    private val categoryItem: CategoryItem? = null
) : InstanceProvider {
    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> get(clazz: KClass<out T>): T? =
        when {
            clazz.isSubclassOf<FunctionItem>() -> functionItem as T?
            clazz.isSubclassOf<FunctionDefinition>() -> functionDefinition as T?
            clazz.isSubclassOf<CategoryDefinition>() -> categoryDefinition as T?
            clazz.isSubclassOf<CategoryItem>() -> categoryItem as T?
            else -> null
        }
}


//
// ClassLoader "fun"
//

@Suppress("UNCHECKED_CAST")
internal fun <T : Any> ClassLoader.loadClasses(classes: Iterable<String>) = classes.map { loadClass(it).kotlin as KClass<out T> }

private class WrappingUrlClassLoader(
    urls: List<URL>,
    private val wrapped: ClassLoader,
    testPackageRoot: String
) : URLClassLoader(urls.toTypedArray(), null) {
    private val testedRoot = "$TEST_PACKAGE_ROOT."
    private val packageRoot = "$testPackageRoot."
    private val noDelegateResources = ".${DevFunGenerated::class.simpleName}"

    override fun loadClass(name: String, resolve: Boolean): Class<*> =
        if (name.startsWith(testedRoot) || name.startsWith(packageRoot)) {
            try {
                super.loadClass(name, resolve)
            } catch (t: Throwable) {
                wrapped.loadClass(name)
            }
        } else {
            wrapped.loadClass(name)
        }

    override fun findResources(name: String): Enumeration<URL> {
        if (name.endsWith(noDelegateResources)) return super.findResources(name)

        val resources = super.findResources(name).toList()
        return when {
            resources.isNotEmpty() -> Collections.enumeration(resources)
            else -> wrapped.getResources(name)
        }
    }
}
