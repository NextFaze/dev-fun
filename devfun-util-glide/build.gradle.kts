import org.jetbrains.kotlin.gradle.dsl.Coroutines

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("com.nextfaze.devfun")
}

description = """Module that provides some utility functions when using Glide.
    |
    |Does not do anything without Glide. Should be on your debug configuration 'debugImplementation'.""".trimMargin()

configureAndroidLib()

kotlin {
    experimental.coroutines = Coroutines.ENABLE
}

dependencies {
    // DevFun
    kapt(project(":devfun-compiler"))
    api(project(":devfun"))
    implementation(project(":devfun-internal"))

    // Kotlin
    api(Dependency.kotlinStdLib)
    api(Dependency.kotlinCoroutines)

    // Glide - https://github.com/bumptech/glide
    compileOnly(Dependency.glide)

    // Google AutoService
    kapt(Dependency.autoService)
    compileOnly(Dependency.autoService)
}

configureDokka()
configurePublishing()
