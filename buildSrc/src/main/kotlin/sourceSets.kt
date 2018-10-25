import org.gradle.api.Project
import org.gradle.kotlin.dsl.*
import java.io.File

val Project.mainSourceFiles
    get() = getSourceFiles("main")

fun Project.getSourceFiles(sourceSet: String = "main"): Iterable<File> =
    when {
        isAndroid -> androidExtension().sourceSets[sourceSet].java.srcDirs
        else -> javaPluginConvention().sourceSets[sourceSet].allSource
    }

fun Project.mainSrcDirs(vararg srcDirs: Any) {
    when {
        isAndroid -> androidExtension().sourceSets["main"].java.srcDirs(*srcDirs)
        else -> javaPluginConvention().sourceSets["main"].java { srcDirs(*srcDirs) }
    }
}
