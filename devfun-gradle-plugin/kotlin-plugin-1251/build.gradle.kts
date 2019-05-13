import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("kotlin")
    kotlin("kapt")
}

val kotlin = Dependency.kotlin("1.2.51")

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

    // Kotlin Compiler - 1.2.51 is not compatible with latest plugin versions
    kotlinCompilerClasspath(kotlin.reflect)
    kotlinCompilerClasspath(kotlin.compilerEmbeddable)

    // Google AutoService - https://github.com/google/auto/tree/master/service
    kapt(Dependency.autoService)
    compileOnly(Dependency.autoServiceAnnotations)
}

// Force specific Kotlin version
configurations.all {
    exclude(module = "kotlin-scripting-compiler-embeddable") // not present in 1.2.51
    resolutionStrategy {
        eachDependency {
            if (requested.group == "org.jetbrains.kotlin") {
                useVersion(kotlin.version)
            }
        }
    }
}
