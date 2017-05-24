package com.nextfaze.devfun.test.tests

import com.nextfaze.devfun.compiler.*
import com.nextfaze.devfun.generated.DevFunGenerated
import com.nextfaze.devfun.test.AbstractKotlinKapt3Tester
import com.nextfaze.devfun.test.TestContext
import org.jetbrains.kotlin.preprocessor.mkdirsOrFail
import org.testng.annotations.Test
import tested.kapt_and_compile.simple_functions_in_classes.SimpleFunctionsInClasses
import java.io.File
import java.lang.reflect.Method
import java.util.ServiceLoader
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.expect

@Test(groups = arrayOf("kapt", "compile", "supported", "compiler", "SupportedOptions"))
class TestCompilerArgs : AbstractKotlinKapt3Tester() {
    fun testDefaultOptionsNoBuildConfig(method: Method) {
        assertFailsWith<IllegalStateException>("This should not have succeeded as there is not BuildConfig.java") {
            TestContext(
                    testMethodName = method.name,
                    testFiles = listOf(SimpleFunctionsInClasses::class),
                    kaptOptions = mapOf(FLAG_DEBUG_VERBOSE to "true")
            ).execute()
        }
    }

    fun testDefaultOptions(method: Method) = TestContext(
            testMethodName = method.name,
            testFiles = listOf(SimpleFunctionsInClasses::class),
            kaptOptions = mapOf(FLAG_DEBUG_VERBOSE to "true"))
            .apply { createGenerated() }
            .use {
                val devFun = ServiceLoader.load(DevFunGenerated::class.java).single()
                expect("$applicationId.$buildType.$PACKAGE_SUFFIX_DEFAULT") { devFun::class.java.`package`.name }
            }

    fun testRootPackage(method: Method) = TestContext(
            testMethodName = method.name,
            testFiles = listOf(SimpleFunctionsInClasses::class),
            kaptOptions = mapOf(
                    FLAG_DEBUG_VERBOSE to "true",
                    PACKAGE_ROOT to "custom_package_root"))
            .apply { createGenerated() }
            .use {
                val devFun = ServiceLoader.load(DevFunGenerated::class.java).single()
                val actualPackage = devFun::class.java.`package`.name
                expect("custom_package_root.$PACKAGE_SUFFIX_DEFAULT") { actualPackage }
                assertFalse(actualPackage.startsWith(applicationId), "Actual package '$actualPackage' should not start with applicationId=$applicationId")
            }

    fun testPackageSuffix(method: Method) = TestContext(
            testMethodName = method.name,
            testFiles = listOf(SimpleFunctionsInClasses::class),
            kaptOptions = mapOf(
                    FLAG_DEBUG_VERBOSE to "true",
                    PACKAGE_SUFFIX to "custom_suffix"))
            .apply { createGenerated() }
            .use {
                val devFun = ServiceLoader.load(DevFunGenerated::class.java).single()
                val actualPackage = devFun::class.java.`package`.name
                expect("$applicationId.$buildType.custom_suffix") { devFun::class.java.`package`.name }
                assertTrue(actualPackage.startsWith(applicationId), "Actual package '$actualPackage' should start with applicationId=$applicationId")
                assertFalse(actualPackage.endsWith(PACKAGE_SUFFIX_DEFAULT), "Actual package '$actualPackage' should not end with PACKAGE_SUFFIX_DEFAULT=$PACKAGE_SUFFIX_DEFAULT")
            }

    fun testPackageRootAndSuffix(method: Method) = TestContext(
            testMethodName = method.name,
            testFiles = listOf(SimpleFunctionsInClasses::class),
            kaptOptions = mapOf(
                    FLAG_DEBUG_VERBOSE to "true",
                    PACKAGE_ROOT to "another.custom.pkg.root",
                    PACKAGE_SUFFIX to "custom.suffix"))
            .apply { createGenerated() }
            .use {
                val devFun = ServiceLoader.load(DevFunGenerated::class.java).single()
                val actualPackage = devFun::class.java.`package`.name
                expect("another.custom.pkg.root.custom.suffix") { devFun::class.java.`package`.name }
                assertFalse(actualPackage.startsWith(applicationId), "Actual package '$actualPackage' should not start with applicationId=$applicationId")
                assertFalse(actualPackage.endsWith(PACKAGE_SUFFIX_DEFAULT), "Actual package '$actualPackage' should not end with PACKAGE_SUFFIX_DEFAULT=$PACKAGE_SUFFIX_DEFAULT")
            }

    fun testPackageOverride(method: Method) = TestContext(
            testMethodName = method.name,
            testFiles = listOf(SimpleFunctionsInClasses::class),
            kaptOptions = mapOf(
                    FLAG_DEBUG_VERBOSE to "true",
                    FLAG_LIBRARY to "true",
                    PACKAGE_OVERRIDE to "com.example.application.generated"))
            .use {
                val devFun = ServiceLoader.load(DevFunGenerated::class.java).single()
                val actualPackage = devFun::class.java.`package`.name
                expect("com.example.application.generated") { devFun::class.java.`package`.name }
                assertFalse(actualPackage.startsWith(applicationId), "Actual package '$actualPackage' should not start with applicationId=$applicationId")
                assertFalse(actualPackage.endsWith(PACKAGE_SUFFIX_DEFAULT), "Actual package '$actualPackage' should not end with PACKAGE_SUFFIX_DEFAULT=$PACKAGE_SUFFIX_DEFAULT")
            }
}

private fun TestContext.createGenerated() {
    run buildConfigFile@ {
        //language=JAVA
        val buildConfig = """ // We're using %ARG% just so we can use IntelliJ Language injection (can't escape $ args in raw strings)
/*
 * Automatically generated file. DO NOT MODIFY
 */
package com.my.pkg;

public final class BuildConfig {
  public static final boolean DEBUG = Boolean.parseBoolean("true");
  public static final String APPLICATION_ID = "%TEST_APPLICATION_ID%";
  public static final String BUILD_TYPE = "%TEST_BUILD_TYPE%";
  public static final String FLAVOR = "%TEST_FLAVOR%";
  public static final int VERSION_CODE = 1;
  public static final String VERSION_NAME = "0.0.0-SNAPSHOT";
}
"""
        File(compileDir, "generated/source/buildConfig/BuildConfig.java").apply {
            parentFile.mkdirsOrFail()
            writeText(buildConfig
                    .substringAfter("\n") // skip first blank line
                    .replace("com.my.pkg", applicationId)
                    .replace("%TEST_APPLICATION_ID%", applicationId)
                    .replace("%TEST_BUILD_TYPE%", buildType)
                    .replace("%TEST_FLAVOR%", flavor))
        }
    }

    run resourceIdsFile@ {
        // It will be considered a "library project" if it does not encounter static fields
        // i.e. blank file = lib project
        File(compileDir, "generated/source/r/$variantDir/${applicationId.replace('.', '/')}/R.java").apply {
            parentFile.mkdirsOrFail()
            createNewFile()
        }
    }
}
