[gh-pages](../index.md) / [com.nextfaze.devfun.compiler](index.md) / [GENERATE_DEFINITIONS](./-g-e-n-e-r-a-t-e_-d-e-f-i-n-i-t-i-o-n-s.md)

# GENERATE_DEFINITIONS

`const val GENERATE_DEFINITIONS: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-compiler/src/main/java/com/nextfaze/devfun/compiler/Compiler.kt#L275)

Flag to control generation of implementations of [DevFunGenerated](../com.nextfaze.devfun.generated/-dev-fun-generated/index.md). *(default: `<true>`)*

Useful for testing or if you only want to generate annotation interfaces.

Set using APT options:

``` kotlin
android {
     defaultConfig {
         javaCompileOptions {
             annotationProcessorOptions {
                 argument("devfun.definitions.generate", "false")
             }
         }
     }
}
```

**See Also**

[GENERATE_INTERFACES](-g-e-n-e-r-a-t-e_-i-n-t-e-r-f-a-c-e-s.md)

[DevFunProcessor](-dev-fun-processor/index.md)

