plugins {
    id("com.android.library")
    kotlin("android")
    id("kotlin-android-extensions")
    kotlin("kapt")
    id("com.nextfaze.devfun")
}

description = """Adds a developer menu, accessible by a floating cog (long-press to drag) or device button sequence.
    |
    |Should be on your debug configuration 'debugImplementation'.""".trimMargin()

configureAndroidLib()

dependencies {
    // DevFun
    kapt(project(":devfun-compiler"))
    api(project(":devfun"))
    implementation(project(":devfun-internal"))
    implementation(project(":devfun-invoke-view-colorpicker"))

    // Kotlin
    api(Dependency.kotlin.stdLib)

    // Support libs
    api(Dependency.supportAppCompat)
    implementation(Dependency.supportRecyclerView)
    implementation(Dependency.supportConstraintLayout)

    // Google AutoService
    kapt(Dependency.autoService)
    compileOnly(Dependency.autoService)
}

configureDokka()
configurePublishing()
