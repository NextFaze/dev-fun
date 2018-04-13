package com.nextfaze.devfun

import net.sourceforge.plantuml.FileFormat
import net.sourceforge.plantuml.FileFormatOption
import net.sourceforge.plantuml.SourceStringReader
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.Delete
import java.io.File

private val Project.plantUmlDir get() = "${rootDir.absolutePath}/gh-pages/assets/uml"

fun Project.registerCleanPlantUmlTask() =
    getOrCreateTask<Delete>("cleanPlantUml") {
        setDelete(
            fileTree(
                mapOf(
                    "dir" to plantUmlDir,
                    "exclude" to listOf("**/*.puml")
                )
            )
        )
    }

fun Project.registerRenderPlantUmlTask() =
    getOrCreateTask<Task>("renderPlantUml") {
        description = "Render Plant UML diagrams."
        group = "documentation"
        dependsOn("cleanPlantUml")

        doLast {
            fileTree(plantUmlDir).filter { it.extension == "puml" }.forEach {
                println("Rendering ${it.name}...")
                val reader = SourceStringReader(it.readText())
                val format = FileFormatOption(FileFormat.PNG, true)
                reader.generateImage(File(it.parentFile, "${it.nameWithoutExtension}.png").outputStream(), format)
                reader.getCMapData(0, format).also { cmap ->
                    if (cmap != null) {
                        File(it.parentFile, "${it.nameWithoutExtension}.cmapx").writeText(cmap)
                    }
                }
            }
        }
    }
