import org.jetbrains.kotlin.gradle.dsl.Coroutines

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("com.nextfaze.devfun")
}

description = """Module that provides some utility functions when using Leak Canary.
    |
    |Does not do anything without Leak Canary. Should be on your debug configuration 'debugImplementation'.""".trimMargin()

configureAndroidLib()

dependencies {
    // DevFun
    kapt(project(":devfun-compiler"))
    api(project(":devfun-annotations"))

    // Kotlin
    api(Dependency.kotlin.stdLib)

    // Leak Canary - https://github.com/square/leakcanary
    compileOnly(Dependency.leakCanary)
}

configureDokka()
configurePublishing()
