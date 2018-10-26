plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("com.nextfaze.devfun")
}

android {
    compileSdkVersion(Android.compileSdkVersion)

    defaultConfig {
        minSdkVersion(Android.minSdkVersion)
        targetSdkVersion(Android.targetSdkVersion)
        versionCode = Android.versionCode
        versionName = project.versionName

        testInstrumentationRunner = "androidx.test.runner.MonitoringInstrumentation"

        javaCompileOptions {
            annotationProcessorOptions {
                // We're using KAPT so ignore annotationProcessor configuration dependencies
                includeCompileClasspath = false
                if (project.isSnapshot) {
                    argument("devfun.debug.verbose", "true")
                }
                argument("devfun.interfaces.generate", "true")
                argument("devfun.definitions.generate", "false")
                argument("devfun.elements.exclude", "tested.kapt_and_compile.interfaces.")
            }
        }
    }

    sourceSets {
        getByName("test").java.srcDirs("src/testData/kotlin")
    }

    testOptions {
        unitTests.setReturnDefaultValues(true)
    }
}

dependencies {
    // Dev Fun
    testImplementation(project(":devfun-compiler"))
    testImplementation(project(":devfun"))
    testImplementation(project(":devfun-internal"))
    kaptTest(project(":devfun-compiler"))

    // TestNG - http://testng.org
    testImplementation("org.testng:testng:6.13.1")

    // Logging - https://logback.qos.ch
    testImplementation("ch.qos.logback:logback-core:1.2.3")
    testImplementation("ch.qos.logback:logback-classic:1.2.3")

    // Kotlin
    testImplementation(Dependency.kotlin.annotationProcessingEmbeddable)
    testImplementation(Dependency.kotlin.compilerEmbeddable)
    testImplementation(Dependency.kotlin.test)
    testImplementation(Dependency.kotlin.reflect)

    // AutoService
    testImplementation(Dependency.autoService)

    // Mockito - http://mockito.org/
    testImplementation("org.mockito:mockito-inline:2.13.0")
    testImplementation("com.nhaarman:mockito-kotlin:1.5.0")

    // Javac Tools
    val toolsJar = File("${System.getProperty("java.home")}/../lib/tools.jar")
    if (!toolsJar.exists()) {
        throw RuntimeException("Tools jar not found at ${toolsJar.canonicalPath}")
    }
    testImplementation(files(toolsJar.canonicalPath))
}

project.afterEvaluate {
    val tests = project.getTasksByName("testDebugUnitTest", false) + project.getTasksByName("testReleaseUnitTest", false)
    tests.forEach {
        it.apply {
            this as Test
            useTestNG()
            testLogging.showStandardStreams = true
        }
    }
}
