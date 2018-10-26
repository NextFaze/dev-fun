[gh-pages](../index.md) / [com.nextfaze.devfun.compiler](index.md) / [PACKAGE_OVERRIDE](./-p-a-c-k-a-g-e_-o-v-e-r-r-i-d-e.md)

# PACKAGE_OVERRIDE

`const val PACKAGE_OVERRIDE: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-compiler/src/main/java/com/nextfaze/devfun/compiler/DevFunProcessor.kt#L156)

Sets the package for the generated code. *(default: `<none>`)*

This will override [PACKAGE_ROOT](-p-a-c-k-a-g-e_-r-o-o-t.md) and [PACKAGE_SUFFIX](-p-a-c-k-a-g-e_-s-u-f-f-i-x.md).

Set using APT options:

``` kotlin
android {
     defaultConfig {
         javaCompileOptions {
             annotationProcessorOptions {
                 argument 'devfun.package.override', 'com.my.full.pkg.devfun.generated'
             }
         }
     }
}
```

