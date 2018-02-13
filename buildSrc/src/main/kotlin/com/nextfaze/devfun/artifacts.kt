@file:Suppress("unused")

package com.nextfaze.devfun

import groovy.lang.Closure
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.UnknownDomainObjectException
import org.gradle.api.artifacts.ConfigurablePublishArtifact
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.ConfigurationContainer
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.the

fun Project.sourcesJar(sourceSet: String? = "main", body: Jar.() -> Unit = {}): Jar =
    getOrCreateTask("sourcesJar") {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        classifier = "sources"
        try {
            if (sourceSet != null) {
                project.pluginManager.withPlugin("java-base") {
                    from(project.the<JavaPluginConvention>().sourceSets[sourceSet].allSource)
                }
            }
        } catch (e: UnknownDomainObjectException) {
            // skip default sources location
        }
        body()
        project.addArtifact("archives", this, this)
    }

fun Project.configureDokka(): Any? = (rootProject.extra["configureDokka"] as Closure<*>).call(project)

fun ConfigurationContainer.getOrCreate(name: String): Configuration = findByName(name) ?: create(name)

fun <T> Project.addArtifact(configuration: Configuration, task: Task, artifactRef: T, body: ConfigurablePublishArtifact.() -> Unit = {}) {
    artifacts.add(configuration.name, artifactRef) {
        builtBy(task)
        body()
    }
}

fun <T> Project.addArtifact(configurationName: String, task: Task, artifactRef: T, body: ConfigurablePublishArtifact.() -> Unit = {}) =
    addArtifact(configurations.getOrCreate(configurationName), task, artifactRef, body)
