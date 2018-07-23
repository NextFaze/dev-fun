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
