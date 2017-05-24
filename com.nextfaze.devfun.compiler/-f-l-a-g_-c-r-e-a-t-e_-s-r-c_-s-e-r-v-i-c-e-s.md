[gh-pages](../index.md) / [com.nextfaze.devfun.compiler](index.md) / [FLAG_CREATE_SRC_SERVICES](.)

# FLAG_CREATE_SRC_SERVICES

`const val FLAG_CREATE_SRC_SERVICES: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-compiler/src/main/java/com/nextfaze/devfun/compiler/Compiler.kt#L247)

Flag to tell the compiler to create a services file in your sources directory instead of using generated sources. *(default: `<none>`)* **(experimental)**

This flag is primarily used to get around Android Gradle bug [#262811](https://issuetracker.google.com/262811), and will be removed once fixed.

In general this should only be used in extreme cases as it involves significant assumptions about your project
structure and tool chains.

It will attempt to create one at `<buildDir>/../src/<variantDir>/resources/META-INF/services/com.nextfaze.devfun.generated.DevFunGenerated`

*Note: Due to resource and compile task ordering, you may need to build once,then build again for the services file to be packaged (depends on project setup).*

Set using APT options:

``` gradle
android {
     defaultConfig {
         javaCompileOptions {
             annotationProcessorOptions {
                 argument 'devfun.services.src', 'true'
             }
         }
     }
}
```

Alternatively, create one yourself in your sources tree:

* `src/<buildType>/resources/META-INF/services/com.nextfaze.devfun.generated.DevFunGenerated`

With a line containing the generated class' fully qualified name.

* If using defaults it will be `<BuildConfig.APPLICATION_ID>.<BuildConfig.BUILD_TYPE>(.<BuildConfig.FLAVOR>).devfun_generated.DevFunDefinitions`
* If you have changed things, easiest way is to grab to package line from the build sources (`DevFunDefinitions.kt`)
