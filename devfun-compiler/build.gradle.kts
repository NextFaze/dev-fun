import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("kotlin")
    kotlin("kapt")
    id("com.github.johnrengelman.shadow")
}

description = """Annotation processor that handles @DeveloperFunction and @DeveloperCategory annotations.
    |
    |This should be applied to your non-main kapt configuration 'kaptDebug' to avoid running/using it on release builds.""".trimMargin()

task<ShadowJar> {
    classifier = ""
    configurations = listOf(project.configurations.shadow.get())
    relocate("com.squareup.kotlinpoet", "com.squareup.kotlinpoet.devfun")
    relocate("kotlinx.metadata", "kotlinx.metadata.devfun")
    mergeServiceFiles()
}

tasks.findByName("jar")!!.apply {
    enabled = false
    dependsOn("shadowJar")
}

dependencies {
    // DevFun
    implementation(project(":devfun-annotations"))

    // Kotlin
    implementation(Dependency.kotlin.stdLib)
    implementation(Dependency.kotlin.reflect)
    shadow("org.jetbrains.kotlinx:kotlinx-metadata-jvm:0.0.5.1") {
        isTransitive = false // just Kotlin libs we already declare
    }

    // Kotlin Poet
    shadow("com.github.alex2069:kotlinpoet:eb0d0a9426ea1b2e3273fb4c536e35b2960d9fa6") {
        isTransitive = false // just Kotlin libs we already declare
    }

    // Dagger
    kapt(Dependency.daggerCompiler)
    implementation(Dependency.dagger)
    compileOnly(Dependency.daggerAnnotations)

    // JavaX Inject
    implementation(Dependency.javaxInject)

    // Google AutoService - https://github.com/google/auto/tree/master/service
    kapt(Dependency.autoService)
    compileOnly(Dependency.autoServiceAnnotations)
}

configureDokka()
configurePublishing()
createVersionFile()
