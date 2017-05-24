[gh-pages](../index.md) / [com.nextfaze.devfun.compiler](index.md) / [FLAG_SERVICES_USE_FILER](.)

# FLAG_SERVICES_USE_FILER

`const val FLAG_SERVICES_USE_FILER: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-compiler/src/main/java/com/nextfaze/devfun/compiler/Compiler.kt#L136)

Flag to enable [Filer](http://docs.oracle.com/javase/6/docs/api/javax/annotation/processing/Filer.html) for services file generation on Application projects instead of manually. *(default: `false`)*

Generating service using standard Java annotation processor calls involves using [Filer.createResource](http://docs.oracle.com/javase/6/docs/api/javax/annotation/processing/Filer.html#createResource(javax.tools.JavaFileManager.Location, java.lang.CharSequence, java.lang.CharSequence, javax.lang.model.element.Element...)) at [StandardLocation.CLASS_OUTPUT](#).
Library projects will always use `Filer`, however Application projects fail to pick up `CLASS_OUTPUT` generated files
([#262811](https://issuetracker.google.com/262811)) and thus will be manually created unless this is `true`.

If `false`, service files will be manually created at:

* `<buildDir>/intermediates/sourceFolderJavaResources/<variant>/...` *allows detection on first build*
* `<buildDir>/generated/javaResources/<variant>/...` *allows detection on subsequent and non-source (incremental) builds*

This process works for both library and application modules, but is non-standard and undocumented, and services files
may only be picked up on *subsequent* build rounds under some circumstances.

Set using APT options:

``` gradle
android {
     defaultConfig {
         javaCompileOptions {
             annotationProcessorOptions {
                 argument 'devfun.services.filer', 'true'
             }
         }
     }
}
```

**This will default to `true` once [#262811](https://issuetracker.google.com/262811) is fixed.**

