import org.gradle.api.Project
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.*
import org.jetbrains.dokka.DokkaConfiguration
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.dokka.gradle.LinkMapping
import java.net.URL

@Suppress("unused")
fun Project.configureDokka() {
    when {
        isAndroid -> apply { plugin("org.jetbrains.dokka-android") }
        else -> apply { plugin("org.jetbrains.dokka") }
    }

    task<DokkaTask> {
        externalDocumentationLink {
            url = URL("https://nextfaze.github.io/dev-fun/")
        }
        dokkaFatJar = Dependency.dokkaFatJar
    }

    val dokkaJavadoc = task<DokkaTask>("dokkaJavadoc") {
        includes = listOf("Module.md")
        linkMapping {
            dir = "src/main/java"
            url = "https://github.com/NextFaze/dev-fun/tree/master/${project.name}/src/main/java"
            suffix = "#L"
        }
        outputFormat = "javadoc"
        outputDirectory = "$buildDir/dokkaJavadoc"
        externalDocumentationLink {
            url = URL("https://nextfaze.github.io/dev-fun/")
        }
        dokkaFatJar = Dependency.dokkaFatJar
        sourceDirs = mainSourceSet
    }

    task<Jar>("javadocJar") {
        group = "publishing"
        classifier = "javadoc"
        dependsOn(dokkaJavadoc)
        from(dokkaJavadoc.outputDirectory)
        addArtifact("archives", this, this)
    }
}

private fun DokkaTask.externalDocumentationLink(body: DokkaConfiguration.ExternalDocumentationLink.Builder.() -> Unit) =
    externalDocumentationLinks.add(DokkaConfiguration.ExternalDocumentationLink.Builder().apply(body).build())

private fun DokkaTask.linkMapping(body: LinkMapping.() -> Unit) = linkMappings.add(LinkMapping().apply(body))
