@file:Suppress("unused")

import org.gradle.api.Project

val Project.isSnapshot get() = getBooleanProperty("VERSION_SNAPSHOT", true)

val Project.versionName: String
    get() {
        val versionName = getStringProperty("VERSION_NAME", "0.0.0")
        return if (project.isSnapshot) "$versionName-SNAPSHOT" else versionName
    }

val Project.groupName get() = getStringProperty("GROUP", "com.nextfaze.devfun")

val Project.isAndroid get() = plugins.hasPlugin("com.android.library") || plugins.hasPlugin("com.android.application")
