plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("com.nextfaze.devfun")
}

description = """Module that generates an admin interface using SB Admin 2 (similar to DevMenu), allowing function invocation from a browser.
    |
    |Use with 'devfun-httpd'. Should be on your debug configuration 'debugImplementation'.""".trimMargin()

configureAndroidLib()

dependencies {
    // Dev Fun
    kapt(project(":devfun-compiler"))
    api(project(":devfun-httpd"))
    implementation(project(":devfun-internal"))

    // Kotlin
    api(Dependency.kotlin.stdLib)

    // Google AutoService - https://github.com/google/auto/tree/master/service
    kapt(Dependency.autoService)
    compileOnly(Dependency.autoServiceAnnotations)
}

configureDokka()
configurePublishing()
