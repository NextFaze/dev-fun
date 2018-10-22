package com.nextfaze.devfun.gradle.plugin

import com.nextfaze.devfun.compiler.DevFunProcessor
import com.nextfaze.devfun.compiler.PACKAGE_OVERRIDE
import com.nextfaze.devfun.compiler.PACKAGE_ROOT
import com.nextfaze.devfun.compiler.PACKAGE_SUFFIX
import com.nextfaze.devfun.compiler.PACKAGE_SUFFIX_DEFAULT
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.internal.classloader.ClassLoaderHierarchy
import org.gradle.internal.classloader.ClassLoaderVisitor
import org.jetbrains.kotlin.config.KotlinCompilerVersion
import org.jetbrains.kotlin.gradle.plugin.KotlinGradleSubplugin
import java.io.File
import java.net.URL
import java.net.URLClassLoader

/**
 * Gradle DSL for configuring DevFun.
 *
 * Values provided via Gradle plugin config can/will be overridden by values provided via. APT options.
 *
 * @see DevFunProcessor
 * @see PACKAGE_SUFFIX
 * @see PACKAGE_ROOT
 * @see PACKAGE_OVERRIDE
 */
open class DevFunExtension {
    /**
     * Sets the package suffix for the generated code. _(default: `devfun_generated`)_
     *
     * This is primarily for testing purposes to allow multiple generations in the same classpath.
     * - If this is null (unset) [PACKAGE_SUFFIX_DEFAULT] will be used.
     * - If this is empty the suffix will be omitted.
     *
     * Final output package will be: [packageRoot].`<variant?>`.[packageSuffix]
     *
     * `<variant?>` will be omitted if both `packageRoot` and `packageSuffix` are provided.
     */
    var packageSuffix: String? = null

    /**
     * Sets the package root for the generated code. _(default: `<application package>`)_
     *
     * Attempts will be made to auto-detect the project package by using the class output directory and known/standard
     * relative paths to various build files, but if necessary this option can be set instead.
     *
     * Final output package will be: [packageRoot].`<variant?>`.[packageSuffix]
     *
     * `<variant?>` will be omitted if both `packageRoot` and `packageSuffix` are provided.
     */
    var packageRoot: String? = null

    /**
     * Sets the package for the generated code. _(default: `<none>`)_
     *
     * This will override [packageRoot] and [packageSuffix].
     */
    var packageOverride: String? = null
}

/**
 * The DevFun Gradle plugin. Allows use of the script configuration DSL.
 *
 * @see DevFunExtension
 */
class DevFunGradlePlugin : Plugin<Project> {
    private val addUrlMethod = URLClassLoader::class.java.getDeclaredMethod("addURL", URL::class.java).apply { isAccessible = true }

    /** Apply this gradle plugin to the [project] registering [DevFunExtension] as `devFun`. */
    override fun apply(project: Project) {
        project.extensions.create("devFun", DevFunExtension::class.java)

        try {
            addKotlinPluginToClassPath(project)
        } catch (t: Throwable) {
            project.logger.warn(
                """Failed to inject KotlinGradleSubplugin.
DevFun Gradle plugin options not applied.
Falling back to compile-time path introspection.""",
                t
            )
        }
    }

    private fun addKotlinPluginToClassPath(project: Project) {
        val kotlinVersion = PluginApiVersionFromClassPath.tryGetVersion()
                ?: PluginApiVersionFromSignatures.tryGetVersion()
                ?: PluginApiVersionFromCompileVersion.tryGetVersion()
                ?: KotlinVersion.CURRENT
        project.logger.debug("Detected Kotlin plugin API version: $kotlinVersion")
        when {
            kotlinVersion.isAtLeast(1, 2, 70) -> addJarToClassLoader(project, "1271")
            kotlinVersion.isAtLeast(1, 2, 60) -> addJarToClassLoader(project, "1261")
            kotlinVersion.isAtLeast(1, 2, 50) -> addJarToClassLoader(project, "1251")
            else -> {
                project.logger.warn("Failed to detect Kotlin version - assuming latest.")
                addJarToClassLoader(project, "1271")
            }
        }
    }

