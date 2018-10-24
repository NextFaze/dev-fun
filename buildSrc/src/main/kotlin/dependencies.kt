import org.gradle.api.Project
import org.gradle.kotlin.dsl.*

@Suppress("unused")
fun Project.registerDependencies() {
    apply { from(rootProject.file("versions.gradle.kts")) }

    val kotlinVersion: String by extra
    val dokkaVersion: String by extra
    val agpVersion: String by extra

    Dependency.kotlinVersion = kotlinVersion
    Dependency.dokkaVersion = dokkaVersion
    Dependency.agpVersion = agpVersion
}

@Suppress("unused", "MemberVisibilityCanBePrivate")
object Dependency {
    lateinit var agpVersion: String
    val androidPlugin by lazy { "com.android.tools.build:gradle:$agpVersion" }

    lateinit var kotlinVersion: String
    fun kotlin(version: String = kotlinVersion) = KotlinDependencies(version)
    val kotlin by lazy { kotlin() }

    const val supportRecyclerView = "androidx.recyclerview:recyclerview:1.0.0"
    const val supportAppCompat = "androidx.appcompat:appcompat:1.0.0"
    const val supportDesign = "com.google.android.material:material:1.0.0"
    const val supportAnnotations = "androidx.annotation:annotation:1.0.0"
    const val supportConstraintLayout = "androidx.constraintlayout:constraintlayout:1.1.3"
    const val androidX = "androidx.core:core-ktx:1.0.0"

    const val javaxInject = "javax.inject:javax.inject:1"

    const val daggerVersion = "2.17"
    fun dagger(version: String = daggerVersion) = "com.google.dagger:dagger:$version"
    fun daggerCompiler(version: String = daggerVersion) = "com.google.dagger:dagger-compiler:$version"
    val dagger = dagger()
    val daggerCompiler = daggerCompiler()
    const val daggerAnnotations = "org.glassfish:javax.annotation:10.0-b28"

    const val slf4jApi = "org.slf4j:slf4j-api:1.7.25"

    const val autoService = "com.google.auto.service:auto-service:1.0-rc4"

    private const val stethoVersion = "1.5.0"
    const val stetho = "com.facebook.stetho:stetho:$stethoVersion"
    const val stethoJsRhino = "com.facebook.stetho:stetho-js-rhino:$stethoVersion"

    const val glide = "com.github.bumptech.glide:glide:4.8.0"

    const val leakCanary = "com.squareup.leakcanary:leakcanary-android:1.6.1"

    lateinit var dokkaVersion: String
    val dokkaFatJar by lazy { "org.jetbrains.dokka:dokka-fatjar:$dokkaVersion" }
}

data class KotlinDependencies(val version: String, val coroutinesVersion: String = "1.0.0-RC1") {
    val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
    val gradlePluginApi = "org.jetbrains.kotlin:kotlin-gradle-plugin-api:$version"
    val stdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$version"
    val reflect = "org.jetbrains.kotlin:kotlin-reflect:$version"
    val test = "org.jetbrains.kotlin:kotlin-test:$version"
    val androidExtensions = "org.jetbrains.kotlin:kotlin-android-extensions:$version"
    val compilerEmbeddable = "org.jetbrains.kotlin:kotlin-compiler-embeddable:$version"
    val annotationProcessingEmbeddable = "org.jetbrains.kotlin:kotlin-annotation-processing-embeddable:$version"
    val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion"
    val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion"
}
