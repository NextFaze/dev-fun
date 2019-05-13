plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

description = """Provides a color picker to be rendered by the invoke UI for annotated function parameters.
    |
    |Annotate dev. function Int parameters with @ColorPicker.
    |Should be on your debug configuration 'debugImplementation'.""".trimMargin()

configureAndroidLib()

dependencies {
    // DevFun
    api(project(":devfun"))

    // Color Picker
    implementation("com.rarepebble:colorpicker:2.3.0")

    // Kotlin
    api(Dependency.kotlin.stdLib)

    // Google AutoService
    kapt(Dependency.autoService)
    compileOnly(Dependency.autoServiceAnnotations)
}

configureDokka()
configurePublishing()
