[gh-pages](../../index.md) / [com.nextfaze.devfun.gradle.plugin](../index.md) / [DevFunExtension](index.md) / [packageRoot](./package-root.md)

# packageRoot

`var packageRoot: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?`

Sets the package root for the generated code. *(default: `<application package>`)*

Attempts will be made to auto-detect the project package by using the class output directory and known/standard
relative paths to various build files, but if necessary this option can be set instead.

Final output package will be: [packageRoot](./package-root.md).`<variant?>`.[packageSuffix](package-suffix.md)

`<variant?>` will be omitted if both `packageRoot` and `packageSuffix` are provided.

