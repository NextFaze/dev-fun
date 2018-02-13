package com.nextfaze.devfun.compiler

import java.io.File
import javax.annotation.processing.ProcessingEnvironment
import javax.tools.Diagnostic
import javax.tools.StandardLocation

// Kapt1 uses <buildDir>/intermediates/classes/<buildType>/...
// Kapt2 uses <buildDir>/tmp/kotlin-classes/<buildType>/...
// Kapt3 uses <buildDir>/tmp/kapt3/classes/<buildType>/...
// /home/user/<path>/<to>/<project>/<app-name>/build/intermediates/classes/<build-type>/META-INF/services/com.nextfaze.devfun.generated.MenuDefinitions
// Path may also be changed using gradle project.buildDir = '...' (default build dir ends with /build/, so we don't include that in regex)
private val servicesPathRegex =
    File.separator.let { s ->
        Regex("(.*)(${s}intermediates${s}classes$s|${s}tmp${s}kotlin-classes$s|${s}tmp${s}kapt3${s}classes$s)(.*)${s}META-INF${s}services$s.*")
    }

// Patterns for BuildConfig.java
private val manifestPackageRegex = Regex("\n?package (.*);")
private val applicationIdRegex = Regex("String APPLICATION_ID = \"(.*)\";")
private val buildTypeRegex = Regex("String BUILD_TYPE = \"(.*)\";")
private val flavorRegex = Regex("String FLAVOR = \"(.*)\";")

private data class BuildConfig(
    val manifestPackage: String,
    val applicationId: String,
    val buildType: String,
    val flavor: String
) {
    val variantDir = when {
        flavor.isEmpty() -> buildType
        buildType.isEmpty() -> flavor
        else -> "$flavor/$buildType"
    }

    override fun toString() =
        "BuildConfig(manifestPackage='$manifestPackage', applicationId='$applicationId', buildType='$buildType', flavor='$flavor', variantDir='$variantDir')"
}

internal class CompileContext(private val processingEnv: ProcessingEnvironment) {
    val pkg by lazy pkg@ {
        // Use package overrides if provided
        val override = packageOverride ?: extPackageOverride
        if (override != null) {
            return@pkg override.also { note { "DevFun package from override option is '$it'" } }
        }

        // Use root/suffix override if both provided (which means we don't need to know the variant)
        val packageRoot = packageRoot ?: extPackageRoot
        val packageSuffix = packageSuffix ?: extPackageSuffix
        if (packageRoot != null && packageSuffix != null) {
            return@pkg "$packageRoot.$packageSuffix".also { note { "DevFun package from root/suffix options is '$it'" } }
        }

        // Use Gradle plugin provided values, but use package root if present
        val applicationPackage = packageRoot ?: applicationPackage
        val applicationVariant = applicationVariant
        if (applicationPackage != null && applicationVariant != null) {
            val variantPkg = applicationVariant.splitCamelCaseToPackage()
            note { "variantPkg=$variantPkg" }
            return@pkg "$applicationPackage.$variantPkg.$PACKAGE_SUFFIX_DEFAULT".also { note { "DevFun package from application options is '$it'" } }
        }

        // They aren't using the Gradle plugin, or it failed to identify the build details - fallback to inspection
        warn(
            """Application package and/or variant arguments were missing or invalid.
            |Please ensure you have applied the DevFun gradle plugin.
            |This can be done by adding "plugins { 'com.nextfaze.devfun' }" to your build.gradle file.
            |Falling back to inspection of classpath and file parsing - this method is unreliable!""".trimMargin()
        )

        // Generate it from BuildConfig fields etc
        return@pkg StringBuilder().apply {
            fun appendPart(part: String) {
                if (part.isNotBlank()) {
                    if (this.isNotEmpty()) {
                        append(".")
                    }
                    append(part)
                }
            }

            if (packageRoot == null) {
                appendPart(buildConfig.applicationId)
            } else {
                appendPart(packageRoot)
            }

            appendPart(buildConfig.buildType)
            appendPart(buildConfig.flavor)

            appendPart(packageSuffix ?: PACKAGE_SUFFIX_DEFAULT)
        }.toString().also { note { "DevFun package from classpath and file parsing is '$it'" } }
    }

