package com.nextfaze.devfun.gradle.plugin

import com.google.auto.service.AutoService
import org.gradle.api.Project
import org.gradle.api.tasks.compile.AbstractCompile
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinGradleSubplugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption

/*
This is this same 1.2.71 except that 1.3.0 has a new function `getNativeCompilerPluginArtifact` with a default implementation.
Thus we need only build against the 1.3.0 version.
 */
@AutoService(KotlinGradleSubplugin::class)
class DevFunKotlinGradlePlugin : KotlinGradleSubplugin<AbstractCompile> {
    override fun getCompilerPluginId() = KotlinGradlePlugin.compilerPluginId
    override fun getPluginArtifact() = KotlinGradlePlugin.pluginArtifact

    override fun isApplicable(project: Project, task: AbstractCompile) = KotlinGradlePlugin.isApplicable(project, task)

    override fun apply(
        project: Project,
        kotlinCompile: AbstractCompile,
        javaCompile: AbstractCompile?,
        variantData: Any?,
        androidProjectHandler: Any?,
        kotlinCompilation: KotlinCompilation?
    ): List<SubpluginOption> = emptyList()
}
