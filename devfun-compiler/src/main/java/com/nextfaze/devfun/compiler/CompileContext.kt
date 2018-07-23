package com.nextfaze.devfun.compiler

import java.io.File
import javax.annotation.processing.Filer
import javax.inject.Inject
import javax.inject.Singleton
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

@Singleton
internal class CompileContext @Inject constructor(
    private val options: Options,
    private val filer: Filer,
    logging: Logging
) {
    private val log by logging()

    val pkg by lazy pkg@{
        // Use package overrides if provided
        val override = packageOverride ?: extPackageOverride
        if (override != null) {
            return@pkg override.also { log.note { "DevFun package from override option is '$it'" } }
        }

        // Use root/suffix override if both provided (which means we don't need to know the variant)
        val packageRoot = packageRoot ?: extPackageRoot
        val packageSuffix = packageSuffix ?: extPackageSuffix
        if (packageRoot != null && packageSuffix != null) {
            return@pkg "$packageRoot.$packageSuffix".also { log.note { "DevFun package from root/suffix options is '$it'" } }
        }

        // Use Gradle plugin provided values, but use package root if present
        val applicationPackage = packageRoot ?: applicationPackage
        val applicationVariant = applicationVariant
        if (applicationPackage != null && applicationVariant != null) {
            val variantPkg = applicationVariant.splitCamelCaseToPackage()
            return@pkg "$applicationPackage.$variantPkg.$PACKAGE_SUFFIX_DEFAULT".also { log.note { "DevFun package from application options is '$it'" } }
        }

        // They aren't using the Gradle plugin, or it failed to identify the build details - fallback to inspection
        // TODO Changing this version number every release is annoying!
        log.warn {
            """Application package and/or variant arguments were missing or invalid.
            |Please ensure you have applied the DevFun gradle plugin.
            |
            |This can be done by adding the plugins block to your build.gradle file:
            |plugins {
            |    id 'com.nextfaze.devfun' version '1.2.1'
            |}
            |
            |Falling back to inspection of classpath and file parsing - this method is unreliable!""".trimMargin()
        }

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
        }.toString().also { log.note { "DevFun package from classpath and file parsing is '$it'" } }
    }

    private val servicesPath by lazy {
        val resource = filer.createResource(StandardLocation.CLASS_OUTPUT, "", "$META_INF_SERVICES/services.tmp")
        try {
            File(resource.toUri()).canonicalPath
        } finally {
            resource.delete()
        }
    }

    private val resourcePathMatch by lazy {
        servicesPathRegex.matchEntire(servicesPath) ?: run {
            log.error { "Failed to match resources path from $servicesPath" }
            throw BuildContextException("Failed to locate active BuildConfig.java")
        }
    }

    private val buildDir by lazy {
        File(resourcePathMatch.groupValues[1]).also {
            if (!it.exists()) {
                log.error { "buildDir $it does not exist!" }
                throw BuildContextException("Failed to locate active BuildConfig.java - buildDir $it does not exist!")
            }
        }
    }
    private val buildVariant by lazy { resourcePathMatch.groupValues[3] }

    private val buildConfig by lazy {
        val buildConfigsDir = File(buildDir, "generated/source/buildConfig/")
        if (!buildConfigsDir.exists()) {
            log.error { "Build configs directory does not exist at $buildConfigsDir (buildDir=$buildDir, servicesPath=$servicesPath)" }
            throw BuildContextException("Failed to locate active BuildConfig.java")
        }

        val buildConfigFiles = buildConfigsDir
            .filterRecursively { it.name == "BuildConfig.java" }
            .filter { !it.canonicalPath.contains("androidTest") }
        if (buildConfigFiles.isEmpty()) {
            log.error { "Failed to find any BuildConfig.java files int buildConfig directory $buildConfigsDir (buildDir=$buildDir, servicesPath=$servicesPath)" }
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
        } ?: run failedToMatch@{
            log.error { "Failed to match single build variant '$buildVariant' to any buildConfigs: $buildConfigs" }
            throw BuildContextException("Failed to locate active BuildConfig.java")
        }
    }

    private val packageRoot get() = options.packageRoot
    private val packageSuffix get() = options.packageSuffix
    private val packageOverride get() = options.packageOverride

    private val extPackageRoot get() = options.extPackageRoot
    private val extPackageSuffix get() = options.extPackageSuffix
    private val extPackageOverride get() = options.extPackageOverride

    private val applicationPackage get() = options.applicationPackage
    private val applicationVariant get() = options.applicationVariant
}

private class BuildContextException(message: String?) : Throwable(message)

private val SPLIT_REGEX = Regex("(?<=[a-z0-9])(?=[A-Z])|[\\s]")

private fun String.splitCamelCaseToPackage() = this
    .split(SPLIT_REGEX)
    .map { it.trim().toLowerCase() }
    .filter(String::isNotBlank)
    .joinToString(".")

private inline fun File.filterRecursively(predicate: (File) -> Boolean) = filterRecursivelyTo(ArrayList(), predicate)

private inline fun <C : MutableCollection<in File>> File.filterRecursivelyTo(destination: C, predicate: (File) -> Boolean): C {
    val directories = ArrayList<File>()
    if (isDirectory) {
        directories.add(this)
    } else {
        if (predicate(this)) destination.add(this)
        return destination
    }

    var i = 0
    while (i < directories.size) {
        directories[i++].listFiles().orEmpty().forEach {
            if (predicate(it)) {
                destination.add(it)
            }
            if (it.isDirectory) {
                directories.add(it)
            }
        }
    }

    return destination
}
