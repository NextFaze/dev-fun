package com.nextfaze.devfun.core.loader

import android.content.Context
import com.nextfaze.devfun.annotations.DeveloperCategory
import com.nextfaze.devfun.annotations.DeveloperFunction
import com.nextfaze.devfun.core.DevFun
import com.nextfaze.devfun.generated.DevFunGenerated
import com.nextfaze.devfun.internal.*
import java.util.ServiceLoader

@DeveloperCategory("DevFun")
internal class DefinitionsLoader(private val devFun: DevFun) {
    private val log = logger()

    var definitions: List<DevFunGenerated> = listOf()
        private set
        get() {
            if (field.isNotEmpty()) return field

            val serviceDefinitions = ServiceLoader.load(DevFunGenerated::class.java).run {
                reload()
                toList()
            }

            field = try {
                // Work-around for buggy Android gradle plugin inconsistent service files packaging.
                val appGeneratedClass = tryLoadAppGeneratedClass(devFun.context)
                when {
                    serviceDefinitions.any { it::class.java == appGeneratedClass } -> {
                        log.d { "App-generated definitions loaded from ServiceLoader." }
                        serviceDefinitions
                    }
                    else -> (serviceDefinitions + appGeneratedClass.newInstance())
                }
            } catch (t: Throwable) {
                log.w(t) {
                    "Failed to check for and/or apply work-around for Android Gradle plugin bug https://issuetracker.google.com/37140464.\n" +
                            "This warning can be ignored if you see your app-defined @DeveloperFunction items."
                }
                serviceDefinitions
            }

            return field
        }

    @DeveloperFunction
    fun reloadItemDefinitions() {
        definitions = listOf()
    }
}

/**
 * Not in a `class`/`object` or cached to ensure definition reloading can take any ClassLoader changes into account.
 *
 * Also, 99% of the time it will be called just once per application instance, with additional calls resulting from
 * user-initiated reloads.
 *
 * @see DefinitionsLoader.reloadItemDefinitions
 */
private fun tryLoadAppGeneratedClass(context: Context): Class<DevFunGenerated> {
    val log = logger("tryLoadAppGeneratedClass")

    /**
     * By default the `BuildConfig` class is in the root application package.
     * However if the app uses an `applicationIdSuffix`, it will be included as part of the deployed app package (from [Context.getPackageName]).
     * Thus we traverse up the packages in hope of finding it.
     */
    val appBuildConfigClass = run loadClass@ {
        var pkg = context.packageName.orEmpty()
        while (pkg.isNotBlank()) {
            val buildConfigPath = "$pkg.BuildConfig"
            log.d { "Try-get BuildConfig from $buildConfigPath" }
            try {
                return@loadClass Class.forName(buildConfigPath)
            } catch (ignore: Throwable) {
                pkg = pkg.substringBeforeLast(".", "")
            }
        }
        throw ClassNotFoundException("Failed to find app BuildConfig class")
    }

    val APPLICATION_ID = appBuildConfigClass.getField("APPLICATION_ID").get(null) as String
    val BUILD_TYPE = appBuildConfigClass.getField("BUILD_TYPE").get(null) as String
    val FLAVOR = appBuildConfigClass.getField("FLAVOR").get(null) as String

    val appGeneratedPath = listOf(APPLICATION_ID, BUILD_TYPE, FLAVOR, "devfun_generated", "DevFunDefinitions").join(".")
    log.d { "Try-get app-generated DevFunDefinitions from $appGeneratedPath" }

    @Suppress("UNCHECKED_CAST")
    return Class.forName(appGeneratedPath) as Class<DevFunGenerated>
}

/**
 * Same as `Iterable.joinToString` with some tweaks.
 */
private fun <T> Iterable<T>.join(separator: CharSequence = ", ",
                                 prefix: CharSequence = "",
                                 postfix: CharSequence = "",
                                 skipBlank: Boolean = true,
                                 prefixIfBlank: Boolean = false,
                                 postfixIfBlank: Boolean = false,
                                 transform: ((T) -> CharSequence)? = null) = StringBuilder().apply {
    var prefixed = false
    var elementsAdded = 0
    fun applyPrefix() {
        if (prefixed || prefix.isBlank()) return
        prefixed = true
        append(prefix)
    }
    this@join.forEach {
        val element = transform?.invoke(it) ?: it.toString()
        if (skipBlank && element.isBlank()) return@forEach
        if (element.isNotBlank() || prefixIfBlank) {
            applyPrefix()
        }
        if (isNotBlank()) {
            append(separator)
        }
        elementsAdded++
        append(element)
    }
    // Ensure prefix
    if (elementsAdded <= 0 || prefixIfBlank) {
        applyPrefix()
    }
    // Postfix (exclude prefix when considering
    if (postfix.isNotBlank()) {
        if (isNotBlank()) {
            append(separator)
        }
        if (elementsAdded > 0 || postfixIfBlank) {
            append(postfix)
        }
    }
}.toString()
