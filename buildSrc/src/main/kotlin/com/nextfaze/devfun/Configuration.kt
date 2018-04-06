@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.nextfaze.devfun

import org.gradle.api.Project
import org.gradle.api.internal.project.ProjectScript

val Project.isSnapshot get() = getBooleanProperty("VERSION_SNAPSHOT", true)
val Project.versionName get() = getStringProperty("VERSION_NAME", "0.0.0")
val Project.deployVersion get() = if (project.isSnapshot) "${project.versionName}-SNAPSHOT" else project.versionName

object Library {
    @JvmStatic fun isSnapshot(project: Project) = project.isSnapshot
    @JvmStatic fun isSnapshot(project: ProjectScript) = project.scriptTarget.isSnapshot
    @JvmStatic fun deployVersion(project: Project) = project.deployVersion
    @JvmStatic fun deployVersion(project: ProjectScript) = project.scriptTarget.deployVersion

    @JvmStatic val getTagRef
        get() = Runtime.getRuntime().exec("git describe --tags")
            .apply { waitFor() }
            .run { inputStream.bufferedReader().readText() }
}

object Android {
    const val compileSdkVersion = 27
    const val buildToolsVersion = "27.0.3"

    const val minSdkVersion = 15
    const val targetSdkVersion = 27
    const val versionCode = 1

    @JvmStatic fun versionName(project: Project) = project.deployVersion
    @JvmStatic fun versionName(project: ProjectScript) = project.scriptTarget.deployVersion
}

object Config {
    const val bintrayPlugin = "com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.0"
    const val androidPlugin = "com.android.tools.build:gradle:3.1.0"
    const val androidMavenGradlePlugin = "com.github.dcendents:android-maven-gradle-plugin:2.1"

    const val kotlinVersion = "1.2.31"
    const val kotlinPlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
    const val kotlinPluginApi = "org.jetbrains.kotlin:kotlin-gradle-plugin-api:$kotlinVersion"
    const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jre7:$kotlinVersion"
    const val kotlinReflect = "org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion"

    const val kotlinCoroutinesVersion = "0.22.5"
    const val kotlinCoroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutinesVersion"
    const val kotlinCoroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$kotlinCoroutinesVersion"

    const val supportVersion = "27.1.1"
    const val supportRecyclerView = "com.android.support:recyclerview-v7:$supportVersion"
    const val supportAppCompat = "com.android.support:appcompat-v7:$supportVersion"
    const val supportDesign = "com.android.support:design:$supportVersion"
    const val supportAnnotations = "com.android.support:support-annotations:$supportVersion"
    const val supportConstraintLayout = "com.android.support.constraint:constraint-layout:1.0.2"

    const val daggerVersion = "2.15"
    const val daggerCompiler = "com.google.dagger:dagger-compiler:$daggerVersion"
    const val dagger = "com.google.dagger:dagger:$daggerVersion"
    const val daggerAnnotations = "org.glassfish:javax.annotation:10.0-b28"

    const val slf4jApi = "org.slf4j:slf4j-api:1.7.25"

    const val autoService = "com.google.auto.service:auto-service:1.0-rc4"

    const val stethoVersion = "1.5.0"
    const val stetho = "com.facebook.stetho:stetho:$stethoVersion"
    const val stethoJsRhino = "com.facebook.stetho:stetho-js-rhino:$stethoVersion"

    const val glide = "com.github.bumptech.glide:glide:4.6.1"

    const val leakCanary = "com.squareup.leakcanary:leakcanary-android:1.5.4"

    const val dokkaVersion = "0.9.16"
    const val dokkaPlugin = "org.jetbrains.dokka:dokka-gradle-plugin:$dokkaVersion"
    const val dokkaAndroidPlugin = "org.jetbrains.dokka:dokka-android-gradle-plugin:$dokkaVersion"
    const val dokkaFatJar = "org.jetbrains.dokka:dokka-fatjar:$dokkaVersion"
}

object Publishing {
    object Vcs {
        const val URL = "https://github.com/NextFaze/dev-fun.git"
    }

    object Organization {
        const val NAME = "NextFaze"
        const val URL = "https://www.nextfaze.com/"
    }
}

fun Project.getBooleanProperty(name: String, default: Boolean) =
    findProperty(name)?.toString()?.takeIf { it.isNotBlank() }?.toBoolean() ?: default

fun Project.getStringProperty(name: String, default: String) =
    findProperty(name)?.toString() ?: default
