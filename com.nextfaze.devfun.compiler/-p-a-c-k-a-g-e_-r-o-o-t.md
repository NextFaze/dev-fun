[gh-pages](../index.md) / [com.nextfaze.devfun.compiler](index.md) / [PACKAGE_ROOT](.)

# PACKAGE_ROOT

`const val PACKAGE_ROOT: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-compiler/src/main/java/com/nextfaze/devfun/compiler/Compiler.kt#L135)

Sets the package root for the generated code. *(default: `<project package>`)*

Attempts will be made to auto-detect the project package by using the class output directory and known/standard
relative paths to various build files, but if necessary this option can be set instead.

Set using APT options:

``` gradle
android {
     defaultConfig {
         javaCompileOptions {
             annotationProcessorOptions {
                 argument 'devfun.package.root', 'com.your.application'
             }
         }
     }
}
```

Final output package will be: PACKAGE_ROOT.`<buildType?>`.[PACKAGE_SUFFIX](-p-a-c-k-a-g-e_-s-u-f-f-i-x.md)

`<buildType?>` will be omitted if both `PACKAGE_ROOT` and `PACKAGE_SUFFIX` are supplied.

