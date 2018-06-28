import com.nextfaze.devfun.*
import java.io.FileNotFoundException
import org.jetbrains.dokka.DokkaConfiguration
import org.jetbrains.dokka.gradle.DokkaAndroidTask
import org.jetbrains.dokka.gradle.DokkaPlugin
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.dokka.gradle.LinkMapping

plugins {
    id("com.android.library")
    kotlin("android")
    id("org.jetbrains.dokka-android")
}

registerCleanPlantUmlTask()
registerRenderPlantUmlTask()

android {
    compileSdkVersion(Android.compileSdkVersion)
    buildToolsVersion = Android.buildToolsVersion

    defaultConfig {
        minSdkVersion(Android.minSdkVersion)
        targetSdkVersion(Android.targetSdkVersion)
        versionCode = Android.versionCode
        versionName = Android.versionName(project)

        consumerProguardFile("../proguard-rules-common.pro")
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"

        javaCompileOptions {
            annotationProcessorOptions {
                // We're using KAPT so ignore annotationProcessor configuration dependencies
                includeCompileClasspath = false
                if (project.isSnapshot) {
                    argument("devfun.debug.verbose", "true")
                }
            }
        }
    }

    resourcePrefix("df_${project.name.replace("devfun-", "")}_".replace('-', '_'))
}

dependencies {
    // Kotlin
    implementation(Config.kotlinStdLib)

    // Support libs
    implementation(Config.supportAppCompat)

    // Dagger
    implementation(Config.dagger)

    // DevFun
    implementation(project(":devfun"))
    implementation(project(":devfun-annotations"))
    implementation(project(":devfun-compiler"))
    implementation(project(":devfun-httpd"))
    implementation(project(":devfun-httpd-frontend"))
    implementation(project(":devfun-inject-dagger2"))
    implementation(project(":devfun-internal"))
    implementation(project(":devfun-invoke-view-colorpicker"))
    implementation(project(":devfun-menu"))
    implementation(project(":devfun-stetho"))
    implementation(project(":devfun-util-glide"))
    implementation(project(":devfun-util-leakcanary"))
//    compile fileTree(new File("${rootProject.project(':demo').buildDir}/libs/jarForDokka.jar"))
}

getOrCreateTask<DokkaAndroidTask>("dokka") {
    if (file("Module.md").exists()) {
        includes = listOf("Module.md")
    }

    moduleName = "gh-pages"
    outputFormat = "gfm"
    outputDirectory = "."
    dokkaFatJar = Config.dokkaFatJar
}

project.afterEvaluate {
    // have main dokka task depend on all module Dokka tasks and have them depend on their respective assemble task
    val mainDokkaTask = project.getTasksByName("dokka", false).first() as DokkaAndroidTask
    rootProject.getTasksByName("dokka", true).forEach { libDokkaTask ->
        if (libDokkaTask.project == rootProject) return@forEach

        mainDokkaTask.sourceDirs +=
                when (libDokkaTask) {
                    is DokkaAndroidTask -> libDokkaTask.project.android.sourceSets["main"].java.srcDirs
                    is DokkaTask -> libDokkaTask.project.java.sourceSets["main"].allSource
                    else -> throw RuntimeException("Unexpected type ${libDokkaTask::class} for libDokkaTask $libDokkaTask")
                }

        libDokkaTask.dependsOn("assemble")

        mainDokkaTask.linkMappings.add(
            LinkMapping().apply {
                dir = "$rootDir/${libDokkaTask.project.name}/src/main/java"
                url = "https://github.com/NextFaze/dev-fun/tree/master/${libDokkaTask.project.name}/src/main/java"
                suffix = "#L"
            }
        )

        if (libDokkaTask != mainDokkaTask) {
            mainDokkaTask.dependsOn(libDokkaTask)
        }
    }

    // cLear previously generated Dokka files -  we need to do this as Dokka doesn't remove old files
    val dokkaOutputDir = "${rootDir.absolutePath}/gh-pages"
    mainDokkaTask.dependsOn(
        getOrCreateTask<Delete>("cleanDokka") {
            setDelete(
                fileTree(
                    mapOf(
                        "dir" to dokkaOutputDir,
                        "excludes" to listOf(".git", "assets", "_config.yml", "README.md")
                    )
                )
            )
        }
    )

    // update Components table of contents (doctoc)
    project.getTasksByName("assemble", false).first().dependsOn(
        getOrCreateTask("doctocForWiki") {
            doLast {
                try {
                    println("Running doctoc...")
                    // need to run from script to source nvm env. for non-interactive shells (i.e. when run from IDE)
                    // suggestions welcome for how to do this without the need for external .sh file
                    // (and no, you can't simply ". ~/.nvm/nvm.sh && doctoc ..." from JVM - not sure why)
                    "bash".execute("dokka/run_doctoc.sh")
                    println("Doctoc complete!")
                } catch (t: Throwable) {
                    println("Do you need to install doctoc?")
                    println("    npm install -g doctoc")
                    throw t
                }

                println("Adjust generated doctoc for Dokka...")
                "sed".execute(
                    "/<!-- START doctoc generated TOC/,/<!-- END doctoc generated TOC/s/^( *)/ * \\1\\1/",
                    "-ri",
                    "dokka/src/main/java/wiki/Components.kt"
                )
                println("Adjustment complete!")
            }
        }
    )

    // Dokka doesn't have HTML support (it strips all HTML tags)
    // Thus to include image elements we wrap them in a code block and after Dokka we strip the code black elements
    mainDokkaTask.finalizedBy(
        getOrCreateTask("dokkaImageHack") {
            doLast {
                file(dokkaOutputDir).walkTopDown()
                    .filter { it.extension == "md" }
                    .forEach {
                        it.writeText(
                            it.readText()
                                .replace("`IMG_START", "")
                                .replace("IMG_END`", "")
                        )
                    }
            }
        }
    )
}

fun String.execute(vararg args: String) {
    val process = ProcessBuilder(listOf(this) + args)
        .directory(rootDir)
        .redirectOutput(ProcessBuilder.Redirect.PIPE)
        .redirectError(ProcessBuilder.Redirect.PIPE)
        .start()

    process.waitFor(1, TimeUnit.MINUTES)
    System.out.print(process.inputStream.bufferedReader().readText())

    process.errorStream.bufferedReader().readText().let {
        if (it.isNotBlank()) {
            System.err.print(it)
            throw RuntimeException("Command execution error: $this")
        }
    }
}
