import org.jetbrains.kotlin.gradle.internal.AndroidExtensionsExtension

plugins {
    id("com.android.library")
    kotlin("android")
    id("kotlin-android-extensions")
    kotlin("kapt")
    id("com.nextfaze.devfun")
}

description = """Core DevFun library that initializes and manages DevFun modules.
    |
    |On its own does not provide any functionality. Use with 'devfun-menu' or 'devfun-httpd', etc. Should be in your 'debugImplementation' configuration.""".trimMargin()

configureAndroidLib()

androidExtensions {
    // Kotlin DSL bug
    // https://youtrack.jetbrains.com/issue/KT-22213
    // Fix: https://github.com/gradle/kotlin-dsl/issues/644#issuecomment-398502551
    configure(delegateClosureOf<AndroidExtensionsExtension> {
        isExperimental = true
    })
}

dependencies {
    // DevFun
    kapt(project(":devfun-compiler"))
    api(project(":devfun-annotations"))
    implementation(project(":devfun-internal"))

    // Kotlin
    api(Dependency.kotlin.stdLib)
    implementation(Dependency.kotlin.reflect)
    implementation(Dependency.kotlin.coroutines)

    // Support libs
    implementation(Dependency.android.appCompat)
    implementation(Dependency.android.design)
    implementation(Dependency.android.constraintLayout)

    // JavaX Inject
    implementation(Dependency.javaxInject)

    // Google AutoService
    kapt(Dependency.autoService)
    compileOnly(Dependency.autoService) {
        isTransitive = false
    }
}

configureDokka()
configurePublishing()
