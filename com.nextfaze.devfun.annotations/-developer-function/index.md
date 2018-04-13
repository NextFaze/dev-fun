[gh-pages](../../index.md) / [com.nextfaze.devfun.annotations](../index.md) / [DeveloperFunction](./index.md)

# DeveloperFunction

`@Target([AnnotationTarget.FUNCTION]) annotation class DeveloperFunction` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-annotations/src/main/java/com/nextfaze/devfun/annotations/Annotations.kt#L259)

Functions/methods annotated with this will be shown on the Developer Menu (and other modules).

The function and its containing class can have any visibility:

* `public` and `internal` visibilities will call the function directly
* `package` or `private` the function will be invoked using reflection

At compile time, a [FunctionDefinition](../../com.nextfaze.devfun.core/-function-definition/index.md) will be generated referencing the function with this annotation.

At runtime the [FunctionDefinition](../../com.nextfaze.devfun.core/-function-definition/index.md) will be transformed to one or more [FunctionItem](../../com.nextfaze.devfun.core/-function-item/index.md) via [FunctionTransformer](../../com.nextfaze.devfun.core/-function-transformer/index.md).

## Name

When [value](value.md) is undefined, the name is derived from the function name split by camel case. (e.g. "myFunction" → "My Function").

### Examples

Change the name of a function to "A Slightly Better Name":

``` kotlin
class MyClass {
    @DeveloperFunction("A Slightly Better Name")
    fun A_STUPID_Name() {
        ...
    }
}
```

## Category

Allows specifying and/or overloading the category details for this function *(see [DeveloperCategory](../-developer-category/index.md))*.

### Examples

Specify a different category:

``` kotlin
class MyClass {
    @DeveloperFunction(category = DeveloperCategory("My Class (Animation Utils)"))
    fun startAnimation() {
        ...
    }
}
```

Specify a group for a function:

``` kotlin
class MyClass {
    @DeveloperFunction(category = DeveloperCategory(group = "Special Snow Flake"))
    fun someFunction() {
        ...
    }

    // This will be in the "Misc" group since (one or more) groups will be defined for this category
    @DeveloperFunction
    fun anotherFunction() {
        ...
    }
}
```

*(todo: add "group" value to DeveloperFunction annotation)*

## Require API

When specified, this function will only be visible if the device has this API.

*(todo: use/look for @RequiresApi instead/as well?)*

### Examples

Restrict `M` function:

``` kotlin
class MyClass {
    // Any device below M will not see this function
    @RequiresApi(Build.VERSION_CODES.M)
    @DeveloperFunction(requiresApi = Build.VERSION_CODES.M)
    fun startAnimation() {
        // doing something M related
    }
}
```

## Transformer

Allows overriding how this function will be transformed to a [FunctionItem](../../com.nextfaze.devfun.core/-function-item/index.md). Default transformer is the [SingleFunctionTransformer](../../com.nextfaze.devfun.core/-single-function-transformer/index.md),
which simply converts (effectively just wraps) the [FunctionDefinition](../../com.nextfaze.devfun.core/-function-definition/index.md) to a [FunctionItem](../../com.nextfaze.devfun.core/-function-item/index.md) (1:1).

Item lifecycle:

* [DeveloperFunction](./index.md) → [FunctionDefinition](../../com.nextfaze.devfun.core/-function-definition/index.md) → [FunctionTransformer](../../com.nextfaze.devfun.core/-function-transformer/index.md) → [FunctionItem](../../com.nextfaze.devfun.core/-function-item/index.md) → "Menu Item"

For an in-depth explanation on transformers see [FunctionTransformer](../../com.nextfaze.devfun.core/-function-transformer/index.md).

### Examples

Provide a list of items to automatically fill in and log in as a test account:

``` kotlin
class MyAuthenticateFragment {
    ...

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        signInButton.apply {
            setOnClickListener { attemptLogin() }
        }
    }

    @DeveloperFunction(transformer = SignInFunctionTransformer::class)
    private fun signInAs(email: String, password: String) {
        emailEditText.setText(email)
        passwordEditText.setText(password)
        attemptLogin()
    }
}

private class SignInFunctionTransformer : FunctionTransformer {
    private data class TestAccount(val email: String, val password: String) {
        val title = "Authenticate as $email" // this will be the name of the item - this is effectively @DeveloperFunction("Authenticate as $email")
    }

    private val accounts = if (BuildConfig.DEBUG) { // BuildConfig.DEBUG for dead-code removal
        listOf(TestAccount("foo@example.com", "hello"),
                TestAccount("bar@example.com", "world"))
    } else {
        emptyList()
    }

    override fun apply(functionDefinition: FunctionDefinition, categoryDefinition: CategoryDefinition): List<SimpleFunctionItem> =
            accounts.map {
                object : SimpleFunctionItem(functionDefinition, categoryDefinition) {
                    override val name = it.title
                    override val args = listOf(it.email, it.password) // arguments as expected from signInAs(...)
                }
            }
}
```

### Parameters

`value` - The name that to be shown for this item. If blank the method name will be split by camel case. (e.g. "myFunction" → "My Function")

`category` - Category definition override. Unset fields will be inherited.

`requiresApi` - API required for this item to be visible/processed. Unset or values `<= 0` are ignored.

`transformer` - A transformer class to handle the [FunctionDefinition](../../com.nextfaze.devfun.core/-function-definition/index.md) to [FunctionItem](../../com.nextfaze.devfun.core/-function-item/index.md) processing. Defaults to [SingleFunctionTransformer](../../com.nextfaze.devfun.core/-single-function-transformer/index.md).

**See Also**

[DeveloperCategory](../-developer-category/index.md)

[FunctionTransformer](../../com.nextfaze.devfun.core/-function-transformer/index.md)

[SimpleFunctionItem](../../com.nextfaze.devfun.core/-simple-function-item/index.md)

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `DeveloperFunction(value: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)` = "", category: `[`DeveloperCategory`](../-developer-category/index.md)` = DeveloperCategory(), requiresApi: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)` = 0, transformer: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out `[`FunctionTransformer`](../../com.nextfaze.devfun.core/-function-transformer/index.md)`> = SingleFunctionTransformer::class)`<br>Functions/methods annotated with this will be shown on the Developer Menu (and other modules). |

### Properties

| Name | Summary |
|---|---|
| [category](category.md) | `val category: `[`DeveloperCategory`](../-developer-category/index.md)<br>Category definition override. Unset fields will be inherited. |
| [requiresApi](requires-api.md) | `val requiresApi: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>API required for this item to be visible/processed. Unset or values `<= 0` are ignored. |
| [transformer](transformer.md) | `val transformer: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out `[`FunctionTransformer`](../../com.nextfaze.devfun.core/-function-transformer/index.md)`>`<br>A transformer class to handle the [FunctionDefinition](../../com.nextfaze.devfun.core/-function-definition/index.md) to [FunctionItem](../../com.nextfaze.devfun.core/-function-item/index.md) processing. Defaults to [SingleFunctionTransformer](../../com.nextfaze.devfun.core/-single-function-transformer/index.md). |
| [value](value.md) | `val value: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The name that to be shown for this item. If blank the method name will be split by camel case. (e.g. "myFunction" → "My Function") |
