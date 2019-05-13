plugins {
    id("com.android.library")
    kotlin("android")
}

description = """Provides common functionality used across various DevFun modules, but not exposed to consumers.
    |
    |You should not use this directly, but if you do, do so knowing there are no compatibility or stability guarantees.""".trimMargin()

configureAndroidLib()

dependencies {
    // DevFun
    api(project(":devfun-annotations"))

    // Kotlin
    api(Dependency.kotlin.stdLib)

    // Logging
    api(Dependency.slf4jApi)

    // Android Support Annotations
    implementation(Dependency.android.annotations)
    implementation(Dependency.android.appCompat)
    implementation(Dependency.android.core)
}

configureDokka()
configurePublishing()
