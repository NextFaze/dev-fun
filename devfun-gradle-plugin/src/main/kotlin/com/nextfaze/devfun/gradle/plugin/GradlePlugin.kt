package com.nextfaze.devfun.gradle.plugin

import com.nextfaze.devfun.compiler.*
import org.gradle.api.Plugin
import org.gradle.api.Project

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
    /** Apply this gradle plugin to the [project] registering [DevFunExtension] as `devFun`. */
    override fun apply(project: Project) {
        project.extensions.create("devFun", DevFunExtension::class.java)
    }
}