    private val servicesPath by lazy {
        val resource = processingEnv.filer.createResource(StandardLocation.CLASS_OUTPUT, "", "$META_INF_SERVICES/services.tmp")
        try {
            File(resource.toUri()).canonicalPath
        } finally {
            resource.delete()
        }
    }

    private val resourcePathMatch by lazy {
        servicesPathRegex.matchEntire(servicesPath) ?: run {
            error("Failed to match resources path from $servicesPath")
            throw BuildContextException("Failed to locate active BuildConfig.java")
        }
    }

    private val buildDir by lazy {
        File(resourcePathMatch.groupValues[1]).also {
            if (!it.exists()) {
                error("buildDir $it does not exist!")
                throw BuildContextException("Failed to locate active BuildConfig.java - buildDir $it does not exist!")
            }
        }
    }
    private val buildVariant by lazy { resourcePathMatch.groupValues[3] }

    private val buildConfig by lazy {
        val buildConfigsDir = File(buildDir, "generated/source/buildConfig/")
        if (!buildConfigsDir.exists()) {
            error("Build configs directory does not exist at $buildConfigsDir (buildDir=$buildDir, servicesPath=$servicesPath)")
            throw BuildContextException("Failed to locate active BuildConfig.java")
        }

        val buildConfigFiles = buildConfigsDir
            .filterRecursively { it.name == "BuildConfig.java" }
            .filter { !it.canonicalPath.contains("androidTest") }
        if (buildConfigFiles.isEmpty()) {
            error("Failed to find any BuildConfig.java files int buildConfig directory $buildConfigsDir (buildDir=$buildDir, servicesPath=$servicesPath)")
            throw BuildContextException("Failed to locate active BuildConfig.java")
        }

        val buildConfigs = buildConfigFiles.map {
            val text = it.readText()
            BuildConfig(
                manifestPackage = manifestPackageRegex.find(text)?.groupValues?.getOrNull(1) ?: "",
                applicationId = applicationIdRegex.find(text)?.groupValues?.getOrNull(1) ?: "",
                buildType = buildTypeRegex.find(text)?.groupValues?.getOrNull(1) ?: "",
                flavor = flavorRegex.find(text)?.groupValues?.getOrNull(1) ?: ""
            )
        }

        buildConfigs.singleOrNull {
            it.variantDir.equals(buildVariant, true) || it.variantDir.replace(File.separator, "").equals(buildVariant, true)
        } ?: run failedToMatch@ {
            error("Failed to match single build variant '$buildVariant' to any buildConfigs: $buildConfigs")
            throw BuildContextException("Failed to locate active BuildConfig.java")
        }
    }

    private val packageRoot by lazy { PACKAGE_ROOT.optionOf() }
    private val packageSuffix by lazy { PACKAGE_SUFFIX.optionOf() }
    private val packageOverride by lazy { PACKAGE_OVERRIDE.optionOf() }

    private val extPackageRoot by lazy { EXT_PACKAGE_ROOT.optionOf() }
    private val extPackageSuffix by lazy { EXT_PACKAGE_SUFFIX.optionOf() }
    private val extPackageOverride by lazy { EXT_PACKAGE_OVERRIDE.optionOf() }

    private val applicationPackage by lazy { APPLICATION_PACKAGE.optionOf() }
    private val applicationVariant by lazy { APPLICATION_VARIANT.optionOf() }

    private val isDebugVerbose by lazy { FLAG_DEBUG_VERBOSE.optionOf()?.toBoolean() ?: false }

    private fun error(message: String) = processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, message)
    private fun warn(message: String) = processingEnv.messager.printMessage(Diagnostic.Kind.MANDATORY_WARNING, message)
    private fun note(condition: Boolean = isDebugVerbose, body: () -> String) =
        runIf(condition) { processingEnv.messager.printMessage(Diagnostic.Kind.NOTE, body()) }

    private fun String.optionOf() = processingEnv.options[this]?.trim()?.takeIf { it.isNotBlank() }
}

private class BuildContextException(message: String?) : Throwable(message)

private val SPLIT_REGEX = Regex("(?<=[a-z0-9])(?=[A-Z])|[\\s]")

private fun String.splitCamelCaseToPackage() = this
    .split(SPLIT_REGEX)
    .map { it.trim().toLowerCase() }
    .filter(String::isNotBlank)
    .joinToString(".")
