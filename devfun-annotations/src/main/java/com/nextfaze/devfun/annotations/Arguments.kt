package com.nextfaze.devfun.annotations

import com.nextfaze.devfun.core.FunctionItem
import com.nextfaze.devfun.core.FunctionTransformer
import kotlin.annotation.AnnotationRetention.SOURCE
import kotlin.annotation.AnnotationTarget.FUNCTION
import kotlin.reflect.KClass

/**
 * A function transformer that tells DevFun how to generate functions for annotation-defined arguments.
 *
 * @see DeveloperArguments
 */
interface ArgumentsTransformer : FunctionTransformer

/**
 * An alternative to [DeveloperFunction] that allows you to provide arguments for multiple invocations.
 *
 * _Note: This annotation is *[SOURCE]* retained - its contents will not be kept in your binary!_
 *
 * For an example usage, see demo [AuthenticateFragment.signInAs](https://github.com/NextFaze/dev-fun/tree/master/demo/src/main/java/com/nextfaze/devfun/demo/AuthenticateScreen.kt#L200)
 *
 *
 * # Args Format
 * Each element in the args will be converted to the appropriate type from the string provided based on the function's parameter types.
 * i.e. "1.0f" -> Float
 *
 * ```kotlin
 * @DeveloperArguments(
 *     args = [Args(["A string", "1.0f"])]
 * )
 * fun myFunction(str: String, float: Float) = Unit
 * ```
 * If the second parameter was a string then it would remain a string `"1.0f"` etc.
 *
 *
 * # Supported Types
 * At present only simple types and strings are supported simply because I haven't used anything more - you're welcome to create an issue
 * for other types and it'll likely be supported.
 *
 * If a type is not supported (or an arg is out of bounds) then DevFun will attempt to inject it as per normal DevFun behaviour.
 *
 *
 * # Arg-index [name] &amp; [group]
 * You can use `%d` to reference your args array.
 *
 * i.e.
 * `name = "%2"` -> `name = args[n][2]`
 *
 * e.g.
 * ```kotlin
 * @DeveloperArguments(
 *     name = "The string arg is: '%0'"
 *     args = [
 *         Args(["Hello", "1.0f"]), // name = "The string arg is: 'Hello'"
 *         Args(["World", "-1.0f"]) // name = "The string arg is: 'World'"
 *     ]
 * )
 * fun myFunction(str: String, float: Float) = Unit
 * ```
 *
 *
 * # Example Usage
 * _Taken from demo [AuthenticateFragment.signInAs](https://github.com/NextFaze/dev-fun/tree/master/demo/src/main/java/com/nextfaze/devfun/demo/AuthenticateScreen.kt#L200)_
 *
 * ```kotlin
 * @DeveloperArguments(
 *     args = [
 *         Args(["foo@example.com", "hello", "Normal"]),
 *         Args(["bar@example.com", "world", "Normal"]),
 *         Args(["mary@mailinator.com", "ContainDateNeck76", "Normal"]),
 *         Args(["eli@example.com", "EveningVermontNeck29", "Normal"]),
 *         Args(["trevor@example.com", "OftenJumpCost02", "Normal"]),
 *         Args(["rude.user@example.com", "TakePlayThought95", "Restricted"]),
 *         Args(["block.stars@mailinator.com", "DeviceSeedsSeason16", "Restricted"]),
 *         Args(["vulgar@user.com", "ChinaMisterGeneral11", "Banned"]),
 *         Args(["thirteen@years.old", "my.password", "Underage"]),
 *         Args(["twelve@years.old", "password", "Underage"]),
 *         Args(["bad.password.1@example.com", "D3;d<HF-", "Invalid Password"]),
 *         Args(["bad.password.2@example.com", "r2Z@fMhA", "Invalid Password"]),
 *         Args(["unknown@example.com", "RV[(x43@", "Unknown User"])
 *     ],
 *     group = "Authenticate as %2"
 * )
 * private fun signInAs(email: String, password: String) {
 *     emailEditText.setText(email)
 *     passwordEditText.setText(password)
 *     attemptLogin()
 * }
 * ```
 * - Will result in 13 functions in the Context group when on the authenticate screen.
 * - The functions will be grouped by the 3rd element "Authenticate as %2"
 *
 * Context Menu:
 * - Authenticate as Normal
 *    - foo@example.com
 *    - bar@example.com
 *    - ...
 * - Authenticate as Restricted
 *    - rude.user@example.com
 *    - ...
 *
 *
 * ## Sensitive Information
 * A reminder that `DeveloperArguments` is _SOURCE_ retained - it will not be present in your compiled files.
 *
 * An alternative to having this annotation at the use-site could be to have the declaration in your Debug sources an invoke it from there.
 * ```kotlin
 * object MyDebugUtils {
 *     @DeveloperArguments(...)
 *     fun signInAs(...) {
 *     }
 * }
 * ```
 * An alternative would be to have `signInAs` be called from the `Debug` source tree or by some other means
 *
 *
 * # Experimental Feature
 * __This annotation is experimental.__
 *
 * Any and all input is welcome!
 *
 * @param name The function's name (equivalent to [DeveloperFunction.value]). Defaults to the first element in each args item `%0`. A `null` or blank value falls back [FunctionItem.name].
 * @param args A list of [Args], with each item an alternative invocation. Behaviour is as [FunctionItem.args].
 * @param group A grouping for the functions - defaults to the function's name. A `null` or blank value falls back to [FunctionItem.group].
 * @param requiresApi This is equivalent to [DeveloperFunction.requiresApi].
 * @param transformer This is equivalent to [DeveloperFunction.transformer] - _do not change this unless you know what you're doing!_
 *
 * @see ArgumentsTransformer
 * @see DeveloperFunction
 */
@Retention(SOURCE)
@Target(FUNCTION)
@DeveloperAnnotation(developerFunction = true)
annotation class DeveloperArguments(
    val name: String = "%0",
    val args: Array<Args>,
    val group: String = "%FUN_SN%",
    val category: DeveloperCategory = DeveloperCategory(),
    val requiresApi: Int = 0,
    val transformer: KClass<out FunctionTransformer> = ArgumentsTransformer::class
)

/**
 * Nested annotation for declaring the arguments of a function invocation.
 *
 * @param value The arguments of the invocation. At present only primitive types are supported (`.toType()` will be used on the entry).
 *
 * @see DeveloperArguments
 */
annotation class Args(
    val value: Array<String>
)
