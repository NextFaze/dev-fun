plugins {
    id("kotlin")
    kotlin("kapt")
}

val kotlinVersion = "1.2.61"

dependencies {
    // DevFun
    compileOnly(project(":devfun-gradle-plugin:gradle-plugin")) {
        isTransitive = false
    }

    // Gradle
    compileOnly(gradleApi())

    // Kotlin
    compileOnly(Dependency.kotlinStdLib(kotlinVersion))
    compileOnly(Dependency.kotlinPlugin(kotlinVersion))
    compileOnly(Dependency.kotlinPluginApi(kotlinVersion))

    // Google AutoService - https://github.com/google/auto/tree/master/service
    kapt(Dependency.autoService)
    compileOnly(Dependency.autoService)
}

// Force specific Kotlin version
configurations.all {
    resolutionStrategy.force(
        "org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion",
        "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlinVersion",
        "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion",
        "org.jetbrains.kotlin:kotlin-gradle-plugin-api:$kotlinVersion"
    )
}
