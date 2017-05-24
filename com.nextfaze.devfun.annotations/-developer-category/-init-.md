[gh-pages](../../index.md) / [com.nextfaze.devfun.annotations](../index.md) / [DeveloperCategory](index.md) / [&lt;init&gt;](.)

# &lt;init&gt;

`DeveloperCategory(value: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)` = "", group: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)` = "", order: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)` = 0)`

This annotation is optional, and is used to change the category's name/order or the group of the functions defined in this class.

At compile time, a [CategoryDefinition](../../com.nextfaze.devfun.core/-category-definition/index.md) will be generated referencing the class with this annotation.

## Name

When [value](-init-.md#com.nextfaze.devfun.annotations.DeveloperCategory$<init>(kotlin.String, kotlin.String, kotlin.Int)/value) is undefined, the name is derived from the class name split by camel case. (e.g. "MyClass" → "My Class").

Categories are merged at runtime by name (see [Category Merging](#Category-Merging) below). They are *case-sensitive*
(compared using standard equality checks). i.e. "MY CATEGORY" ≠ "My Category"

Since they are merged at runtime, declaring a category with the same name on multiple classes will create a single
category with all the functions from those classes.

### Examples

Change the name of a category to "Better Name":

``` kotlin
@DeveloperCategory("Better Name")
class MyClassWithAStupidName {
    @DeveloperFunction
    fun someFunction() {
        ...
    }
}
```

Merge classes to a single category:

``` kotlin
@DeveloperCategory("My Category", group = "In Stupid Name")
class MyClassWithAStupidName {
    // This will be put in the "My Category" > "In Stupid Name" group
    @DeveloperFunction
    fun someFunction() {
        ...
    }
}

@DeveloperCategory("My Category", group = "Another Class")
class AnotherClass {
    // This will be put in the "My Category" > "Another Class" group
    @DeveloperFunction
    fun anotherFunction() {
        ...
    }

    // This will be put in the "My Category" > "Special" group
    @DeveloperFunction(category = DeveloperCategory(group = "Special"))
    fun snowFlake() {
        ...
    }
}


## Group
Items in a category can be grouped (will be put under the same "group" heading) - this will happen automatically for
the context aware "Context" category. e.g. "Context" = ["Current Activity" = [...], "My Fragment" = [...], "Another Fragment" = [...]]

A "Misc" group is used for functions without a group if a category has one or more groups.

### Examples
Grouping functions and overloading parent (class-defined) category definition:
```kotlin
@DeveloperCategory("My Category", group = "In Stupid Name")
class MyClassWithAStupidName {
    // This will be put in the "My Category" > "In Stupid Name" group
    @DeveloperFunction
    fun someFunction() {
        ...
    }

    // This will be put in the "My Category" > "Also In Stupid Name" group
    @DeveloperFunction(category = DeveloperCategory(group = "Also In Stupid Name"))
    fun anotherFunction() {
        ...
    }
}


## Order
By default the category list is ordered by name. The [order] value can be used to adjust this. Categories with the
same order are then ordered by name. i.e. ordering is by `order` (-∞ → +∞) then by `name`

This is used by DevFun for the "Context" category (`-10_1000`) and the "Dev Fun" category (`10_000`).

_If the order of a category is defined more than once, the first encountered non-null order value is used._


## Limitations
Some use-site restrictions are present due to the way KAPT handles annotations for functions in components and
interfaces with default functions (non-trivial weird edge cases).
- Not usable on interfaces
- Not usable on annotations

Additionally inheritance is not yet considered.

These are intended to be handled in the future.

@see DeveloperFunction

@param value The name that to be shown for this category. If undefined the class name will be split by camel case. (e.g. "MyClass" → "My Class")
@param group Items in this class will be assigned this group.
@param order Order of this category when listed. Ordering will be by [order] (-∞ → +∞) then by name ([value])
```

