import groovy.lang.Closure
import org.gradle.kotlin.dsl.get
import kotlin.reflect.KCallable
import kotlin.reflect.KFunction

plugins {
    id("kotlin")
    kotlin("kapt")
    id("maven-publish")
    id("com.gradle.plugin-publish") version "0.9.10"
}

description = "Gradle plugin that facilitates DevFun annotation processor configuration."

dependencies {
    compile(project(":devfun-compiler"))

    // Gradle
    compileOnly(gradleApi())

    // Kotlin
    compile(Dependency.kotlinStdLib)
    compile(Dependency.kotlinPlugin)
    compile(Dependency.kotlinPluginApi)

    // Android
    compileOnly(Dependency.androidPlugin) {
        exclude(group = "org.jetbrains.kotlin")
    }

    // Google AutoService - https://github.com/google/auto/tree/master/service
    kapt(Dependency.autoService)
    compileOnly(Dependency.autoService)
}

configureDokka()
configurePublishing()

pluginBundle {
    website = VCS_URL // use GitHub URL so that the Gradle plugin page points to repo rather than just the NextFaze site
    vcsUrl = VCS_URL

    description = project.description
    tags = listOf("kotlin", "android", "developer", "kapt", "NextFaze", "tool", "debugging")

    (plugins) {
        "devfun-gradle-plugin" {
            id = "com.nextfaze.devfun"
            displayName = "DevFun Gradle Plugin"
        }
    }
}
