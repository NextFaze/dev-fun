plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("com.nextfaze.devfun")
}

description = """Module that adds a local HTTP server (uses NanoHttpD).
    |
    |Provides a single 'POST' method 'invoke' with one parameter 'hashCode' - expecting FunctionItem.hashCode()
    |Use with 'devfun-httpd-frontend'. Should be on your debug configuration 'debugImplementation'.""".trimMargin()

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

    // Http Server
    api("org.nanohttpd:nanohttpd:2.3.1")
    api("org.nanohttpd:nanohttpd-nanolets:2.3.1")

    // Google AutoService - https://github.com/google/auto/tree/master/service
    kapt(Dependency.autoService)
    compileOnly(Dependency.autoService) {
        isTransitive = false
    }
}

configureDokka()
configurePublishing()
