[gh-pages](../../index.md) / [com.nextfaze.devfun.category](../index.md) / [DeveloperCategory](./index.md)

# DeveloperCategory

`@Target([AnnotationTarget.CLASS]) annotation class DeveloperCategory` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-annotations/src/main/java/com/nextfaze/devfun/category/DeveloperCategory.kt#L153)

This annotation is optional, and is used to change the category's name/order or the group of the functions defined in this class.

At compile time, a [CategoryDefinition](../-category-definition/index.md) will be generated referencing the class with this annotation.

* [Inheritance](#inheritance)
* [Properties](#properties)
  * [Name](#name)
  * [Group](#group)
  * [Order](#order)
* [Contextual Vars](#contextual-vars)
* [Custom Annotations](#custom-annotations)
* [Limitations](#limitations)

# Inheritance

Categories can be specified at the class level and the function-level. Unset properties at the function level will use the class-level
values.

However categories are "keyed" based on their name. Thus if a function declares a category with a different name, it will be considered
a different category and will inherit values based on that one instead.

Note: Due to the way APT orders elements (in that it doesn't) you may get non-deterministic inheritance if you redeclare values over
multiple classes.

i.e. The order of "My Category" will be whatever APT provides first.

``` kotlin
@DeveloperCategory("My Category", order = 10) class SomeClass
@DeveloperCategory("My Category", order = 20) class AnotherClass
```

# Properties

All properties are optional.

## Name

When [value](value.md) is undefined, the name is derived from the class name split by camel case. (e.g. "MyClass" → "My Class").

Categories are merged at runtime by name (see below). They are *case-sensitive* (compared using standard equality checks).
i.e. "MY CATEGORY" ≠ "My Category"

Since they are merged at runtime, declaring a category with the same name on multiple classes will create a single
category with all the functions from those classes.

e.g.
Change the name of a category to "Better Name":

``` kotlin
@DeveloperCategory("Better Name")
class MyClassWithAStupidName {
    @DeveloperFunction
    fun someFunction() = Unit // category="Better Name"
}
```

Merge classes to a single category:

``` kotlin
@DeveloperCategory("My Category")
class SomeClass {
    @DeveloperFunction
    fun someFunction() = Unit // category="My Category"
}

@DeveloperCategory("My Category")
class AnotherClass {
    @DeveloperFunction
    fun anotherFunction() = Unit // category="My Category"
}
```

## Group

Items in a category can be grouped (will be put under the same "group" heading) - this will happen automatically for the context aware
"Context" category.
e.g. "Context" = ["Current Activity" = [...](#), "My Fragment" = [...](#), "Another Fragment" = [...](#)]

A "Misc" group will be created for functions without a group if a category has one or more groups.

e.g.
Grouping functions and overloading parent (class-defined) category definition:

``` kotlin
@DeveloperCategory("My Category", group = "Some Group")
class SomeClass {
    @DeveloperFunction
    fun someFunction() = Unit // category="My Category", group="Some Group"

    @DeveloperFunction(category = DeveloperCategory(group = "Another Group"))
    fun anotherFunction() = Unit // category="My Category", group="Another Group"
}

@DeveloperCategory("My Category", group = "Another Group")
class AnotherClass {
    @DeveloperFunction
    fun anotherFunction() = Unit // category="My Category", group="Another Group"

    @DeveloperFunction(category = DeveloperCategory(group = "Special"))
    fun snowFlake() = Unit // category="My Category", group="Special"
}
```

## Order

By default the category list is ordered by name. The [order](order.md) value can be used to adjust this. Categories with the same order are then
ordered by name. i.e. ordering is by `order` (-∞ → +∞) then by `name`.

This is used by DevFun for the "Context" category (`-10_1000`) and the "Dev Fun" category (`100_000`).

*If the order of a category is defined more than once, the first encountered non-null order value is used.*

# Contextual Vars

*(experimental)* At compile time that follow vars are available for use in [value](value.md) or [group](group.md) (also [DeveloperFunction.value](../../com.nextfaze.devfun.function/-developer-function/value.md)):

* `%CLASS_SN%` → The simple name of the annotated class
* `%CLASS_QN%` → The fully qualified name of the annotated class

e.g.

``` kotlin
@DeveloperCategory("Some Category", group = "Class: %CLASS_SN%")
annotation class MyCategory

@MyCategory
class MyClass {
    @DeveloperFunction
    fun someFun() = Unit // category="Some Category", group="Class: MyClass"
}
```

# Custom Annotations

A "category" annotation is declared with `@DeveloperAnnotation(developerCategory = true)`.

Simply declare the same fields with different defaults (they can also be omitted) to have a "grouping" category or what have you.

For an in-use example see demo annotation [TestCat](https://github.com/NextFaze/dev-fun/tree/master/demo/src/main/java/com/nextfaze/devfun/demo/test/DaggerScopesScreen.kt#L127)

# Limitations

You cannot annotate interfaces at the moment due to the way Kotlin/KAPT handles annotations on functions in interfaces with default
functions. This is intended to be handled in the future - feel free to post an issue or submit a PR.

### Parameters

`value` - The name that to be shown for this category. If undefined the class name will be split by camel case. (e.g. "MyClass" → "My Class")

`group` - Items in this class will be assigned this group.

`order` - Order of this category when listed. Ordering will be by [order](order.md) (-∞ → +∞) then by name ([value](value.md))

**See Also**

[DeveloperAnnotation](../../com.nextfaze.devfun/-developer-annotation/index.md)

[DeveloperFunction](../../com.nextfaze.devfun.function/-developer-function/index.md)

[DeveloperReference](../../com.nextfaze.devfun.reference/-developer-reference/index.md)

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `DeveloperCategory(value: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)` = "", group: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)` = "", order: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)` = 0)`<br>This annotation is optional, and is used to change the category's name/order or the group of the functions defined in this class. |

### Properties

| Name | Summary |
|---|---|
| [group](group.md) | `val group: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Items in this class will be assigned this group. |
| [order](order.md) | `val order: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Order of this category when listed. Ordering will be by [order](order.md) (-∞ → +∞) then by name ([value](value.md)) |
| [value](value.md) | `val value: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The name that to be shown for this category. If undefined the class name will be split by camel case. (e.g. "MyClass" → "My Class") |
