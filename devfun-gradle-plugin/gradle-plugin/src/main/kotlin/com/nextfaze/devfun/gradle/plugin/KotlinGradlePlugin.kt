package com.nextfaze.devfun.gradle.plugin

import com.android.build.gradle.BaseExtension
import com.nextfaze.devfun.compiler.APPLICATION_PACKAGE
import com.nextfaze.devfun.compiler.APPLICATION_VARIANT
import com.nextfaze.devfun.compiler.EXT_PACKAGE_OVERRIDE
import com.nextfaze.devfun.compiler.EXT_PACKAGE_ROOT
import com.nextfaze.devfun.compiler.EXT_PACKAGE_SUFFIX
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.tasks.compile.AbstractCompile
import org.jetbrains.kotlin.gradle.plugin.KaptExtension
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

private const val DEVFUN_GROUP_NAME = "com.nextfaze.devfun"
private const val DEVFUN_ARTIFACT_NAME = "devfun-gradle-plugin"
private const val DEVFUN_VERSION_NAME = versionName

/**
 * The DevFun Kotlin Gradle plugin. Configures the KAPT options.
 *
 * Attempts to automatically determine the application package and build variant.
 * Also passes though the script configuration options.
 *
 * @see DevFunGradlePlugin
 * @see DevFunExtension
 */
object KotlinGradlePlugin {
    /** The Gradle sub-plugin compiler plugin ID `com.nextfaze.devfun`. */
    val compilerPluginId get() = DEVFUN_GROUP_NAME

    val groupName get() = DEVFUN_GROUP_NAME
    val artifactName get() = DEVFUN_ARTIFACT_NAME

    /** The Gradle sub-plugin artifact details. */
    val pluginArtifact get() = SubpluginArtifact(DEVFUN_GROUP_NAME, DEVFUN_ARTIFACT_NAME, DEVFUN_VERSION_NAME)

    /**
     * Determine if this plugin can be applied to this [project] and compile [task].
     *
     * For some reason the [apply] call never receives the first variant so most of the logic is performed in here instead.
     */
    fun isApplicable(project: Project, task: AbstractCompile): Boolean {
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
}

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
