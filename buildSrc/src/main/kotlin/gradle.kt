import com.android.build.gradle.BaseExtension
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.kotlin.dsl.*

fun Project.getBooleanProperty(name: String, default: Boolean) =
    findProperty(name)?.toString()?.takeIf { it.isNotBlank() }?.toBoolean() ?: default

fun Project.getStringProperty(name: String, default: String) =
    findProperty(name)?.toString() ?: default

inline fun <reified T : Task> Project.task(crossinline body: T.() -> Unit) {
    tasks.withType(T::class.java) {
        body(this)
    }
}

fun Project.androidExtension(): BaseExtension = the()
fun Project.javaPluginConvention(): JavaPluginConvention = the()
