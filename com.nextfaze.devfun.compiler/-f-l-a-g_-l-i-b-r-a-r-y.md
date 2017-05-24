[gh-pages](../index.md) / [com.nextfaze.devfun.compiler](index.md) / [FLAG_LIBRARY](.)

# FLAG_LIBRARY

`const val FLAG_LIBRARY: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-compiler/src/main/java/com/nextfaze/devfun/compiler/Compiler.kt#L105)

Flag to indicate if project is a library project. *(default: `false`)*

This is necessary due to generated services file not being picked up in application modules.
Attempts will be made to auto-detect if project is a library, but if necessary this flag can be set instead.

Set using APT options:

``` gradle
android {
     defaultConfig {
         javaCompileOptions {
             annotationProcessorOptions {
                 argument 'devfun.library', 'true'
             }
         }
     }
}
```

