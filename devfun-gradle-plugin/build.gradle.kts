import com.nextfaze.devfun.*
import groovy.lang.Closure
import org.gradle.kotlin.dsl.get
import kotlin.reflect.KCallable
import kotlin.reflect.KFunction

plugins {
    id("com.gradle.plugin-publish") version "0.9.10"
}

apply {
    plugin("kotlin")
    plugin("kotlin-kapt")
    plugin("maven-publish")
}

description = "Gradle plugin that facilitates DevFun annotation processor configuration."

dependencies {
    compileOnly(project(":devfun-compiler"))

    // Gradle
    compileOnly(gradleApi())

    // Kotlin
    compileOnly(Config.kotlinStdLib)
    compileOnly(Config.kotlinPlugin)
    compileOnly(Config.kotlinPluginApi)

    // Android
    compileOnly(Config.androidPlugin) {
        exclude(group = "org.jetbrains.kotlin")
    }

    // Google AutoService - https://github.com/google/auto/tree/master/service
    kapt_(Config.autoService)
    compileOnly(Config.autoService)
}

sourcesJar()
configureDokka()

configure<PublishingExtension> {
    publications {
        create("mavenJava", MavenPublication::class.java) {
            from(components["java"])
            artifact(tasks["sourcesJar"]) {
                classifier = "sources"
            }
        }
    }
}

pluginBundle {
    website = Publishing.Vcs.URL // use GitHub URL so that the Gradle plugin page points to repo rather than just the NextFaze site
    vcsUrl = Publishing.Vcs.URL

    description = project.description
    tags = listOf("kotlin", "android", "developer", "kapt", "NextFaze", "tool", "debugging")

    (plugins) {
        "devfun-gradle-plugin" {
            id = "com.nextfaze.devfun"
            displayName = "DevFun Gradle Plugin"
        }
    }
}
