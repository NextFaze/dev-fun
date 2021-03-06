import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.artifacts.ConfigurablePublishArtifact
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.ConfigurationContainer
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.*

fun Project.sourcesJar(): Jar =
    task<Jar>("sourcesJar") {
        group = "publishing"
        setDuplicatesStrategy(DuplicatesStrategy.EXCLUDE)
        classifier = "sources"
        from(mainSourceFiles)
        addArtifact("archives", this, this)
    }

fun ConfigurationContainer.getOrCreate(name: String): Configuration = findByName(name) ?: create(name)

fun <T: Any> Project.addArtifact(configuration: Configuration, task: Task, artifactRef: T, body: ConfigurablePublishArtifact.() -> Unit = {}) {
    artifacts.add(configuration.name, artifactRef) {
        builtBy(task)
        body()
    }
}

fun <T: Any> Project.addArtifact(configurationName: String, task: Task, artifactRef: T, body: ConfigurablePublishArtifact.() -> Unit = {}) =
    addArtifact(configurations.getOrCreate(configurationName), task, artifactRef, body)
