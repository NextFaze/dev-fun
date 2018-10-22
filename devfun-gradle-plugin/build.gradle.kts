import com.google.common.io.Files
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

val deployVersion = getStringProperty("DEVFUN_GRADLE_PLUGIN_DEPLOY_VERSION", "")
if (deployVersion.isNotBlank()) {
    version = versionName(deployVersion)
    println("Gradle plugin version set to: $version")
}

dependencies {
    // Core Gradle Plugin
    compileOnly(project(":devfun-gradle-plugin:gradle-plugin"))

    // Kotlin std-lib version specific plugins
    compileOnly(project(":devfun-gradle-plugin:kotlin-plugin-1251"))
    compileOnly(project(":devfun-gradle-plugin:kotlin-plugin-1261"))
    compileOnly(project(":devfun-gradle-plugin:kotlin-plugin-1271"))
}

configureDokka()
configurePublishing()

pluginBundle {
    website = VCS_URL // use GitHub URL so that the Gradle plugin page points to repo rather than just the NextFaze site
    vcsUrl = VCS_URL

    description = project.description
    tags = listOf("kotlin", "android", "developer", "kapt", "NextFaze", "tool", "debugging")

    (plugins) {
        create("devfun-gradle-plugin") {
            id = "com.nextfaze.devfun"
            displayName = "DevFun Gradle Plugin"
        }
    }
}

afterEvaluate {
    subprojects {
        task<Jar> {
            archiveName = "${project.name}.jar"
        }
    }
}

task<Jar> {
    project.subprojects.forEach {
        dependsOn(":${project.name}:${it.name}:jar")
    }

    doFirst {
        val jarTasks = project.subprojects.flatMap { it.tasks.withType(Jar::class.java) }

        val mainPluginTask = jarTasks.single { it.archiveName == "gradle-plugin.jar" }
        val versionTasks = jarTasks.filter { it.archiveName != "gradle-plugin.jar" }

        from(mainPluginTask.source)
        from(versionTasks)
    }
}
