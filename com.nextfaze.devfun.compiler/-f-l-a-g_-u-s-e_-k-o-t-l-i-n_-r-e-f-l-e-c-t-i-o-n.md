[gh-pages](../index.md) / [com.nextfaze.devfun.compiler](index.md) / [FLAG_USE_KOTLIN_REFLECTION](./-f-l-a-g_-u-s-e_-k-o-t-l-i-n_-r-e-f-l-e-c-t-i-o-n.md)

# FLAG_USE_KOTLIN_REFLECTION

`const val FLAG_USE_KOTLIN_REFLECTION: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-compiler/src/main/java/com/nextfaze/devfun/compiler/Compiler.kt#L52)

Flag to enable Kotlin reflection to get method references. *(default: `false`)* **(experimental)**

Normal java reflection works fine - Kotlin reflection was disable as it was *extremely* slow (~0.5ms vs.
**~1.5s** in some cases). *(last tested around 1.1)*

*Also, be aware that when last used, for unknown reasons every second private function reflection call using Kotlinreflection failed with `IllegalAccessViolation`, even though `isAccessible = true` was clearly being called.*

Set using APT options:

``` gradle
android {
     defaultConfig {
         javaCompileOptions {
             annotationProcessorOptions {
                 argument 'devfun.kotlin.reflection', 'true'
             }
         }
     }
}
```

**This feature is largely untested and mostly academic. It also has issues with overloaded functions.**

