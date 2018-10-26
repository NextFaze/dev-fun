[gh-pages](../index.md) / [com.nextfaze.devfun.compiler](index.md) / [PROMOTE_NOTE_LOG_MESSAGES](./-p-r-o-m-o-t-e_-n-o-t-e_-l-o-g_-m-e-s-s-a-g-e-s.md)

# PROMOTE_NOTE_LOG_MESSAGES

`const val PROMOTE_NOTE_LOG_MESSAGES: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-compiler/src/main/java/com/nextfaze/devfun/compiler/DevFunProcessor.kt#L293)

Flag to have all `note` messages output as `warning` - workaround for various KAPT logging implementations and for debugging purposes.  *(default: `<false>`)*

Set using APT options:

``` kotlin
android {
     defaultConfig {
         javaCompileOptions {
             annotationProcessorOptions {
                 argument("devfun.logging.note.promote", "true")
             }
         }
     }
}
```

