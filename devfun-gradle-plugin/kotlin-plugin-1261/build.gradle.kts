plugins {
    id("kotlin")
    kotlin("kapt")
}

val kotlin = Dependency.kotlin("1.2.61")

dependencies {
    // DevFun
    compileOnly(project(":devfun-gradle-plugin:gradle-plugin")) {
        isTransitive = false
    }

    // Gradle
    compileOnly(gradleApi())

    // Kotlin
    compileOnly(kotlin.stdLib)
    compileOnly(kotlin.gradlePlugin)
    compileOnly(kotlin.gradlePluginApi)

    // Google AutoService - https://github.com/google/auto/tree/master/service
    kapt(Dependency.autoService)
    compileOnly(Dependency.autoService)
}

// Force specific Kotlin version
configurations.all {
    resolutionStrategy {
        eachDependency {
            if (requested.group == "org.jetbrains.kotlin") {
                useVersion(kotlin.version)
            }
        }
    }
}
