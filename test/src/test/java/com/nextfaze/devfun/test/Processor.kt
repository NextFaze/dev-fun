@file:Suppress("PackageDirectoryMismatch")

package javax.annotation.processing

/**
 * Provides a reference to the annotation processor interface - not actually used directly though.
 *
 *
 * We cannot include `rt.jar` as it conflicts with AndroidX Jetifier:
 * `Reason: The type does not support '.' as package separator!`
 *
 * ```kotlin
 * dependencies {
 *     // Full Java
 *     val bootJars = System.getProperty("sun.boot.class.path").toString().split(File.pathSeparator)
 *     val rtJar = bootJars.filter { it.endsWith("rt.jar") }.map { File(it) }.single()
 *     testImplementation(files(rtJar.canonicalPath))
 * }
 * ```
 */
interface Processor
