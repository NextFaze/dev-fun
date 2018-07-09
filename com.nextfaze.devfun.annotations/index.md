[gh-pages](../index.md) / [com.nextfaze.devfun.annotations](./index.md)

## Package com.nextfaze.devfun.annotations

Contains the annotations and interface definitions.

### Types

| Name | Summary |
|---|---|
| [Dagger2Scope](-dagger2-scope/index.md) | `enum class Dagger2Scope`<br>Some range of scopes for use with [Dagger2Component](-dagger2-component/index.md). Priority is based on their ordinal value (higher = broader scope). |
| [PropertyTransformer](-property-transformer.md) | `interface PropertyTransformer : `[`FunctionTransformer`](../com.nextfaze.devfun.core/-function-transformer/index.md)<br>A function transformer that tells DevFun how to render Kotlin properties. |

### Annotations

| Name | Summary |
|---|---|
| [Dagger2Component](-dagger2-component/index.md) | `annotation class Dagger2Component`<br>Annotated functions (`fun`, properties, or property getters (`@get:Dagger2Component`)) will be checked/used as Dagger 2 components. |
| [DeveloperAnnotation](-developer-annotation/index.md) | `annotation class DeveloperAnnotation`<br>Annotation used to by DevFun to "tag" references to some other annotations. |
| [DeveloperCategory](-developer-category/index.md) | `annotation class DeveloperCategory`<br>This annotation is optional, and is used to change the category's name/order or the group of the functions defined in this class. |
| [DeveloperFunction](-developer-function/index.md) | `annotation class DeveloperFunction`<br>Functions/methods annotated with this will be shown on the Developer Menu (and other modules). |
| [DeveloperLogger](-developer-logger/index.md) | `annotation class DeveloperLogger`<br>Annotated references will be rendered as an overlay. |
| [DeveloperProperty](-developer-property/index.md) | `annotation class DeveloperProperty`<br>An annotation that, when used on Kotlin properties, allows DevFun to provide the means of getting/setting properties on the fly. |
