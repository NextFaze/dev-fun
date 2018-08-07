package com.nextfaze.devfun.gradle.plugin

import com.android.build.gradle.BaseExtension
import com.google.auto.service.AutoService
import com.nextfaze.devfun.compiler.*
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.compile.AbstractCompile
import org.jetbrains.kotlin.gradle.internal.KaptVariantData
import org.jetbrains.kotlin.gradle.plugin.KaptExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinGradleSubplugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

/**
 * The DevFun Gradle plugin. Allows use of the script configuration DSL.
 *
 * @see DevFunExtension
 */
class DevFunGradlePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.extensions.create("devFun", DevFunExtension::class.java)
    }
}

/**
 * The DevFun Kotlin Gradle plugin. Configures the KAPT options.
 *
 * Attempts to automatically determine the application package and build variant.
 * Also passes though the script configuration options.
 *
 * @see DevFunExtension
 */
@AutoService(KotlinGradleSubplugin::class)
class DevFunKotlinGradlePlugin : KotlinGradleSubplugin<AbstractCompile> {
    override fun getCompilerPluginId() = DEVFUN_GROUP_NAME
    override fun getPluginArtifact() = SubpluginArtifact(DEVFUN_GROUP_NAME, DEVFUN_ARTIFACT_NAME, DEVFUN_VERSION_NAME)

    override fun isApplicable(project: Project, task: AbstractCompile): Boolean {
        if (project.plugins.findPlugin(DevFunGradlePlugin::class.java) == null) return false
        if (task !is KotlinCompile) return false // we generate Kotlin code so definitely need this

        val androidExt = project.extensions.android ?: run {
            project.logger.error("Failed to find Android BaseExtension. Please ensure the devfun plugin is declared *after* the android plugin.")
            return false
        }
        val kotlinExt = project.extensions.kapt ?: run {
            project.logger.error("Failed to find KaptExtension. Please ensure the devfun plugin is declared *after* the kotlin-kapt plugin.")
            return false
        }
        val devFunExt = project.extensions.devFun ?: run {
            project.logger.error("Failed to find DevFunExtension. Please ensure the devfun plugin has been applied.")
            return false
        }

        val appPackage = getApplicationPackage(project, androidExt) ?: run {
            project.logger.warn("Unable to determine application package. Using DevFun package $DEVFUN_GROUP_NAME")
            DEVFUN_GROUP_NAME
        }
        val variant = run {
            val taskName = task.name
            kotlinCompileTaskNameRegex.find(taskName)?.groupValues?.getOrNull(1)?.decapitalize() ?: run {
                project.logger.warn("Failed to identify variant name from task name. Using 'main'")
                "main"
            }
        }

        kotlinExt.arguments {
            fun nonNullArg(name: String, value: String?) = value?.let { arg(name, it) }

            nonNullArg(APPLICATION_PACKAGE, appPackage)
            nonNullArg(APPLICATION_VARIANT, variant)

            nonNullArg(EXT_PACKAGE_SUFFIX, devFunExt.packageSuffix)
            nonNullArg(EXT_PACKAGE_ROOT, devFunExt.packageRoot)
            nonNullArg(EXT_PACKAGE_OVERRIDE, devFunExt.packageOverride)
        }

        return true
    }

    override fun apply(
        project: Project,
        kotlinCompile: AbstractCompile,
        javaCompile: AbstractCompile,
        variantData: Any?,
        androidProjectHandler: Any?,
        javaSourceSet: SourceSet?
    ): List<SubpluginOption> {
        //
        // TODO? Currently the kotlinExt value is not invoked for the first variant for some reason (which is typically the debug variant)
        //
        // Thus we're doing all our work in the isApplicable block instead (which is not ideal since we have to guess
        // the name from the task name rather than from the KaptVariantData object).
        //
        if (true) return emptyList()

        val androidExt = project.extensions.android ?: run {
            project.logger.error("Failed to find Android BaseExtension. Please ensure the devfun plugin is declared *after* the android plugin.")
            return emptyList()
        }
        val kotlinExt = project.extensions.kapt ?: run {
            project.logger.error("Failed to find KaptExtension. Please ensure the devfun plugin is declared *after* the kotlin-kapt plugin.")
            return emptyList()
        }
        val devFunExt = project.extensions.devFun ?: run {
            project.logger.error("Failed to find DevFunExtension. Please ensure the devfun plugin has been applied.")
            return emptyList()
        }

        val appPackage = getApplicationPackage(project, androidExt) ?: run {
            project.logger.warn("Unable to determine application package. Using DevFun package $DEVFUN_GROUP_NAME")
            DEVFUN_GROUP_NAME
        }
        val variant = if (variantData is KaptVariantData<*>) {
            variantData.name
        } else {
            val taskName = kotlinCompile.name
            project.logger.warn(
                """Variant data was not KaptVariantData<*> (variantData=$variantData).
                |Guessing variant name from kotlin compile $kotlinCompile""".trimMargin()
            )

            kotlinCompileTaskNameRegex.find(taskName)?.groupValues?.getOrNull(1)?.decapitalize() ?: run {
                project.logger.warn("Failed to identify variant name from task name. Using 'main'")
                "main"
            }
        }

        kotlinExt.arguments {
            fun nonNullArg(name: String, value: String?) = value?.let { arg(name, it) }

            nonNullArg(APPLICATION_PACKAGE, appPackage)
            nonNullArg(APPLICATION_VARIANT, variant)

            nonNullArg(EXT_PACKAGE_SUFFIX, devFunExt.packageSuffix)
            nonNullArg(EXT_PACKAGE_ROOT, devFunExt.packageRoot)
            nonNullArg(EXT_PACKAGE_OVERRIDE, devFunExt.packageOverride)
        }

        return emptyList()
    }
}

private const val DEVFUN_GROUP_NAME = "com.nextfaze.devfun"
private const val DEVFUN_ARTIFACT_NAME = "devfun-gradle-plugin"

// TODO this is just annoying - package with JAR or something so we can set at build time from build state and get it dynamically at run time
private const val DEVFUN_VERSION_NAME = "1.3.0-SNAPSHOT"

private val ExtensionContainer.android get() = findByType(BaseExtension::class.java)
private val ExtensionContainer.kapt get() = findByType(KaptExtension::class.java)
private val ExtensionContainer.devFun get() = findByType(DevFunExtension::class.java)

private val kotlinCompileTaskNameRegex = Regex("compile(.*)Kotlin")

private fun getApplicationPackage(project: Project, android: BaseExtension): String? {
    fun File.parseXml() = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(this)

    val manifestFile = android.mainSourceSet.manifest.srcFile
    return try {
        manifestFile.parseXml().documentElement.getAttribute("package")
            .also {
                if (it.isNullOrBlank()) {
                    project.logger.warn("Application package name is not present in the manifest file (${manifestFile.absolutePath})")
                }
            }
    } catch (t: Throwable) {
        project.logger.warn("Failed to parse manifest file (${manifestFile.absolutePath})", t)
        null
    }
}

private val BaseExtension.mainSourceSet get() = sourceSets.getByName("main")
