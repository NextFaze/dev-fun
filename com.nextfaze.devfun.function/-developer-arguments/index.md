[gh-pages](../../index.md) / [com.nextfaze.devfun.function](../index.md) / [DeveloperArguments](./index.md)

# DeveloperArguments

`@Target([AnnotationTarget.FUNCTION]) annotation class DeveloperArguments` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-annotations/src/main/java/com/nextfaze/devfun/function/DeveloperArguments.kt#L136)

An alternative to [DeveloperFunction](../-developer-function/index.md) that allows you to provide arguments for multiple invocations.

*Note: This annotation is *[SOURCE](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.annotation/-annotation-retention/-s-o-u-r-c-e/index.html)* retained - its contents will not be kept in your binary!*

For an example usage, see demo [AuthenticateFragment.signInAs](https://github.com/NextFaze/dev-fun/tree/master/demo/src/main/java/com/nextfaze/devfun/demo/AuthenticateScreen.kt#L200)

# Args Format

Each element in the args will be converted to the appropriate type from the string provided based on the function's parameter types.
i.e. "1.0f" -&gt; Float

``` kotlin
@DeveloperArguments(
    args = [Args(["A string", "1.0f"])]
)
fun myFunction(str: String, float: Float) = Unit
```

If the second parameter was a string then it would remain a string `"1.0f"` etc.

# Supported Types

At present only simple types and strings are supported simply because I haven't used anything more - you're welcome to create an issue
for other types and it'll likely be supported.

If a type is not supported (or an arg is out of bounds) then DevFun will attempt to inject it as per normal DevFun behaviour.

# Arg-index [name](name.md) &amp; [group](group.md)

You can use `%d` to reference your args array.

i.e.
`name = "%2"` -&gt; `name = args[n][2]`

e.g.

``` kotlin
@DeveloperArguments(
    name = "The string arg is: '%0'"
    args = [
        Args(["Hello", "1.0f"]), // name = "The string arg is: 'Hello'"
        Args(["World", "-1.0f"]) // name = "The string arg is: 'World'"
    ]
)
fun myFunction(str: String, float: Float) = Unit
```

# Example Usage

*Taken from demo [AuthenticateFragment.signInAs](https://github.com/NextFaze/dev-fun/tree/master/demo/src/main/java/com/nextfaze/devfun/demo/AuthenticateScreen.kt#L200)*

``` kotlin
@DeveloperArguments(
    args = [
        Args(["foo@example.com", "hello", "Normal"]),
        Args(["bar@example.com", "world", "Normal"]),
        Args(["mary@mailinator.com", "ContainDateNeck76", "Normal"]),
        Args(["eli@example.com", "EveningVermontNeck29", "Normal"]),
        Args(["trevor@example.com", "OftenJumpCost02", "Normal"]),
        Args(["rude.user@example.com", "TakePlayThought95", "Restricted"]),
        Args(["block.stars@mailinator.com", "DeviceSeedsSeason16", "Restricted"]),
        Args(["vulgar@user.com", "ChinaMisterGeneral11", "Banned"]),
        Args(["thirteen@years.old", "my.password", "Underage"]),
        Args(["twelve@years.old", "password", "Underage"]),
        Args(["bad.password.1@example.com", "D3;d<HF-", "Invalid Password"]),
        Args(["bad.password.2@example.com", "r2Z@fMhA", "Invalid Password"]),
        Args(["unknown@example.com", "RV[(x43@", "Unknown User"])
    ],
    group = "Authenticate as %2"
)
private fun signInAs(email: String, password: String) {
    emailEditText.setText(email)
    passwordEditText.setText(password)
    attemptLogin()
}
```

* Will result in 13 functions in the Context group when on the authenticate screen.
* The functions will be grouped by the 3rd element "Authenticate as %2"

Context Menu:

* Authenticate as Normal
  * foo@example.com
  * bar@example.com
  * ...
* Authenticate as Restricted
  * rude.user@example.com
  * ...

## Sensitive Information

A reminder that `DeveloperArguments` is *SOURCE* retained - it will not be present in your compiled files.

An alternative to having this annotation at the use-site could be to have the declaration in your Debug sources an invoke it from there.

``` kotlin
object MyDebugUtils {
    @DeveloperArguments(...)
    fun signInAs(...) {
    }
}
```

An alternative would be to have `signInAs` be called from the `Debug` source tree or by some other means

# Experimental Feature

**This annotation is experimental.**

Any and all input is welcome!

### Parameters

`name` - The function's name (equivalent to [DeveloperFunction.value](../-developer-function/value.md)). Defaults to the first element in each args item `%0`. A `null` or blank value falls back [FunctionItem.name](../-function-item/name.md).

`args` - A list of [Args](../-args/index.md), with each item an alternative invocation. Behaviour is as [FunctionItem.args](../-function-item/args.md).

`group` - A grouping for the functions - defaults to the function's name. A `null` or blank value falls back to [FunctionItem.group](../-function-item/group.md).

`requiresApi` - This is equivalent to [DeveloperFunction.requiresApi](../-developer-function/requires-api.md).

`transformer` - This is equivalent to [DeveloperFunction.transformer](../-developer-function/transformer.md) - *do not change this unless you know what you're doing!*

**See Also**

[ArgumentsTransformer](../-arguments-transformer.md)

[DeveloperFunction](../-developer-function/index.md)

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `DeveloperArguments(name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)` = "%0", args: `[`Array`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)`<`[`Args`](../-args/index.md)`>, group: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)` = "%FUN_SN%", category: `[`DeveloperCategory`](../../com.nextfaze.devfun.category/-developer-category/index.md)` = DeveloperCategory(), requiresApi: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)` = 0, transformer: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out `[`FunctionTransformer`](../-function-transformer/index.md)`> = ArgumentsTransformer::class)`<br>An alternative to [DeveloperFunction](../-developer-function/index.md) that allows you to provide arguments for multiple invocations. |

### Properties

| Name | Summary |
|---|---|
| [args](args.md) | `val args: `[`Array`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)`<`[`Args`](../-args/index.md)`>`<br>A list of [Args](../-args/index.md), with each item an alternative invocation. Behaviour is as [FunctionItem.args](../-function-item/args.md). |
| [category](category.md) | `val category: `[`DeveloperCategory`](../../com.nextfaze.devfun.category/-developer-category/index.md) |
| [group](group.md) | `val group: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>A grouping for the functions - defaults to the function's name. A `null` or blank value falls back to [FunctionItem.group](../-function-item/group.md). |
| [name](name.md) | `val name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The function's name (equivalent to [DeveloperFunction.value](../-developer-function/value.md)). Defaults to the first element in each args item `%0`. A `null` or blank value falls back [FunctionItem.name](../-function-item/name.md). |
| [requiresApi](requires-api.md) | `val requiresApi: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>This is equivalent to [DeveloperFunction.requiresApi](../-developer-function/requires-api.md). |
| [transformer](transformer.md) | `val transformer: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out `[`FunctionTransformer`](../-function-transformer/index.md)`>`<br>This is equivalent to [DeveloperFunction.transformer](../-developer-function/transformer.md) - *do not change this unless you know what you're doing!* |
