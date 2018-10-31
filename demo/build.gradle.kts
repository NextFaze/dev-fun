plugins {
    id("com.android.application")
    id("kotlin-android-extensions")
    kotlin("android")
    kotlin("kapt")
    id("com.nextfaze.devfun")
}

androidExtensions {
    isExperimental = true
}

android {
    compileSdkVersion(Android.compileSdkVersion)

    defaultConfig {
        applicationId = "com.nextfaze.devfun.demo"

        minSdkVersion(Android.minSdkVersion)
        targetSdkVersion(Android.targetSdkVersion)
        versionCode = Android.versionCode
        versionName = project.versionName
        multiDexEnabled = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        javaCompileOptions {
            annotationProcessorOptions {
                // We're using KAPT so ignore annotationProcessor configuration dependencies
                includeCompileClasspath = false
                if (project.isSnapshot) {
                    argument("devfun.debug.verbose", "true")
//                    argument("devfun.logging.note.promote", "true")
                }
            }
        }
    }

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"
            isMinifyEnabled = getBooleanProperty("demo.minify.enabled", false)
            proguardFiles("proguard-rules.pro")
        }
        getByName("release") {
            isMinifyEnabled = getBooleanProperty("demo.minify.enabled", false)
            proguardFiles("proguard-rules.pro")
        }
    }
}

dependencies {
    // Dev Fun
    kaptDebug(project(":devfun-compiler"))
    implementation(project(":devfun-annotations"))
    debugImplementation(project(":devfun-inject-dagger2"))
    debugImplementation(project(":devfun-menu"))
    debugImplementation(project(":devfun-httpd"))
    debugImplementation(project(":devfun-httpd-frontend"))

    // Kotlin
    implementation(Dependency.kotlin.stdLib)
    implementation(Dependency.kotlin.reflect)
    implementation(Dependency.kotlin.coroutines)
    implementation(Dependency.kotlin.coroutinesAndroid)

    // Support libs
    implementation(Dependency.android.appCompat)
    implementation(Dependency.android.design)
    implementation(Dependency.android.constraintLayout)
    implementation(Dependency.android.multidex)

    // Logging - https://github.com/tony19/logback-android
    implementation(Dependency.slf4jApi)
    implementation("com.github.tony19:logback-android-core:1.1.1-6")
    implementation("com.github.tony19:logback-android-classic:1.1.1-6") {
        exclude(group = "com.google.android", module = "android")
    }

    // Dagger 2 - https://github.com/google/dagger
    val daggerVersion = getStringProperty("testDaggerVersion", Dependency.daggerVersion)
    kapt(Dependency.daggerCompiler(daggerVersion))
    implementation(Dependency.dagger(daggerVersion))
    compileOnly(Dependency.daggerAnnotations)

    // OkHttp - https://github.com/square/okhttp
    implementation("com.squareup.okhttp3:okhttp:3.11.0")

    // Joda Time - https://github.com/dlew/joda-time-android
    implementation("net.danlew:android.joda:2.9.9.4")

    // Stetho - https://github.com/facebook/stetho
    debugImplementation(Dependency.stetho)
    debugImplementation(Dependency.stethoJsRhino)
    debugImplementation(project(":devfun-stetho"))

    // Glide - https://github.com/bumptech/glide
    implementation(Dependency.glide)
    implementation("com.github.bumptech.glide:okhttp3-integration:4.8.0@aar")
    debugImplementation(project(":devfun-util-glide"))

    // Leak Canary - https://github.com/square/leakcanary
    debugImplementation(Dependency.leakCanary)
    debugImplementation(project(":devfun-util-leakcanary"))

    // RxJava: Reactive Extensions for the JVM - https://github.com/ReactiveX/RxJava
    implementation("io.reactivex.rxjava2:rxjava:2.2.2")

    // RxJava bindings for Android - https://github.com/ReactiveX/RxAndroid
    implementation("io.reactivex.rxjava2:rxandroid:2.1.0")

    // RxKotlin
    implementation("io.reactivex.rxjava2:rxkotlin:2.3.0")

    // Auto Disposable
    implementation("com.uber.autodispose:autodispose-kotlin:0.8.0")
    implementation("com.uber.autodispose:autodispose-android-kotlin:0.8.0")
    implementation("com.uber.autodispose:autodispose-android-archcomponents-kotlin:0.8.0")

    // Instrumentation tests
    androidTestImplementation(Dependency.kotlin.test)

    androidTestImplementation(Dependency.android.testRules)
    androidTestImplementation(Dependency.android.testRunner)
    androidTestImplementation(Dependency.android.espressoCore)
}

project.afterEvaluate {
    getTasksByName("connectedDebugAndroidTest", false).single().apply {
        doFirst {
            val testDaggerVersion = getStringProperty("testDaggerVersion", "")
            if (testDaggerVersion.isNotEmpty()) {
                println("testDaggerVersion=$testDaggerVersion")
            }
        }
    }
}
