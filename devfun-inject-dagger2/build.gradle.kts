plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("com.nextfaze.devfun")
}

description = """Adds an instance provider (and/or user helper functions) to reflectively locate instances from Dagger 2.x Components.
    |
    |Should only be used if you are using Dagger 2.x. Should be on your debug configuration 'debugImplementation'.""".trimMargin()

configureAndroidLib()

dependencies {
    // Dev Fun
    kapt(project(":devfun-compiler"))
    api(project(":devfun"))
    implementation(project(":devfun-internal"))

    // Kotlin
    api(Dependency.kotlin.stdLib)

    // Android
    implementation(Dependency.supportAppCompat)

    // Dagger 2 - https://github.com/google/dagger
    compileOnly(Dependency.dagger)

    // Google AutoService - https://github.com/google/auto/tree/master/service
    kapt(Dependency.autoService)
    compileOnly(Dependency.autoService) {
        isTransitive = false
    }
}

configureDokka()
configurePublishing()
