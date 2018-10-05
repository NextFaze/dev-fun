[gh-pages](../index.md) / [com.nextfaze.devfun.compiler](index.md) / [PACKAGE_SUFFIX](./-p-a-c-k-a-g-e_-s-u-f-f-i-x.md)

# PACKAGE_SUFFIX

`const val PACKAGE_SUFFIX: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-compiler/src/main/java/com/nextfaze/devfun/compiler/Compiler.kt#L111)

Sets the package suffix for the generated code. *(default: `devfun_generated`)*

This is primarily for testing purposes to allow multiple generations in the same classpath.

* If this is null (unset) [PACKAGE_SUFFIX_DEFAULT](-p-a-c-k-a-g-e_-s-u-f-f-i-x_-d-e-f-a-u-l-t.md) will be used.
* If this is empty the suffix will be omitted.

Final output package will be: [PACKAGE_ROOT](-p-a-c-k-a-g-e_-r-o-o-t.md).`<variant?>`.`PACKAGE_SUFFIX`

`<variant?>` will be omitted if both `packageRoot` and `packageSuffix` are provided.

Set using APT options:

``` kotlin
android {
     defaultConfig {
         javaCompileOptions {
             annotationProcessorOptions {
                 argument("devfun.package.suffix", "custom.suffix")
             }
         }
     }
}
```

