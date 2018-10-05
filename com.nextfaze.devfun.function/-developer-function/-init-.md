[gh-pages](../../index.md) / [com.nextfaze.devfun.function](../index.md) / [DeveloperFunction](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`DeveloperFunction(value: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)` = "", category: `[`DeveloperCategory`](../../com.nextfaze.devfun.category/-developer-category/index.md)` = DeveloperCategory(), requiresApi: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)` = 0, transformer: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out `[`FunctionTransformer`](../-function-transformer/index.md)`> = SingleFunctionTransformer::class)`

Functions/methods annotated with this will be shown on the Developer Menu (and other modules).

The function and its containing class can have any visibility:

* `public` and `internal` visibilities will call the function directly
* `package` or `private` the function will be invoked using reflection

At compile time, a [FunctionDefinition](../-function-definition/index.md) will be generated referencing the function with this annotation.

At runtime the [FunctionDefinition](../-function-definition/index.md) will be transformed to one or more [FunctionItem](../-function-item/index.md) via [FunctionTransformer](../-function-transformer/index.md).

* [Properties](#properties)
  * [Name](#name)
  * [Category](#category)
  * [Requires API](#requires-api)
  * [Transformer](#transformer)
* [Contextual Vars](#contextual-vars)
* [Limitations](#limitations)

# Properties

All properties are optional.

## Name

When [value](value.md) is undefined, the name is derived from the function name split by camel case. (e.g. "myFunction" → "My Function").

e.g.
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

The [category](category.md) property allows specifying and/or overloading the category details for this function *(see [DeveloperCategory](../../com.nextfaze.devfun.category/-developer-category/index.md))*.

e.g.
Specify a different category:

``` kotlin
class MyClass {
    @DeveloperFunction(category = DeveloperCategory("My Class (Animation Utils)"))
    fun startAnimation() = Unit // category="My Class (Animation Utils)"
}
```

Specify a group for a function:

``` kotlin
class MyClass {
    @DeveloperFunction(category = DeveloperCategory(group = "Special Snow Flake"))
    fun someFunction() = Unit // category="My Class", group="Special Snow Flake"

    @DeveloperFunction
    fun anotherFunction() = Unit // category="My Class", group="Misc" (as one or more groups are present)
}
```

*(todo: add "group" value to DeveloperFunction annotation?)*

## Requires API

When [requiresApi](requires-api.md) is specified this function will only be visible/referenced if the device meets the requirements.

*(todo: use/look for @RequiresApi instead/as well?)*

e.g.
Restrict `M` function:

``` kotlin
class MyClass {
    // Any device below M will not see this function and DevFun wont invoke any transformers upon it
    @RequiresApi(Build.VERSION_CODES.M)
    @DeveloperFunction(requiresApi = Build.VERSION_CODES.M)
    fun startAnimation() {
        // doing something M related
    }
}
```

## Transformer

The [transformer](transformer.md) tells DevFun how to generate one or more [FunctionItem](../-function-item/index.md) from this function's [FunctionDefinition](../-function-definition/index.md).

The default transformer is [SingleFunctionTransformer](../-single-function-transformer/index.md) which effectively just wraps the [FunctionDefinition](../-function-definition/index.md) to a [FunctionItem](../-function-item/index.md) (1:1).

Item lifecycle:

* [DeveloperFunction](index.md) → [FunctionDefinition](../-function-definition/index.md) → [FunctionTransformer](../-function-transformer/index.md) → [FunctionItem](../-function-item/index.md) → "Menu Item"

For an in-depth explanation on transformers see [FunctionTransformer](../-function-transformer/index.md).

e.g.
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
        listOf(
            TestAccount("foo@example.com", "hello"),
            TestAccount("bar@example.com", "world")
        )
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

The above transformer can also be achieved using @[DeveloperArguments](../-developer-arguments/index.md).

``` kotlin
@DeveloperArguments(
    name = "Authenticate as %0",
    args = [
        Args(["foo@example.com", "hello"]),
        Args(["bar@example.com", "world"])
    ]
)
fun signInAs(...)
```

# Contextual Vars

*(experimental)* At compile time the follow vars are available for use in [value](value.md) (also [DeveloperCategory.value](../../com.nextfaze.devfun.category/-developer-category/value.md) and [DeveloperCategory.group](../../com.nextfaze.devfun.category/-developer-category/group.md)):

* `%CLASS_SN%` → The simple name of the class
* `%CLASS_QN%` → The fully qualified name of the class
* `%FUN_SN%` → The simple name of the annotated function
* `%FUN_QN%` → The qualified name of the annotated function. "fun myFunction(param1: String)" becomes "myFunction(java.lang.String)"

e.g.

``` kotlin
class MyClass {
    @DeveloperFunction("I am in %CLASS_SN%")
    fun someFun() = Unit // name="I am in MyClass"
}
```

# Limitations

* Annotations on functions in interfaces is not supported at the moment due to the way Kotlin/KAPT handles default functions/args. This is
intended to be permissible in the future.

* Unfortunately the [transformer](transformer.md) class must be in the same source tree as the declaration site. Looking into ways to change this but it
may not be trivial/possible by the nature of Javac and annotation processing.

### Parameters

`value` - The name that to be shown for this item. If blank the method name will be split by camel case. (e.g. "myFunction" → "My Function")

`category` - Category definition override. Unset fields will be inherited.

`requiresApi` - API required for this item to be visible/processed. Unset or values `<= 0` are ignored.

`transformer` - A transformer class to handle the [FunctionDefinition](../-function-definition/index.md) to [FunctionItem](../-function-item/index.md) processing. Defaults to [SingleFunctionTransformer](../-single-function-transformer/index.md).

**See Also**

[DeveloperCategory](../../com.nextfaze.devfun.category/-developer-category/index.md)

[FunctionTransformer](../-function-transformer/index.md)

[SimpleFunctionItem](../-simple-function-item/index.md)

