package com.nextfaze.devfun.annotations

import com.nextfaze.devfun.core.*
import kotlin.annotation.AnnotationRetention.BINARY
import kotlin.annotation.AnnotationRetention.SOURCE
import kotlin.annotation.AnnotationTarget.*
import kotlin.reflect.KClass

/**
 * This annotation is optional, and is used to change the category's name/order or the group of the functions defined in this class.
 *
 * At compile time, a [CategoryDefinition] will be generated referencing the class with this annotation.
 *
 * - [Inheritance](#inheritance)
 * - [Meta Annotations](#meta-annotations)
 * - [Properties](#properties)
 *     - [Name](#name)
 *     - [Group](#group)
 *     - [Order](#order)
 * - [Contextual Vars](#contextual-vars)
 * - [Limitations](#limitations)
 *
 * # Inheritance
 * Categories can be specified at the class level and the function-level. Unset properties at the function level will use the class-level
 * values.
 *
 * However categories are "keyed" based on their name. Thus if a function declares a category with a different name, it will be considered
 * a different category and will inherit values based on that one instead.
 *
 * Note: Due to the way APT orders elements (in that it doesn't) you may get non-deterministic inheritance if you redeclare values over
 * multiple classes.
 *
 * i.e. The order of "My Category" will be whatever APT provides first.
 * ```kotlin
 * @DeveloperCategory("My Category", order = 10) class SomeClass
 * @DeveloperCategory("My Category", order = 20) class AnotherClass
 * ```
 *
 * # Meta Annotations
 * When placed on an annotation, said annotation will act as a category definition. It can be further configured at the use-site with
 * values inheriting as normal.
 *
 * Properties declared on the meta annotation that match the declaration of a property of `CategoryDefinition` will be also be used.
 *
 * e.g.
 * ```kotlin
 * @DeveloperCategory("My Category")
 * annotation class MyCategory {
 *     val group: String = "Default Group" // custom group property will be used
 * }
 *
 * @MyCategory
 * class SomeClass {
 *     @DeveloperFunction
 *     fun fun1() = Unit // category="My Category", group="Default Group"
 *
 *     @DeveloperFunction(category = DeveloperCategory(group = "Another Group"))
 *     fun fun2() = Unit // category="My Category", group="Another Group"
 * }
 *
 * @MyCategory(group = "Some Group")
 * class AnotherClass {
 *     @DeveloperFunction
 *     fun fun1() = Unit // category="My Category", group="Some Group"
 *
 *     @DeveloperFunction(category = DeveloperCategory(group = "YAG"))
 *     fun fun2() = Unit // category="My Category", group="YAG"
 * }
 * ```
 *
 * For an in-use example see demo annotation [TestCat](https://github.com/NextFaze/dev-fun/tree/master/demo/src/main/java/com/nextfaze/devfun/demo/test/DaggerScopesScreen.kt#L123)
 *
 * # Properties
 * All properties are optional.
 *
 * ## Name
 * When [value] is undefined, the name is derived from the class name split by camel case. (e.g. "MyClass" → "My Class").
 *
 * Categories are merged at runtime by name (see below). They are *case-sensitive* (compared using standard equality checks).
 * i.e. "MY CATEGORY" ≠ "My Category"
 *
 * Since they are merged at runtime, declaring a category with the same name on multiple classes will create a single
 * category with all the functions from those classes.
 *
 * e.g.
 * Change the name of a category to "Better Name":
 * ```kotlin
 * @DeveloperCategory("Better Name")
 * class MyClassWithAStupidName {
 *     @DeveloperFunction
 *     fun someFunction() = Unit // category="Better Name"
 * }
 * ```
 *
 * Merge classes to a single category:
 * ```kotlin
 * @DeveloperCategory("My Category")
 * class SomeClass {
 *     @DeveloperFunction
 *     fun someFunction() = Unit // category="My Category"
 * }
 *
 * @DeveloperCategory("My Category")
 * class AnotherClass {
 *     @DeveloperFunction
 *     fun anotherFunction() = Unit // category="My Category"
 * }
 * ```
 *
 * ## Group
 * Items in a category can be grouped (will be put under the same "group" heading) - this will happen automatically for the context aware
 * "Context" category.
 * e.g. "Context" = ["Current Activity" = [...], "My Fragment" = [...], "Another Fragment" = [...]]
 *
 * A "Misc" group will be created for functions without a group if a category has one or more groups.
 *
 * e.g.
 * Grouping functions and overloading parent (class-defined) category definition:
 * ```kotlin
 * @DeveloperCategory("My Category", group = "Some Group")
 * class SomeClass {
 *     @DeveloperFunction
 *     fun someFunction() = Unit // category="My Category", group="Some Group"
 *
 *     @DeveloperFunction(category = DeveloperCategory(group = "Another Group"))
 *     fun anotherFunction() = Unit // category="My Category", group="Another Group"
 * }
 *
 * @DeveloperCategory("My Category", group = "Another Group")
 * class AnotherClass {
 *     @DeveloperFunction
 *     fun anotherFunction() = Unit // category="My Category", group="Another Group"
 *
 *     @DeveloperFunction(category = DeveloperCategory(group = "Special"))
 *     fun snowFlake() = Unit // category="My Category", group="Special"
 * }
 * ```
 *
 * ## Order
 * By default the category list is ordered by name. The [order] value can be used to adjust this. Categories with the same order are then
 * ordered by name. i.e. ordering is by `order` (-∞ → +∞) then by `name`.
 *
 * This is used by DevFun for the "Context" category (`-10_1000`) and the "Dev Fun" category (`100_000`).
 *
 * _If the order of a category is defined more than once, the first encountered non-null order value is used._
 *
 * # Contextual Vars
 * _(experimental)_ At compile time that follow vars are available for use in [value] or [group] (also [DeveloperFunction.value]):
 * - `%CLASS_SN%` → The simple name of the annotated class
 * - `%CLASS_QN%` → The fully qualified name of the annotated class
 *
 * e.g.
 * ```kotlin
 * @DeveloperCategory("Some Category", group = "Class: %CLASS_SN%")
 * annotation class MyCategory
 *
 * @MyCategory
 * class MyClass {
 *     @DeveloperFunction
 *     fun someFun() = Unit // category="Some Category", group="Class: MyClass"
 * }
 * ```
 *
 * # Limitations
 * You cannot annotate interfaces at the moment due to the way Kotlin/KAPT handles annotations on functions in interfaces with default
 * functions. This is intended to be handled in the future - feel free to post an issue or submit a PR.
 *
 * @param value The name that to be shown for this category. If undefined the class name will be split by camel case. (e.g. "MyClass" → "My Class")
 * @param group Items in this class will be assigned this group.
 * @param order Order of this category when listed. Ordering will be by [order] (-∞ → +∞) then by name ([value])
 *
 * @see DeveloperFunction
 */
