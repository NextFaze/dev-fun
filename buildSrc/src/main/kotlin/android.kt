@file:Suppress("RemoveRedundantBackticks", "NON_PUBLIC_CALL_FROM_PUBLIC_INLINE", "NOTHING_TO_INLINE")

import com.android.build.gradle.BaseExtension
import com.android.build.gradle.internal.dsl.BaseFlavor
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware

object Android {
    const val compileSdkVersion = 28

    const val minSdkVersion = 15
    const val targetSdkVersion = 28
    const val versionCode = 1
}

@Suppress("unused")
inline fun Project.configureAndroidLib() {
    androidExt {
        compileSdkVersion(Android.compileSdkVersion)

        defaultConfig {
            minSdkVersion(Android.minSdkVersion)
            targetSdkVersion(Android.targetSdkVersion)
            versionCode = Android.versionCode
            versionName = project.versionName

            consumerProguardFile("../proguard-rules-common.pro")
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

            javaCompileOptions {
                annotationProcessorOptions {
                    // We're using KAPT so ignore annotationProcessor configuration dependencies
                    includeCompileClasspath = false
                    if (project.isSnapshot) {
                        argument("devfun.debug.verbose", "true")
                    }
                }
            }

            buildConfigField("VERSION_SNAPSHOT", project.isSnapshot)
        }

        resourcePrefix("df_${project.name.replace("devfun-", "")}_".replace('-', '_'))
    }
}

@Suppress("UnstableApiUsage")
private inline fun Project.`androidExt`(noinline configure: BaseExtension.() -> Unit): Unit =
    (this as ExtensionAware).extensions.configure("android", configure)

private inline fun <T : Any> BaseFlavor.buildConfigField(name: String, value: T) =
    when (value) {
        is Boolean -> buildConfigField("boolean", name, """Boolean.parseBoolean("$value")""")
        else -> throw RuntimeException("buildConfigField type ${value::class} not implemented.")
    }
