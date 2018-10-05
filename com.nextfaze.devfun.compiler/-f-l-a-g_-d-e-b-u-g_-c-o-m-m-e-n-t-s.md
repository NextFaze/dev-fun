[gh-pages](../index.md) / [com.nextfaze.devfun.compiler](index.md) / [FLAG_DEBUG_COMMENTS](./-f-l-a-g_-d-e-b-u-g_-c-o-m-m-e-n-t-s.md)

# FLAG_DEBUG_COMMENTS

`const val FLAG_DEBUG_COMMENTS: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-compiler/src/main/java/com/nextfaze/devfun/compiler/Compiler.kt#L67)

Flag to output additional debug info as code comments. *(default: `false`)*

Will show various class/function enclosing types, arg types, modifiers, etc.

Set using APT options:

``` kotlin
android {
     defaultConfig {
         javaCompileOptions {
             annotationProcessorOptions {
                 argument("devfun.debug.comments", "true")
             }
         }
     }
}
```

