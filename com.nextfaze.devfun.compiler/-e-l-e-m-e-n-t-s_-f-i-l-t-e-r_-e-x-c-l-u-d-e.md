[gh-pages](../index.md) / [com.nextfaze.devfun.compiler](index.md) / [ELEMENTS_FILTER_EXCLUDE](./-e-l-e-m-e-n-t-s_-f-i-l-t-e-r_-e-x-c-l-u-d-e.md)

# ELEMENTS_FILTER_EXCLUDE

`const val ELEMENTS_FILTER_EXCLUDE: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-compiler/src/main/java/com/nextfaze/devfun/compiler/DevFunProcessor.kt#L252)

Restrict DevFun to only process elements matching filter `elementFQN.startsWith(it)`. *(default: `<none>`)*

Value can be a comma separated list. Whitespace will be trimmed.

In general this shouldn't be used and is primarily for testing/development purposes.

Example usage (from test sources):

``` kotlin
android {
    defaultConfig {
        javaCompileOptions {
            annotationProcessorOptions {
                argument("devfun.elements.include", "tested.developer_reference.HasSimpleTypes, tested.custom_names.")
            }
        }
    }
}
```

Will match classes `tested.developer_reference.HasSimpleTypes` and `tested.developer_reference.HasSimpleTypesWithDefaults`,
and anything in package `tested.custom_names.` (including nested).

