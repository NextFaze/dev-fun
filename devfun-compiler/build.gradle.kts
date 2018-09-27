plugins {
    id("kotlin")
    kotlin("kapt")
}

description = """Annotation processor that handles @DeveloperFunction and @DeveloperCategory annotations.
    |
    |This should be applied to your non-main kapt configuration 'kaptDebug' to avoid running/using it on release builds.""".trimMargin()

kapt {
    generateStubs = true
}

dependencies {
    // DevFun
    compile(project(":devfun-annotations"))

    // Kotlin
    compile(Dependency.kotlinStdLib)
    compile(Dependency.kotlinReflect)
    compile("org.jetbrains.kotlinx:kotlinx-metadata-jvm:0.0.4")

    // Kotlin Poet
//    compile("com.squareup:kotlinpoet:1.0.0-RC1")
    compile("com.github.alex2069:kotlinpoet:01d85d37883895b913e547ead8828dcd75889bf0")

    // Dagger
    kapt(Dependency.daggerCompiler)
    compile(Dependency.dagger)
    compileOnly(Dependency.daggerAnnotations)

    // Google AutoService - https://github.com/google/auto/tree/master/service
    kapt(Dependency.autoService)
    compileOnly(Dependency.autoService) {
        isTransitive = false
    }
}

configureDokka()
configurePublishing()
