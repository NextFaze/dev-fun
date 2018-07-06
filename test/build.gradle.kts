plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    compileSdkVersion(Android.compileSdkVersion)

    defaultConfig {
        minSdkVersion(Android.minSdkVersion)
        targetSdkVersion(Android.targetSdkVersion)
        versionCode = Android.versionCode
        versionName = project.versionName

        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"

        javaCompileOptions {
            annotationProcessorOptions {
                // We're using KAPT so ignore annotationProcessor configuration dependencies
                includeCompileClasspath = false
                if (project.isSnapshot) {
                    argument("devfun.debug.verbose", "true")
                }
            }
        }
    }

    sourceSets {
        getByName("test").java.srcDirs("src/testData/kotlin")
    }
}

dependencies {
    // Dev Fun
    testImplementation(project(":devfun-compiler"))
    testImplementation(project(":devfun"))
    testImplementation(project(":devfun-internal"))

    // TestNG - http://testng.org
    testImplementation("org.testng:testng:6.13.1")

    // Logging - https://logback.qos.ch
    testImplementation("ch.qos.logback:logback-core:1.2.3")
    testImplementation("ch.qos.logback:logback-classic:1.2.3")

    // Kotlin
    testImplementation("org.jetbrains.kotlin:kotlin-annotation-processing-embeddable:${Dependency.kotlinVersion}")
    testImplementation("org.jetbrains.kotlin:kotlin-compiler-embeddable:${Dependency.kotlinVersion}")
    testImplementation("org.jetbrains.kotlin:kotlin-compiler:${Dependency.kotlinVersion}:sources") // because sources aren't auto-linking
    testImplementation("org.jetbrains.kotlin:kotlin-test:${Dependency.kotlinVersion}")
    testImplementation("org.jetbrains.kotlin:kotlin-reflect:${Dependency.kotlinVersion}")

    // Mockito - http://mockito.org/
    testImplementation("org.mockito:mockito-inline:2.13.0")
    testImplementation("com.nhaarman:mockito-kotlin:1.5.0")

    // Javac Tools
    val toolsJar = File("${System.getProperty("java.home")}/../lib/tools.jar")
    if (!toolsJar.exists()) {
        throw RuntimeException("Tools jar not found at ${toolsJar.canonicalPath}")
    }
    testImplementation(files(toolsJar.canonicalPath))

    // Full Java
    val bootJars = System.getProperty("sun.boot.class.path").toString().split(File.pathSeparator)
    val jars = bootJars.filter { it.endsWith("rt.jar") }.map { File(it) }
    testImplementation(files(jars))
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
