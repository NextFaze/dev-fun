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
    isExperimental = true
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
    compileOnly(Dependency.kotlin.androidExtensions) // setting 'isExperimental' doesn't seem to add dependency anymore?

    // Support libs
    implementation(Dependency.supportAppCompat)
    implementation(Dependency.supportDesign)
    implementation(Dependency.supportConstraintLayout)

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
