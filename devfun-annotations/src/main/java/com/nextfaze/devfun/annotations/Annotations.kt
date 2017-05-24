package com.nextfaze.devfun.annotations

import com.nextfaze.devfun.core.*
import kotlin.annotation.AnnotationRetention.SOURCE
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.annotation.AnnotationTarget.FUNCTION
import kotlin.reflect.KClass

/**
 * This annotation is optional, and is used to change the category's name/order or the group of the functions defined in this class.
 *
 * At compile time, a [CategoryDefinition] will be generated referencing the class with this annotation.
 *
 *
 * ## Name
 * When [value] is undefined, the name is derived from the class name split by camel case. (e.g. "MyClass" → "My Class").
 *
 * Categories are merged at runtime by name (see [Category Merging](#Category-Merging) below). They are *case-sensitive*
 * (compared using standard equality checks). i.e. "MY CATEGORY" ≠ "My Category"
 *
 * Since they are merged at runtime, declaring a category with the same name on multiple classes will create a single
 * category with all the functions from those classes.
 *
 * ### Examples
 * Change the name of a category to "Better Name":
 * ```kotlin
 * @DeveloperCategory("Better Name")
 * class MyClassWithAStupidName {
 *     @DeveloperFunction
 *     fun someFunction() {
 *         ...
 *     }
 * }
 * ```
 *
 * Merge classes to a single category:
 * ```kotlin
 * @DeveloperCategory("My Category", group = "In Stupid Name")
 * class MyClassWithAStupidName {
 *     // This will be put in the "My Category" > "In Stupid Name" group
 *     @DeveloperFunction
 *     fun someFunction() {
 *         ...
 *     }
 * }
 *
 * @DeveloperCategory("My Category", group = "Another Class")
 * class AnotherClass {
 *     // This will be put in the "My Category" > "Another Class" group
 *     @DeveloperFunction
 *     fun anotherFunction() {
 *         ...
 *     }
 *
 *     // This will be put in the "My Category" > "Special" group
 *     @DeveloperFunction(category = DeveloperCategory(group = "Special"))
 *     fun snowFlake() {
 *         ...
 *     }
 * }
 *
 *
 * ## Group
 * Items in a category can be grouped (will be put under the same "group" heading) - this will happen automatically for
 * the context aware "Context" category. e.g. "Context" = ["Current Activity" = [...], "My Fragment" = [...], "Another Fragment" = [...]]
 *
 * A "Misc" group is used for functions without a group if a category has one or more groups.
 *
 * ### Examples
 * Grouping functions and overloading parent (class-defined) category definition:
 * ```kotlin
 * @DeveloperCategory("My Category", group = "In Stupid Name")
 * class MyClassWithAStupidName {
 *     // This will be put in the "My Category" > "In Stupid Name" group
 *     @DeveloperFunction
 *     fun someFunction() {
 *         ...
 *     }
 *
 *     // This will be put in the "My Category" > "Also In Stupid Name" group
 *     @DeveloperFunction(category = DeveloperCategory(group = "Also In Stupid Name"))
 *     fun anotherFunction() {
 *         ...
 *     }
 * }
 *
 *
 * ## Order
 * By default the category list is ordered by name. The [order] value can be used to adjust this. Categories with the
 * same order are then ordered by name. i.e. ordering is by `order` (-∞ → +∞) then by `name`
 *
 * This is used by DevFun for the "Context" category (`-10_1000`) and the "Dev Fun" category (`10_000`).
 *
 * _If the order of a category is defined more than once, the first encountered non-null order value is used._
 *
 *
 * ## Limitations
 * Some use-site restrictions are present due to the way KAPT handles annotations for functions in components and
 * interfaces with default functions (non-trivial weird edge cases).
 * - Not usable on interfaces
 * - Not usable on annotations
 *
 * Additionally inheritance is not yet considered.
 *
 * These are intended to be handled in the future.
 *
 * @see DeveloperFunction
 *
 * @param value The name that to be shown for this category. If undefined the class name will be split by camel case. (e.g. "MyClass" → "My Class")
 * @param group Items in this class will be assigned this group.
 * @param order Order of this category when listed. Ordering will be by [order] (-∞ → +∞) then by name ([value])
 */
@Target(CLASS)
@Retention(SOURCE)
annotation class DeveloperCategory(val value: String = "",
                                   val group: String = "",
                                   val order: Int = 0)

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
 *
 * ## Name
 * When [value] is undefined, the name is derived from the function name split by camel case. (e.g. "myFunction" → "My Function").
 *
 * ### Examples
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
 * Allows specifying and/or overloading the category details for this function _(see [DeveloperCategory])_.
 *
 * ### Examples
 * Specify a different category:
 * ```kotlin
 * class MyClass {
 *     @DeveloperFunction(category = DeveloperCategory("My Class (Animation Utils)"))
 *     fun startAnimation() {
 *         ...
 *     }
 * }
 * ```
 *
 * Specify a group for a function:
 * ```kotlin
 * class MyClass {
 *     @DeveloperFunction(category = DeveloperCategory(group = "Special Snow Flake"))
 *     fun someFunction() {
 *         ...
 *     }
 *
 *     // This will be in the "Misc" group since (one or more) groups will be defined for this category
 *     @DeveloperFunction
 *     fun anotherFunction() {
 *         ...
 *     }
 * }
 * ```
 *
 * _(todo: add "group" value to DeveloperFunction annotation)_
 *
 * ## Require API
 * When specified, this function will only be visible if the device has this API.
 *
 * _(todo: use/look for @RequiresApi instead/as well?)_
 *
 * ### Examples
 * Restrict `M` function:
 * ```kotlin
 * class MyClass {
 *     // Any device below M will not see this function
 *     @RequiresApi(Build.VERSION_CODES.M)
 *     @DeveloperFunction(requiresApi = Build.VERSION_CODES.M)
 *     fun startAnimation() {
 *         // doing something M related
 *     }
 * }
 * ```
 *
 * ## Transformer
 * Allows overriding how this function will be transformed to a [FunctionItem]. Default transformer is the [SingleFunctionTransformer],
 * which simply converts (effectively just wraps) the [FunctionDefinition] to a [FunctionItem] (1:1).
 *
 * Item lifecycle:
 * - [DeveloperFunction] → [FunctionDefinition] → [FunctionTransformer] → [FunctionItem] → "Menu Item"
 *
 * For an in-depth explanation on transformers see [FunctionTransformer].
 *
 * ### Examples
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
 *         listOf(TestAccount("foo@example.com", "hello"),
 *                 TestAccount("bar@example.com", "world"))
 *     } else {
 *         emptyList()
 *     }
 *
 *     override fun apply(functionDefinition: FunctionDefinition, categoryDefinition: CategoryDefinition): List<SimpleFunctionItem> =
 *             accounts.map {
 *                 object : SimpleFunctionItem(functionDefinition, categoryDefinition) {
 *                     override val name = it.title
 *                     override val args = listOf(it.email, it.password) // arguments as expected from signInAs(...)
 *                 }
 *             }
 * }
 * ```
 *
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
annotation class DeveloperFunction(val value: String = "",
                                   val category: DeveloperCategory = DeveloperCategory(),
                                   val requiresApi: Int = 0,
                                   val transformer: KClass<out FunctionTransformer> = SingleFunctionTransformer::class)
