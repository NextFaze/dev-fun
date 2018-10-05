[gh-pages](../index.md) / [com.nextfaze.devfun.compiler](index.md) / [GENERATE_INTERFACES](./-g-e-n-e-r-a-t-e_-i-n-t-e-r-f-a-c-e-s.md)

# GENERATE_INTERFACES

`const val GENERATE_INTERFACES: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-compiler/src/main/java/com/nextfaze/devfun/compiler/DevAnnotationProcessor.kt#L38)

Flag to control generation of [DeveloperAnnotation](../com.nextfaze.devfun/-developer-annotation/index.md) properties `AnnotationProperties` interfaces. *(default: `<true>`)*

Useful for testing or if you only want to generate [DevFunGenerated](../com.nextfaze.devfun.generated/-dev-fun-generated/index.md) implementations.

Set using APT options:

``` kotlin
android {
     defaultConfig {
         javaCompileOptions {
             annotationProcessorOptions {
                 argument("devfun.interfaces.generate", "false")
             }
         }
     }
}
```

**See Also**

[GENERATE_DEFINITIONS](-g-e-n-e-r-a-t-e_-d-e-f-i-n-i-t-i-o-n-s.md)

[DevAnnotationProcessor](-dev-annotation-processor/index.md)

