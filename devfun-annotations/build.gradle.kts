plugins {
    id("kotlin")
    kotlin("kapt")
}

description = """Contains the annotations @DeveloperFunction and @DeveloperCategory, and various interface definitions.
    |
    |As this library is primarily interface definitions and inline functions, it will have a negligible impact on your method count and dex sizes.
    |Apply to your main 'implementation' configuration.""".trimMargin()

dependencies {
    // Kotlin
    compile(Dependency.kotlinStdLib)

    // JavaX Inject
    compile(Dependency.javaxInject)
}

configureDokka()
configurePublishing()
