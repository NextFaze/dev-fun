[gh-pages](../index.md) / [com.nextfaze.devfun.compiler](index.md) / [FLAG_DEBUG_VERBOSE](.)

# FLAG_DEBUG_VERBOSE

`const val FLAG_DEBUG_VERBOSE: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-compiler/src/main/java/com/nextfaze/devfun/compiler/Compiler.kt#L86)

Flag to enable additional compile/processing log output. *(default: `false`)*

Set using APT options:

``` gradle
android {
     defaultConfig {
         javaCompileOptions {
             annotationProcessorOptions {
                 argument 'devfun.debug.verbose', 'true'
             }
         }
     }
}
```

