import com.jfrog.bintray.gradle.BintrayExtension
import com.jfrog.bintray.gradle.tasks.BintrayUploadTask
import groovy.util.Node
import groovy.util.NodeList
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.*
import java.util.Date

private const val PUBLICATION_NAME = "mavenJava"
const val VCS_URL = "https://github.com/NextFaze/dev-fun.git"

@Suppress("unused")
fun Project.configurePublishing() {
    sourcesJar()
    configureMavenPublishing()
    configureBintray()
}

fun Project.configureMavenPublishing() {
    apply { plugin("maven-publish") }

    publishing {
        val repoUrl = if (isSnapshot) getStringProperty("DEVFUN_PUBLISH_URL_SNAPSHOT", "") else getStringProperty("DEVFUN_PUBLISH_URL", "")
        if (repoUrl.isNotBlank()) {
            repositories {
                maven {
                    setUrl(repoUrl)
                    credentials {
                        username = getStringProperty("DEVFUN_PUBLISH_USER", "")
                        password = getStringProperty("DEVFUN_PUBLISH_PASSWORD", "")
                    }
                }
            }
        }

        publications {
            create(PUBLICATION_NAME, MavenPublication::class.java) {
                if (isAndroid) {
                    afterEvaluate {
                        artifact(tasks["bundleReleaseAar"])
                    }
                } else {
                    val shadowJarTask = tasks.findByName("shadowJar")
                    if (shadowJarTask != null) {
                        artifact(shadowJarTask)
                    } else {
                        from(components["java"])
                    }
                }

                artifact(tasks["sourcesJar"])
                artifact(tasks["javadocJar"])

                pom {
                    name.set("${project.group}:${project.name}")
                    url.set(getStringProperty("POM_URL", ""))
                    description.set(project.description)

                    organization {
                        name.set("NextFaze")
                        url.set("https://www.nextfaze.com/")
                    }

                    licenses {
                        license {
                            name.set("The Apache Software License, Version 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                            distribution.set("repo")
                        }
                    }

                    scm {
                        connection.set("scm:git:git://github.com/NextFaze/dev-fun.git")
                        developerConnection.set("scm:git:ssh://github.com:NextFaze/dev-fun.git")
                        url.set("http://github.com/NextFaze/dev-fun/tree/master")
                        tag.set(gitTagRef)
                    }

                    issueManagement {
                        system.set("GitHub Issues")
                        url.set(pomIssuesUrl)
                    }

                    developers {
                        developer {
                            id.set("alex2069")
                            name.set("Alex Waters")
                            email.set("awaters@nextfaze.com")
                            organization.set("NextFaze")
                            organizationUrl.set("https://www.nextfaze.com")
                        }
                    }

                    withXml {
                        val node = asNode()

                        // remove original if it exists (non-Android projects)
                        (node.get("dependencies") as NodeList).forEach { node.remove(it as Node) }

                        val dependenciesNode = node.appendNode("dependencies")
                        configurations.forEach { config ->
                            // only consider apiElements and runtimeElements configurations
                            if (!config.name.endsWith("Elements")) return@forEach

                            config.allDependencies.forEach { dep ->
                                if (dep.group != null && dep.version != null && dep.name != "unspecified") {
                                    dependenciesNode.appendNode("dependency").apply {
                                        appendNode("groupId", dep.group)
                                        appendNode("artifactId", dep.name)
                                        appendNode("version", dep.version)
                                        appendNode("scope", config.name)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun Project.configureBintray() {
    apply { plugin("com.jfrog.bintray") }

    val bintrayUser = getStringProperty("DEVFUN_BINTRAY_USER", "")
    val bintrayKey = getStringProperty("DEVFUN_BINTRAY_KEY", "")
    val pomUrl = getStringProperty("POM_URL", "")
    val mavenUser = getStringProperty("DEVFUN_MAVEN_USER", "")
    val mavenPassword = getStringProperty("DEVFUN_MAVEN_USER", "")

    bintray {
        dryRun = getBooleanProperty("DEVFUN_BINTRAY_DRY_RUN", true)
        publish = true
        override = true

        user = bintrayUser
        key = bintrayKey
        setPublications(PUBLICATION_NAME)

        pkg {
            repo = "dev-fun"
            name = project.name
            userOrg = "nextfaze"
            desc = project.description

            websiteUrl = pomUrl
            issueTrackerUrl = pomIssuesUrl
            vcsUrl = VCS_URL

            setLicenses("Apache-2.0")

            githubRepo = "NextFaze/dev-fun"
            githubReleaseNotesFile = "README.md"

            version {
                name = project.versionName
                desc = project.description
                released = Date().toString()
                vcsTag = gitTagRef

                gpg {
                    sign = true
                }

                mavenCentralSync {
                    sync = false
                    user = mavenUser
                    password = mavenPassword
                }
            }
        }
    }

    task<BintrayUploadTask> {
        dependsOn("javadocJar", "sourcesJar", "generatePomFileForMavenJavaPublication")
    }
}

private fun Project.publishing(configure: PublishingExtension.() -> Unit): Unit = extensions.configure("publishing", configure)

private fun Project.bintray(configure: BintrayExtension.() -> Unit): Unit = extensions.configure("bintray", configure)

private fun BintrayExtension.pkg(body: BintrayExtension.PackageConfig.() -> Unit) {
    pkg.apply(body)
}

private fun BintrayExtension.PackageConfig.version(body: BintrayExtension.VersionConfig.() -> Unit) {
    version.apply(body)
}

private fun BintrayExtension.VersionConfig.gpg(body: BintrayExtension.GpgConfig.() -> Unit) {
    gpg.apply(body)
}

private fun BintrayExtension.VersionConfig.mavenCentralSync(body: BintrayExtension.MavenCentralSyncConfig.() -> Unit) {
    mavenCentralSync.apply(body)
}

private val gitTagRef
    get() = Runtime.getRuntime().exec("git describe --tags")
        .apply { waitFor() }
        .run { inputStream.bufferedReader().readText() }
        .trim()

private val Project.pomIssuesUrl get() = getStringProperty("POM_ISSUES_URL", "")
