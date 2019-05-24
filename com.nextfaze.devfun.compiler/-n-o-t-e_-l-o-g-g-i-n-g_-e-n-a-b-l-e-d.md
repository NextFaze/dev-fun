[gh-pages](../index.md) / [com.nextfaze.devfun.compiler](index.md) / [NOTE_LOGGING_ENABLED](./-n-o-t-e_-l-o-g-g-i-n-g_-e-n-a-b-l-e-d.md)

# NOTE_LOGGING_ENABLED

`const val NOTE_LOGGING_ENABLED: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-compiler/src/main/java/com/nextfaze/devfun/compiler/DevFunProcessor.kt#L317)

Flag to enable logging of `note` messages.  *(default: `<false>`)*

Set using APT options:

``` kotlin
android {
     defaultConfig {
         javaCompileOptions {
             annotationProcessorOptions {
                 argument("devfun.logging.note.enabled", "true")
             }
         }
     }
}
```

**See Also**

[PROMOTE_NOTE_LOG_MESSAGES](-p-r-o-m-o-t-e_-n-o-t-e_-l-o-g_-m-e-s-s-a-g-e-s.md)

