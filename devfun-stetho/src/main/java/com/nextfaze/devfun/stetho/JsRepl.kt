package com.nextfaze.devfun.stetho

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.facebook.stetho.inspector.console.RuntimeReplFactory
import com.facebook.stetho.rhino.JsRuntimeReplFactoryBuilder
import com.nextfaze.devfun.category.CategoryItem
import com.nextfaze.devfun.core.ActivityProvider
import com.nextfaze.devfun.core.DevFun
import com.nextfaze.devfun.core.call
import com.nextfaze.devfun.function.FunctionItem
import com.nextfaze.devfun.internal.exception.stackTraceAsString
import com.nextfaze.devfun.internal.log.*
import org.mozilla.javascript.BaseFunction
import org.mozilla.javascript.Function
import org.mozilla.javascript.Script
import org.mozilla.javascript.Scriptable
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

private val handler = Handler(Looper.getMainLooper())

internal typealias JsContext = org.mozilla.javascript.Context

private val log = logger("JsRepl")

internal fun generateFunctionName(category: CategoryItem, function: FunctionItem) =
    "${category.name}_${function.name}".replace(LEGAL_CHARACTERS, "_")

fun buildJsRuntime(context: Context, activityProvider: ActivityProvider, devFun: DevFun): RuntimeReplFactory =
    TrackingJsRuntimeReplFactoryBuilder(context).apply {
        importPackage("android.os")
        importPackage("android.view")
        importPackage("android.widget")

        addVariable("handler", handler)

        addFunction("post", jsFunction(invokeOnMain = false) { cx, scope, _, args ->
            val func = args.getOrNull(0) as? Script ?: return@jsFunction JsContext.reportRuntimeError("Argument is not a Script")
            scope.put("func", scope, func)
            log.d { "func=$func" }
            return@jsFunction cx.evaluateString(scope, "handler.post(func);", "uiThread.js", 1, null)
        })

        addFunction("viewById", jsArgsFunction { args ->
            val activity = activityProvider() ?: return@jsArgsFunction JsContext.reportRuntimeError("Activity is null")

            val idString = args.getOrNull(0) as? String
            if (idString == null || idString.isBlank()) {
                return@jsArgsFunction JsContext.reportRuntimeError("viewById requires single resource identifier.\nCan be as name or fully qualified.\ne.g. helloWorldTextView, R.id.helloWorldTextView, com.package.R.id.helloWorldTextView, etc.")
            }

            // Input could be:
            // drawer_layout
            // R.id.drawer_layout
            // com.package.R.id.drawer_layout

            val resPackage: String
            val resName: String
            val rDotId = idString.indexOf("R.id.")
            when {
                rDotId < 0 -> { // Format: name
                    resPackage = context.packageName
                    resName = idString
                }
                rDotId == 0 -> { // Format: R.id.name
                    resPackage = context.packageName
                    resName = idString.substring(rDotId + 5)
                }
                else -> { // Format: com.package.R.id.drawer_layout
                    resPackage = idString.substring(0, rDotId - 1)
                    resName = idString.substring(rDotId + 5)
                }
            }

            val id = context.resources.getIdentifier(resName, "id", resPackage)
            if (id == 0) {
                return@jsArgsFunction JsContext.reportRuntimeError("Only single resource id valid. Can be as name or fully qualified.")
            }

            return@jsArgsFunction activity.findViewById(id)
        })

        addFunction("toast", jsArgsFunction {
            val arg = it.getOrNull(0)
            val str = arg as? CharSequence ?: return@jsArgsFunction JsContext.reportRuntimeError(
                "String argument expected by got ${if (arg != null) arg::class.toString() else "null"}=${it.getOrNull(
                    0
                )}"
            )
            if (str.isEmpty()) return@jsArgsFunction JsContext.getUndefinedValue()
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
            return@jsArgsFunction true
        })

        devFun.categories.forEach { cat ->
            cat.items.forEach {
                addFunction(generateFunctionName(cat, it), jsSimpleFunction { it.call() })
            }
        }

        addFunction("scope", jsScopedFunction { scope ->
            """Imported Classes:
${importedClasses.map { it.toString() }.sorted().joiner("\n", default = "<none>") { "> $it" }}

Imported Packages:
${importedPackages.sorted().joiner("\n", default = "<none>") { "> $it" }}

Variables:
${addedVariables.entries.sortedBy { it.key }.joinToString("\n") { "> ${it.key}=${it.value}" }}

Functions:
${addedFunctions.entries.sortedBy { it.key }.joinToString("\n") { "> ${it.key}=${it.value}." }}

Current Scope:
${scope.ids.map { it.toString() }.sorted().joinToString("\n") { "> $it is ${scope.get(it, scope)}" }}
""".also { log.d { "==Scope==\n\n$it" } }
        })

        addFunction("help", jsSimpleFunction {
            """
scope(): Log JsBuilder environment.
help(): This.
"""
        })

    }.build()

