package com.nextfaze.devfun.gradle.plugin

import com.google.auto.service.AutoService
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.compile.AbstractCompile
import org.jetbrains.kotlin.gradle.plugin.KotlinGradleSubplugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption

@AutoService(KotlinGradleSubplugin::class)
class DevFunKotlinGradlePlugin : KotlinGradleSubplugin<AbstractCompile> {
    override fun getCompilerPluginId() = KotlinGradlePlugin.compilerPluginId
    override fun getPluginArtifact() = KotlinGradlePlugin.pluginArtifact

    override fun isApplicable(project: Project, task: AbstractCompile) = KotlinGradlePlugin.isApplicable(project, task)

    override fun apply(
        project: Project,
        kotlinCompile: AbstractCompile,
        javaCompile: AbstractCompile,
        variantData: Any?,
        androidProjectHandler: Any?,
        javaSourceSet: SourceSet?
    ): List<SubpluginOption> = emptyList()
}
