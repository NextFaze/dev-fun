[gh-pages](../index.md) / [com.nextfaze.devfun.compiler](index.md) / [FLAG_DEBUG_VERBOSE](./-f-l-a-g_-d-e-b-u-g_-v-e-r-b-o-s-e.md)

# FLAG_DEBUG_VERBOSE

`const val FLAG_DEBUG_VERBOSE: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-compiler/src/main/java/com/nextfaze/devfun/compiler/Compiler.kt#L85)

Flag to enable additional compile/processing log output. *(default: `false`)*

Set using APT options:

``` kotlin
android {
     defaultConfig {
         javaCompileOptions {
             annotationProcessorOptions {
                 argument("devfun.debug.verbose", "true")
             }
         }
     }
}
```

