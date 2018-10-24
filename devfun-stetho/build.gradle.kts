plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

description = """Experimental module that allows generated methods to be invoked from Chrome's Dev Tools JavaScript console.
    |
    |Should only be used if you are using Stetho. Should be on your debug configuration 'debugImplementation'.""".trimMargin()

configureAndroidLib()

dependencies {
    // DevFun
    api(project(":devfun"))
    implementation(project(":devfun-internal"))

    // Kotlin
    api(Dependency.kotlin.stdLib)

    // Stetho
    api(Dependency.stetho)
    implementation(Dependency.stethoJsRhino)

    // Google AutoService
    kapt(Dependency.autoService)
    compileOnly(Dependency.autoService)
}

configureDokka()
configurePublishing()
