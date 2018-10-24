import com.google.common.io.Files
import groovy.lang.Closure
import org.gradle.kotlin.dsl.get
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
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
    compileOnly(project(":devfun-gradle-plugin:kotlin-plugin-1300"))
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
        // Previous versions of Kotlin are not compatible with latest daemon, so we force use of compiler-embeddable (in-process).
        // It can work without forcing it, but it will spit out a lot of warnings.
        task<KotlinCompile> {
            var prevValue: String? = null
            doFirst {
                prevValue = System.getProperty("kotlin.compiler.execution.strategy")
                System.setProperty("kotlin.compiler.execution.strategy", "in-process")
            }
            doLast {
                if (prevValue != null) {
                    System.setProperty("kotlin.compiler.execution.strategy", prevValue)
                }
            }
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