@Target(CLASS)
@Retention(SOURCE)
@DeveloperAnnotation(developerCategory = true)
annotation class DeveloperCategory(
    val value: String = "",
    val group: String = "",
    val order: Int = 0
)

/**
 * Functions/methods annotated with this will be shown on the Developer Menu (and other modules).
 *
 * The function and its containing class can have any visibility:
 * - `public` and `internal` visibilities will call the function directly
 * - `package` or `private` the function will be invoked using reflection
 *
 * At compile time, a [FunctionDefinition] will be generated referencing the function with this annotation.
 *
 * At runtime the [FunctionDefinition] will be transformed to one or more [FunctionItem] via [FunctionTransformer].
 *
 * - [Properties](#properties)
 *     - [Name](#name)
 *     - [Category](#category)
 *     - [Requires API](#requires-api)
 *     - [Transformer](#transformer)
 * - [Contextual Vars](#contextual-vars)
 * - [Limitations](#limitations)
 *
 * # Properties
 * All properties are optional.
 *
 * ## Name
 * When [value] is undefined, the name is derived from the function name split by camel case. (e.g. "myFunction" → "My Function").
 *
 * e.g.
 * Change the name of a function to "A Slightly Better Name":
 * ```kotlin
 * class MyClass {
 *     @DeveloperFunction("A Slightly Better Name")
 *     fun A_STUPID_Name() {
 *         ...
 *     }
 * }
 * ```
 *
 * ## Category
 * The [category] property allows specifying and/or overloading the category details for this function _(see [DeveloperCategory])_.
 *
 * e.g.
 * Specify a different category:
 * ```kotlin
 * class MyClass {
 *     @DeveloperFunction(category = DeveloperCategory("My Class (Animation Utils)"))
 *     fun startAnimation() = Unit // category="My Class (Animation Utils)"
 * }
 * ```
 *
 * Specify a group for a function:
 * ```kotlin
 * class MyClass {
 *     @DeveloperFunction(category = DeveloperCategory(group = "Special Snow Flake"))
 *     fun someFunction() = Unit // category="My Class", group="Special Snow Flake"
 *
 *     @DeveloperFunction
 *     fun anotherFunction() = Unit // category="My Class", group="Misc" (as one or more groups are present)
 * }
 * ```
 *
 * _(todo: add "group" value to DeveloperFunction annotation?)_
 *
 * ## Requires API
 * When [requiresApi] is specified this function will only be visible/referenced if the device meets the requirements.
 *
 * _(todo: use/look for @RequiresApi instead/as well?)_
 *
 * e.g.
 * Restrict `M` function:
 * ```kotlin
 * class MyClass {
 *     // Any device below M will not see this function and DevFun wont invoke any transformers upon it
 *     @RequiresApi(Build.VERSION_CODES.M)
 *     @DeveloperFunction(requiresApi = Build.VERSION_CODES.M)
 *     fun startAnimation() {
 *         // doing something M related
 *     }
 * }
 * ```
 *
 * ## Transformer
 * The [transformer] tells DevFun how to generate one or more [FunctionItem] from this function's [FunctionDefinition].
 *
 * The default transformer is [SingleFunctionTransformer] which effectively just wraps the [FunctionDefinition] to a [FunctionItem] (1:1).
 *
 * Item lifecycle:
 * - [DeveloperFunction] → [FunctionDefinition] → [FunctionTransformer] → [FunctionItem] → "Menu Item"
 *
 * For an in-depth explanation on transformers see [FunctionTransformer].
 *
 * e.g.
 * Provide a list of items to automatically fill in and log in as a test account:
 * ```kotlin
 * class MyAuthenticateFragment {
 *     ...
 *
 *     override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
 *         signInButton.apply {
 *             setOnClickListener { attemptLogin() }
 *         }
 *     }
 *
 *     @DeveloperFunction(transformer = SignInFunctionTransformer::class)
 *     private fun signInAs(email: String, password: String) {
 *         emailEditText.setText(email)
 *         passwordEditText.setText(password)
 *         attemptLogin()
 *     }
 * }
 *
 * private class SignInFunctionTransformer : FunctionTransformer {
 *     private data class TestAccount(val email: String, val password: String) {
 *         val title = "Authenticate as $email" // this will be the name of the item - this is effectively @DeveloperFunction("Authenticate as $email")
 *     }
 *
 *     private val accounts = if (BuildConfig.DEBUG) { // BuildConfig.DEBUG for dead-code removal
 *         listOf(
 *             TestAccount("foo@example.com", "hello"),
 *             TestAccount("bar@example.com", "world")
 *         )
 *     } else {
 *         emptyList()
 *     }
 *
 *     override fun apply(functionDefinition: FunctionDefinition, categoryDefinition: CategoryDefinition): List<SimpleFunctionItem> =
 *         accounts.map {
 *             object : SimpleFunctionItem(functionDefinition, categoryDefinition) {
 *                 override val name = it.title
 *                 override val args = listOf(it.email, it.password) // arguments as expected from signInAs(...)
 *             }
 *         }
 * }
 * ```
 *
 * # Contextual Vars
 * _(experimental)_ At compile time the follow vars are available for use in [value] (also [DeveloperCategory.value] and [DeveloperCategory.group]):
 * - `%CLASS_SN%` → The simple name of the class
 * - `%CLASS_QN%` → The fully qualified name of the class
 * - `%FUN_SN%` → The simple name of the annotated function
 * - `%FUN_QN%` → The qualified name of the annotated function. "fun myFunction(param1: String)" becomes "myFunction(java.lang.String)"
 *
 * e.g.
 * ```kotlin
 * class MyClass {
 *     @DeveloperFunction("I am in %CLASS_SN%")
 *     fun someFun() = Unit // name="I am in MyClass"
 * }
 * ```
 *
 * # Limitations
 * - Annotations on functions in interfaces is not supported at the moment due to the way Kotlin/KAPT handles default functions/args. This is
 * intended to be permissible in the future.
 *
 * - Unfortunately the [transformer] class must be in the same source tree as the declaration site. Looking into ways to change this but it
 * may not be trivial/possible by the nature of Javac and annotation processing.
 *
 * @param value The name that to be shown for this item. If blank the method name will be split by camel case. (e.g. "myFunction" → "My Function")
 * @param category Category definition override. Unset fields will be inherited.
 * @param requiresApi API required for this item to be visible/processed. Unset or values `<= 0` are ignored.
 * @param transformer A transformer class to handle the [FunctionDefinition] to [FunctionItem] processing. Defaults to [SingleFunctionTransformer].
 *
 * @see DeveloperCategory
 * @see FunctionTransformer
 * @see SimpleFunctionItem
 */