private typealias JsFunctionBody = (cx: JsContext, scope: Scriptable, thisObj: Scriptable, args: Array<out Any>) -> Any?

private inline fun jsSimpleFunction(crossinline body: () -> Any?) = JsBaseFunction({ _, _, _, _ -> body.invoke() })
private inline fun jsScopedFunction(crossinline body: (scope: Scriptable) -> Any) = JsBaseFunction({ _, scope, _, _ -> body.invoke(scope) })
private inline fun jsArgsFunction(crossinline body: (args: Array<out Any>) -> Any) = JsBaseFunction({ _, _, _, args -> body.invoke(args) })
private inline fun jsFunction(invokeOnMain: Boolean = true, crossinline body: JsFunctionBody) =
    JsBaseFunction({ cx, scope, thisObj, args -> body.invoke(cx, scope, thisObj, args) }, invokeOnMain)

private class JsBaseFunction(inline val body: JsFunctionBody, val invokeOnMain: Boolean = true) : BaseFunction() {
    private val log = logger()
    private val isMainThread get() = Looper.myLooper() == Looper.getMainLooper()

    override fun call(cx: JsContext, scope: Scriptable, thisObj: Scriptable, args: Array<out Any>) = try {
        if (!invokeOnMain || isMainThread) {
            body.invoke(cx, scope, thisObj, args)
        } else {
            val waiting = CountDownLatch(1)
            var result: Any? = null
            handler.post {
                result = try {
                    body.invoke(cx, scope, thisObj, args)
                } catch (t: Throwable) {
                    log.w(t) { "Call exception" }
                    t.stackTraceAsString
                } finally {
                    waiting.countDown()
                }
            }
            waiting.await(1000, TimeUnit.SECONDS)
            result
        }
    } catch (t: Throwable) {
        log.w(t) { "Call exception" }
        "Exception $t\n${t.stackTraceAsString}"
    }
}

// TODO Can result in function name clashes! Only latest "addFunction" will actually work
internal val LEGAL_CHARACTERS = Regex("[^a-zA-Z0-9_]")

private class TrackingJsRuntimeReplFactoryBuilder(context: Context) : JsRuntimeReplFactoryBuilder(context) {
    val importedClasses = ArrayList<Class<*>>()
    val importedPackages = ArrayList<String>()
    val addedVariables = HashMap<String, Any?>()
    val addedFunctions = HashMap<String, Function>()

    override fun importClass(aClass: Class<*>): JsRuntimeReplFactoryBuilder {
        importedClasses += aClass
        return super.importClass(aClass)
    }

    override fun importPackage(packageName: String): JsRuntimeReplFactoryBuilder {
        importedPackages += packageName
        return super.importPackage(packageName)
    }

    override fun addVariable(name: String, value: Any?): JsRuntimeReplFactoryBuilder {
        addedVariables[name] = value
        return super.addVariable(name, value)
    }

    override fun addFunction(name: String, function: Function): JsRuntimeReplFactoryBuilder {
        addedFunctions[name] = function
        return super.addFunction(name, function)
    }
}

/** Same as [Iterable.joinToString] but returns and empty string if [List] is empty. (i.e. wont include [prefix] or [postfix]) */
private fun <T> List<T>.joiner(
    separator: CharSequence = ", ",
    prefix: CharSequence = "",
    postfix: CharSequence = "",
    limit: Int = -1,
    truncated: CharSequence = "...",
    default: CharSequence = "",
    transform: ((T) -> CharSequence)? = null
) =
    when {
        this.isEmpty() -> default
        else -> joinTo(StringBuilder(), separator, prefix, postfix, limit, truncated, transform).toString()
    }
