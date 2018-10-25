import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.AbstractKotlinCompile

plugins {
    id("kotlin")
    kotlin("kapt")
    id("com.github.johnrengelman.shadow")
}

description = """Annotation processor that handles @DeveloperFunction and @DeveloperCategory annotations.
    |
    |This should be applied to your non-main kapt configuration 'kaptDebug' to avoid running/using it on release builds.""".trimMargin()

kapt {
    correctErrorTypes = true
}

task<ShadowJar> {
    configurations = listOf(project.configurations.shadow)
    relocate("com.squareup.kotlinpoet", "com.squareup.kotlinpoet.devfun")
    classifier = ""
}

tasks.findByName("jar")!!.apply {
    enabled = false
    dependsOn("shadowJar")
}

dependencies {
    // DevFun
    compile(project(":devfun-annotations"))

    // Kotlin
    compile(Dependency.kotlin.stdLib)
    compile(Dependency.kotlin.reflect)
    compile("org.jetbrains.kotlinx:kotlinx-metadata-jvm:0.0.4")

    // Kotlin Poet
    shadow("com.github.alex2069:kotlinpoet:eb0d0a9426ea1b2e3273fb4c536e35b2960d9fa6") {
        isTransitive = false // just Kotlin libs we already declare
    }

    // Dagger
    kapt(Dependency.daggerCompiler)
    compile(Dependency.dagger)
    compileOnly(Dependency.daggerAnnotations)

    // Google AutoService - https://github.com/google/auto/tree/master/service
    kapt(Dependency.autoService)
    compileOnly(Dependency.autoService) {
        isTransitive = false
    }
}

configureDokka()
configurePublishing()
createVersionFile()