@Target(FUNCTION)
@Retention(SOURCE)
@DeveloperAnnotation(developerFunction = true)
annotation class DeveloperFunction(
    val value: String = "",
    val category: DeveloperCategory = DeveloperCategory(),
    val requiresApi: Int = 0,
    val transformer: KClass<out FunctionTransformer> = SingleFunctionTransformer::class
)

/**
 * Annotated elements will be recorded by DevFun for later retrieval via `devFun.developerReferences<DeveloperReference>()`.
 *
 * In general this is not used much beyond testing.
 *
 * Typically usage of this type is via your own custom annotations with [DeveloperAnnotation] à la [DeveloperLogger].
 *
 * @see DeveloperAnnotation
 * @see Dagger2Component
 * @see DeveloperLogger
 */
@Retention(SOURCE)
@DeveloperAnnotation(developerReference = true)
annotation class DeveloperReference

/**
 * Annotation used to by DevFun to "tag" references to some other annotations.
 *
 * By default usages of the annotated annotations result in generated [ReferenceDefinition] instances.
 *
 * However, if [developerFunction] is set to `true` then the compiler will treat it as if it was an @[DeveloperFunction] annotation.
 * In this state the compiler will check for the same fields of `@DeveloperFunction`.
 *
 * If you have different defaults defined compared to [DeveloperFunction] then these values will be written as if you had used
 * `@DeveloperFunction(field = value)` at the declaration site - this behaviour is somewhat experimental. Please report any issues you have.
 *
 * An example of this can be seen with @[DeveloperProperty].
 *
 * # Custom Properties
 * If your annotation declares custom properties, these will be serialized and available at run-time during function transformation.
 *
 * Function definitions will implement [ReferenceDefinition] with the properties available as a map via [ReferenceDefinition.properties].
 *
 * ## Contextual Vars
 * _(experimental)_ At compile time the follow vars are available for use in `String` properties:
 * - `%CLASS_SN%` → The simple name of the class
 * - `%CLASS_QN%` → The fully qualified name of the class
 * - `%FUN_SN%` → The simple name of the annotated function
 * - `%FUN_QN%` → The qualified name of the annotated function. "fun myFunction(param1: String)" becomes "myFunction(java.lang.String)"
 * - `%VAR_SN%` → The simple name of the annotated variable
 * - `%VAR_QN%` → The qualified name of the annotated variable
 *
 * e.g.
 * ```kotlin
 * class MyClass {
 *     @DeveloperFunction("I am in %CLASS_SN%")
 *     fun someFun() = Unit // name="I am in MyClass"
 * }
 * ```
 *
 * @param developerFunction Set to `true` to have the compiler treat the annotation as a @[DeveloperFunction]. _(experimental)_
 *
 * @see Dagger2Component
 * @see DeveloperProperty
 * @see DeveloperLogger
 * @see DeveloperArguments
 */
@Retention(BINARY)
@Target(ANNOTATION_CLASS)
annotation class DeveloperAnnotation(
    val developerFunction: Boolean = false,
    val developerCategory: Boolean = false,
    val developerReference: Boolean = false
)
