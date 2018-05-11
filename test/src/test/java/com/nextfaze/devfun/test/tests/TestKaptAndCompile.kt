package com.nextfaze.devfun.test.tests

import com.nextfaze.devfun.generated.DevFunGenerated
import com.nextfaze.devfun.inject.*
import com.nextfaze.devfun.internal.*
import com.nextfaze.devfun.invoke.parameterInstances
import com.nextfaze.devfun.invoke.receiverInstance
import com.nextfaze.devfun.test.*
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import tested.kapt_and_compile.functions_with_args.FunctionsWithArgs
import tested.kapt_and_compile.functions_with_complex_types.FunctionsWithComplexTypes
import tested.kapt_and_compile.functions_with_primitive_args.FunctionsWithPrimitiveArgs
import tested.kapt_and_compile.interfaces.InterfacesWithDefaults
import tested.kapt_and_compile.interfaces.SimpleInterfaces
import tested.kapt_and_compile.simple_functions_in_classes.SimpleFunctionsInClasses
import tested.kapt_and_compile.simple_functions_in_nested_classes.SimpleFunctionsInNestedClasses
import tested.kapt_and_compile.simple_functions_in_nested_objects.SimpleFunctionsInNestedObjects
import tested.kapt_and_compile.simple_functions_in_objects.SimpleFunctionsInObjects
import tested.kapt_and_compile.simple_jvm_static_functions_in_classes.SimpleJvmStaticFunctionsInClasses
import tested.kapt_and_compile.simple_jvm_static_functions_in_nested_classes.SimpleJvmStaticFunctionsInNestedClasses
import tested.kapt_and_compile.simple_jvm_static_functions_in_nested_objects.SimpleJvmStaticFunctionsInNestedObjects
import tested.kapt_and_compile.simple_jvm_static_functions_in_objects.SimpleJvmStaticFunctionsInObjects
import tested.kapt_and_compile.simple_typed_functions.SimpleTypedFunctions
import tested.kapt_and_compile.top_level_functions.TopLevelFunctions
import java.lang.reflect.Method
import java.util.ServiceLoader
import kotlin.test.assertFails
import kotlin.test.assertTrue

class TestKaptAndCompile : AbstractKotlinKapt3Tester() {
    private val log = logger()

    @DataProvider(name = "testKaptAndCompileData")
    fun testKaptAndCompileData(testMethod: Method) = singleFileTests(
        testMethod,
        SimpleFunctionsInClasses::class,
        SimpleFunctionsInObjects::class,
        SimpleJvmStaticFunctionsInClasses::class,
        SimpleJvmStaticFunctionsInObjects::class,
        SimpleFunctionsInNestedClasses::class,
        SimpleFunctionsInNestedObjects::class,
        SimpleJvmStaticFunctionsInNestedClasses::class,
        SimpleJvmStaticFunctionsInNestedObjects::class,
        FunctionsWithPrimitiveArgs::class,
        FunctionsWithArgs::class,
        SimpleTypedFunctions::class,
        FunctionsWithComplexTypes::class,
        TopLevelFunctions::class
    )

    @Test(dataProvider = "testKaptAndCompileData", groups = ["kapt", "compile", "supported"])
    fun testKaptAndCompile(test: TestContext) {
        val generated = ServiceLoader.load(DevFunGenerated::class.java).single()

        // Check that there are the expected number of definitions
        val counts = test.files.map {
            it.useLines {
                it.map { it.substringBefore("//") }.fold(0 to 0) { acc, str ->
                    (acc.first + if (str.contains("@DeveloperCategory")) 1 else 0) to (acc.second + if (str.contains("@DeveloperFunction")) 1 else 0)
                }
            }
        }
        val catCount = counts.map { it.first }.sum()
        val funCount = counts.map { it.second }.sum()
        log.d { "Test declared $catCount category definitions and $funCount function definitions" }

        assertTrue("Expected $catCount category definitions but got ${generated.categoryDefinitions.size}") { generated.categoryDefinitions.size == catCount }
        assertTrue("Expected $funCount function definitions but got ${generated.functionDefinitions.size}") { generated.functionDefinitions.size == funCount }

        val instanceProviders = createDefaultCompositeInstanceProvider().apply {
            this += ConstructingInstanceProvider(this, false)
            this += KObjectInstanceProvider()
            this += captureInstance<InstanceProvider> { this }
            this += PrimitivesInstanceProvider()
            this += SimpleTypesInstanceProvider()
            test.classLoader.loadClasses<InstanceProvider>(test.testInstanceProviders).forEach {
                this@apply += this@apply[it]
            }
        }

        // Check that we can invoke all the functions
        generated.functionDefinitions.forEach {
            log.t { "Invoke $it" }
            it.invoke(it.receiverInstance(instanceProviders), it.parameterInstances(instanceProviders, null))
        }
    }

    @DataProvider(name = "testKaptAndCompileUnsupportedData")
    fun testKaptAndCompileUnsupportedData(testMethod: Method) = singleFileTests(
        testMethod,
        SimpleInterfaces::class,
        InterfacesWithDefaults::class,
        autoKaptAndCompile = false
    )

    @Test(dataProvider = "testKaptAndCompileUnsupportedData", groups = ["kapt", "unsupported"])
    fun testKaptAndCompileUnsupported(test: TestContext) {
        assertFails("Test should be failing for unsupported features for $test") {
            test.runKapt(compileClasspath, processors)
        }
    }
}
