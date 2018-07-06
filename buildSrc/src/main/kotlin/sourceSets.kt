import org.gradle.api.Project
import org.gradle.kotlin.dsl.*
import java.io.File

val Project.mainSourceSet
    get() = getSourceSet("main")

fun Project.getSourceSet(sourceSet: String = "main"): Iterable<File> =
    when {
        isAndroid -> androidExtension().sourceSets[sourceSet].java.srcDirs
        else -> javaPluginConvention().sourceSets[sourceSet].allSource
    }
