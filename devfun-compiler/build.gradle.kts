import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

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
    compile(Dependency.kotlinStdLib)
    compile(Dependency.kotlinReflect)
    compile("org.jetbrains.kotlinx:kotlinx-metadata-jvm:0.0.4")

    // Kotlin Poet
    shadow("com.github.alex2069:kotlinpoet:01d85d37883895b913e547ead8828dcd75889bf0") {
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

afterEvaluate {
    val code = """/** Generated by build script - do not modify directly. */
package com.nextfaze.devfun.compiler

internal const val versionName = "$version"
"""

    val versionKtFile = File("$projectDir/src/main/java/com/nextfaze/devfun/compiler/version.kt")
    if (versionKtFile.readText() != code) {
        versionKtFile.writeText(code)
        println("Compiler version.kt updated to:\n$code")
    }
}
