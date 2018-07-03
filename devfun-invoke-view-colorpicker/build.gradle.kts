import com.nextfaze.devfun.*

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

description = "Provides a ColorPicker to be rendered by the invoke UI for annotated function parameters."

android {
    compileSdkVersion(Android.compileSdkVersion)

    defaultConfig {
        minSdkVersion(Android.minSdkVersion)
        targetSdkVersion(Android.targetSdkVersion)
        versionCode = Android.versionCode
        versionName = Android.versionName(project)

        consumerProguardFile("../proguard-rules-common.pro")
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"

        javaCompileOptions {
            annotationProcessorOptions {
                // We're using KAPT so ignore annotationProcessor configuration dependencies
                includeCompileClasspath = false
                if (project.isSnapshot) {
                    argument("devfun.debug.verbose", "true")
                }
            }
        }
    }

    resourcePrefix("df_${project.name.replace("devfun-", "")}_".replace('-', '_'))
}

dependencies {
    // DevFun
    api(project(":devfun"))

    // Color Picker
    implementation("com.rarepebble:colorpicker:2.3.0")

    // Kotlin
    api(Config.kotlinStdLib)

    // Google AutoService
    kapt(Config.autoService)
    compileOnly(Config.autoService)
}

configureDokka()
configurePublishing()
