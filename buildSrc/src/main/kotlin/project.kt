@file:Suppress("unused")

import org.gradle.api.Project

val Project.isSnapshot get() = getBooleanProperty("VERSION_SNAPSHOT", true)

val Project.versionName: String get() = versionName(getStringProperty("VERSION_NAME", "0.0.0"))
fun Project.versionName(version: String) = if (project.isSnapshot) "$version-SNAPSHOT" else version

val Project.groupName get() = getStringProperty("GROUP", "com.nextfaze.devfun")

val Project.isAndroid get() = plugins.hasPlugin("com.android.library") || plugins.hasPlugin("com.android.application")
