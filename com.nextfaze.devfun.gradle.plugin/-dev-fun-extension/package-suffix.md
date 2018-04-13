[gh-pages](../../index.md) / [com.nextfaze.devfun.gradle.plugin](../index.md) / [DevFunExtension](index.md) / [packageSuffix](./package-suffix.md)

# packageSuffix

`var packageSuffix: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?`

Sets the package suffix for the generated code. *(default: `devfun_generated`)*

This is primarily for testing purposes to allow multiple generations in the same classpath.

* If this is null (unset) [PACKAGE_SUFFIX_DEFAULT](../../com.nextfaze.devfun.compiler/-p-a-c-k-a-g-e_-s-u-f-f-i-x_-d-e-f-a-u-l-t.md) will be used.
* If this is empty the suffix will be omitted.

Final output package will be: [packageRoot](package-root.md).`<variant?>`.[packageSuffix](./package-suffix.md)

`<variant?>` will be omitted if both `packageRoot` and `packageSuffix` are provided.

