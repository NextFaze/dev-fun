import net.sourceforge.plantuml.FileFormat
import net.sourceforge.plantuml.FileFormatOption
import net.sourceforge.plantuml.SourceStringReader
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.Delete
import org.gradle.kotlin.dsl.*
import java.io.File

private val Project.plantUmlDir get() = "${rootDir.absolutePath}/gh-pages/assets/uml"

@Suppress("unused")
fun Project.configurePlantUml() {
    val deleteTask =
        task<Delete>("cleanPlantUml") {
            description = "Deletes rendered Plant UML diagrams."
            group = "documentation"
            setDelete(
                fileTree(
                    mapOf(
                        "dir" to plantUmlDir,
                        "exclude" to listOf("**/*.puml")
                    )
                )
            )
        }

    task<Task>("renderPlantUml") {
        description = "Render Plant UML diagrams."
        group = "documentation"
        dependsOn(deleteTask)

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
}