    /**
     * Unfortunately we need to add it to the classloader directly as we can't modify the `project.buildscript.dependencies`
     * after they've been resolved.
     */
    private fun addJarToClassLoader(project: Project, version: String) {
        val jarFileName = "kotlin-plugin-$version.jar"
        val jarStream = this::class.java.getResourceAsStream("/$jarFileName")
        val tmpDir = File(System.getProperty("java.io.tmpdir", "."), "df").apply { mkdirs() }

        val jarFile = File(tmpDir, "kotlin-plugin-$version.jar")
        jarFile.outputStream().use { jarStream.copyTo(it) }
        addUrlMethod.invoke(Thread.currentThread().contextClassLoader, jarFile.toURI().toURL())
        project.logger.debug("Added to class-loader: jarFile=$jarFileName")
        println("Added to class-loader: jarFile=$jarFile")
    }
}

private object PluginApiVersionFromClassPath {
    private val pluginApiJarNameRegex = Regex("""kotlin-gradle-plugin-api-(\d+)\.(\d+)\.(\d+)\.jar""")

    fun tryGetVersion(): KotlinVersion? {
        var kotlinVersion: KotlinVersion? = null

        try {
            val contextClassLoader = Thread.currentThread().contextClassLoader
            if (contextClassLoader is ClassLoaderHierarchy) {
                contextClassLoader.visit(object : ClassLoaderVisitor() {
                    override fun visitClassPath(classPath: Array<out URL>?) {
                        if (kotlinVersion != null) return

                        classPath?.forEach {
                            val match = pluginApiJarNameRegex.find(it.toString())
                            if (match != null && match.groups.size == 4) {
                                val g = match.groups
                                kotlinVersion = KotlinVersion(g[1]!!.value.toInt(), g[2]!!.value.toInt(), g[3]!!.value.toInt())
                                return
                            }
                        }
                    }
                })
            }
        } catch (ignore: Throwable) {
        }

        return kotlinVersion
    }
}

private object PluginApiVersionFromSignatures {
    // removed at Kotlin 1.2.6x (true for Kotlin <= 1.2.5x)
    private val hasGroupNameMethod by lazy {
        try {
            KotlinGradleSubplugin::class.java.getDeclaredMethod("getGroupName")
            true
        } catch (t: Throwable) {
            false
        }
    }

    private val applyMethod by lazy {
        try {
            KotlinGradleSubplugin::class.java.declaredMethods.singleOrNull { it.name == "apply" }?.toString()
        } catch (t: Throwable) {
            null
        }
    }

    // changed after 1.2.6x (true for Kotlin <= 1.2.6x)
    private val applyUsesSourceSet by lazy { applyMethod?.contains("SourceSet") == true }

    // present from 1.2.7x+ (true for Kotlin <= 1.2.7x)
    private val applyUsesKotlinCompilation by lazy { applyMethod?.contains("KotlinCompilation") == true }

    fun tryGetVersion(): KotlinVersion? {
        if (hasGroupNameMethod) return KotlinVersion(1, 2, 51)
        if (applyUsesSourceSet) return KotlinVersion(1, 2, 61)
        if (applyUsesKotlinCompilation) return KotlinVersion(1, 2, 71)
        return null
    }
}

private object PluginApiVersionFromCompileVersion {
    private val versionRegex = Regex("""(\d+)\.(\d+)\.(\d+)""")

    fun tryGetVersion(): KotlinVersion? {
        try {
            // The compiler version seems to be more representative of the plugin API version
            val match = versionRegex.find(KotlinCompilerVersion.VERSION)
            if (match != null && match.groups.size == 4) {
                val g = match.groups
                return KotlinVersion(g[1]!!.value.toInt(), g[2]!!.value.toInt(), g[3]!!.value.toInt())
            }
        } catch (ignore: Throwable) {
        }

        return null
    }
}
