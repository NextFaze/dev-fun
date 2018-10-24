import org.jetbrains.kotlin.samWithReceiver.gradle.SamWithReceiverExtension

buildscript {
    project.apply { from(rootProject.file("../versions.gradle.kts")) }

    repositories {
        mavenLocal()
        jcenter()
        maven { setUrl("https://dl.bintray.com/kotlin/kotlin-dev") }
        maven { setUrl("https://repo.gradle.org/gradle/libs-releases-local/") }
        maven { setUrl("https://plugins.gradle.org/m2/") }
        google()
    }
}

println("buildSrcKotlinVersion: ${extra["buildSrcKotlinVersion"]}")
println("buildSrc kotlin compiler version: ${org.jetbrains.kotlin.config.KotlinCompilerVersion.VERSION}")
println("buildSrc stdlib version: ${KotlinVersion.CURRENT}")

plugins {
    `kotlin-dsl`
}

repositories {
    mavenLocal()
    jcenter()
    maven { setUrl("https://dl.bintray.com/kotlin/kotlin-dev") }
    maven { setUrl("https://repo.gradle.org/gradle/libs-releases-local/") }
    google()
}

dependencies {
    // Kotlin
    val buildSrcKotlinVersion: String by extra
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$buildSrcKotlinVersion")

    // PlantUML
    implementation("net.sourceforge.plantuml:plantuml:8059")

    // Android Plugin
    val agpVersion: String by extra
    implementation("com.android.tools.build:gradle:$agpVersion")

    // Dokka
    val dokkaVersion: String by extra
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:$dokkaVersion")
    implementation("org.jetbrains.dokka:dokka-android-gradle-plugin:$dokkaVersion")

    // Bintray
    implementation("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.4")

    // ShadowJar
    implementation("com.github.jengelman.gradle.plugins:shadow:4.0.1")
}
