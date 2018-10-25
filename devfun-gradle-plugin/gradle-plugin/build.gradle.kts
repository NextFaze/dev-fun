plugins {
    id("kotlin")
    kotlin("kapt")
}

dependencies {
    // DevFun
    compileOnly(project(":devfun-compiler")) // we only reference constants (DevFunProcessor's supported options)

    // Gradle
    compileOnly(gradleApi())

    // Kotlin
    compileOnly(Dependency.kotlin.stdLib)
    compileOnly(Dependency.kotlin.gradlePlugin)
    compileOnly(Dependency.kotlin.gradlePluginApi)

    // Android
    compileOnly(Dependency.androidPlugin) {
        exclude(group = "org.jetbrains.kotlin")
    }

    // Google AutoService - https://github.com/google/auto/tree/master/service
    kapt(Dependency.autoService)
    compileOnly(Dependency.autoService)
}

configureDokka()
createVersionFile(pkg = "com.nextfaze.devfun.gradle.plugin")
