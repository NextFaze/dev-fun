buildscript {
    repositories {
        mavenLocal()
        maven { setUrl("https://dl.bintray.com/kotlin/kotlin-dev") }
        jcenter()
        google()
        gradlePluginPortal()
        maven { setUrl("https://jitpack.io") }
    }

    dependencies {
        project.registerDependencies()
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Dependency.kotlinVersion}")

        val useMavenLocal = getBooleanProperty("DEVFUN_GRADLE_PLUGIN_USE_MAVEN_LOCAL", false)
        if (useMavenLocal) {
            classpath("com.nextfaze.devfun:devfun-gradle-plugin:+")
        } else {
            classpath("gradle.plugin.com.nextfaze.devfun:devfun-gradle-plugin:+")
        }
    }
}

registerDependencies()

plugins {
    `build-scan`
}

buildScan {
    setTermsOfServiceUrl("https://gradle.com/terms-of-service")
    setTermsOfServiceAgree("yes")
}

allprojects {
    version = versionName
    group = groupName

    repositories {
        mavenLocal()
        maven { setUrl("https://dl.bintray.com/kotlin/kotlin-dev") }
        jcenter()
        google()
        maven { setUrl("https://jitpack.io") }
    }

    println(
        """$name
        |>> group: $group
        |>> artifact: $name
        |>> version: $version
        |""".trimMargin()
    )

    if (project.isSnapshot) {
        configurations.all {
            // check for updates every build
            resolutionStrategy.cacheChangingModulesFor(0, TimeUnit.SECONDS)
        }
    }
}

val cleanRoot = task<Delete>("cleanRoot") {
    delete(rootProject.buildDir)
    delete(rootProject.file(".gradle"))
    delete(rootProject.file("buildSrc/.gradle"))
}
getTasksByName("clean", true).forEach {
    it.dependsOn(cleanRoot)
}
