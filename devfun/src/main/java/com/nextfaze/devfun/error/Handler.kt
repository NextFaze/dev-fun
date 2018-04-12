package com.nextfaze.devfun.error

import android.app.Application
import android.support.v4.app.FragmentActivity
import android.text.SpannableStringBuilder
import com.nextfaze.devfun.annotations.DeveloperCategory
import com.nextfaze.devfun.annotations.DeveloperFunction
import com.nextfaze.devfun.core.*
import com.nextfaze.devfun.inject.Constructable
import com.nextfaze.devfun.internal.*
import kotlinx.android.parcel.Parcelize

/**
 * Details/information of an error.
 *
 * @see SimpleError
 */
interface ErrorDetails {
    /** When this error occurred in millis since epoch. */
    val time: Long

    /** The exception that was thrown. */
    val t: Throwable

    /** A title for the dialog - the "kind" if you will. */
    val title: CharSequence

    /** Some details about why it occurred and/or resolution details, etc. */
    val body: CharSequence

    /** The function item to lead to this error (such as when attempting to invoke/prepare/whatever). Will be `null` for general errors. */
    val functionItem: FunctionItem?
}

/**
 * Convenience class that implements [ErrorDetails] and automatically time stamps it.
 *
 * @see ErrorDetails.time
 */
data class SimpleError(
    override val t: Throwable,
    override val title: CharSequence,
    override val body: CharSequence,
    override val functionItem: FunctionItem? = null,
    override val time: Long = System.currentTimeMillis()
) : ErrorDetails

/**
 * Handles errors that occur during/throughout DevFun.
 *
 * You should use this in your own modules to provide consistent error handling.
 * It's unlikely you'll need to implement this yourself.
 *
 * The default error handler will show a dialog with the exception stack trace and some error details.
 */
interface ErrorHandler {
    /**
     * Call to log an error.
     *
     * This could be anything from reading/processing throughout DevFun, to more specific scenarios such as when
     * invoking a [FunctionItem].
     *
     * This function simply delegates to [ErrorHandler.onError].
     *
     * @param t The exception that occurred.
     * @param title A title for the message/dialog.
     * @param body A short description of how/why this exception was thrown.
     * @param functionItem The relevant function item that lead to this error occurring (or `null`/absent) if not relevant.
     */
    fun onError(t: Throwable, title: CharSequence, body: CharSequence, functionItem: FunctionItem? = null)

    /**
     * Call to log an error.
     *
     * This could be anything from reading/processing throughout DevFun, to more specific scenarios such as when
     * invoking a [FunctionItem].
     *
     * @param error The error details.
     */
    fun onError(error: ErrorDetails)

    /**
     * Mark an error as seen by the user.
     *
     * @param key To error key - as defined by the [ErrorHandler] implementation.
     */
    fun markSeen(key: Any)

    /**
     * Remove an error from the history.
     *
     * @param key To error key - as defined by the [ErrorHandler] implementation.
     */
    fun remove(key: Any)

    /** Clears all errors, seen or otherwise. */
    fun clearAll()
}

@DeveloperCategory("DevFun")
internal class DefaultErrorHandler(application: Application, private val activityProvider: ActivityProvider) : ErrorHandler {
    private val log = logger()
    private val activity get() = activityProvider() as? FragmentActivity

    private val errorLock = Any()
    private var errors = mutableMapOf<Any, RenderedError>()

    init {
        application.registerActivityCallbacks(
            onResumed = { showErrorDialogIfHaveUnseen() }
        )
    }

    override fun onError(t: Throwable, title: CharSequence, body: CharSequence, functionItem: FunctionItem?) =
        onError(SimpleError(t, title, body, functionItem))

    override fun onError(error: ErrorDetails) {
        log.e(error.t) { "DevFun Error:\n${error.toString().replace(", ", "\n")}" }

        val renderedError = error.render()
        synchronized(errorLock) {
            errors[renderedError.nanoTime] = renderedError
        }

        showErrorDialogIfHaveUnseen()
    }

    override fun markSeen(key: Any) {
        synchronized(errorLock) {
            errors[key]?.seen = true
        }
    }

    override fun remove(key: Any) {
        synchronized(errorLock) {
            errors.remove(key)
        }
    }

    override fun clearAll() {
        synchronized(errorLock) {
            errors.clear()
        }
    }

    @Constructable
    private inner class ShowErrorDialogVisibility : VisibilityTransformer() {
        override val predicate = { synchronized(errorLock) { errors.isNotEmpty() } }
    }

    @DeveloperFunction(transformer = ShowErrorDialogVisibility::class)
    private fun showErrorDialog() = showErrorDialogIfHaveUnseen(true)

    private fun showErrorDialogIfHaveUnseen(force: Boolean = false) {
        val dialogErrors = synchronized(errorLock) {
            errors.takeIf { it.isNotEmpty() }?.takeIf { force || it.values.any { !it.seen } }?.values?.mapTo(ArrayList()) { it }
        } ?: return
        activity?.also {
            ErrorDialogFragment.show(it, dialogErrors)
        }
    }

    @Constructable
    private inner class ShowErrorsTransformer : FunctionTransformer {
        override fun apply(functionDefinition: FunctionDefinition, categoryDefinition: CategoryDefinition): List<SimpleFunctionItem>? {
            val errorCount = synchronized(errorLock) { errors.size }.takeIf { it > 0 } ?: return emptyList()
            val category = object : CategoryDefinition {
                override val name = SpannableStringBuilder().apply { this += bold("Errors ($errorCount)") }
                override val order = -9_000 // under Context
            }

            fun createFunctionItem(name: String, group: String? = null, action: ErrorHandler.() -> Unit) =
                object : SimpleFunctionItem(functionDefinition, categoryDefinition) {
                    override val name = name
                    override val group = group
                    override val category = category
                    override val args = listOf(action)
                }

            return listOf(
                createFunctionItem("Show Error Dialog", "Errors") { showErrorDialog() },
                createFunctionItem("Clear All") { clearAll() }
            )
        }
    }

    @DeveloperFunction(transformer = ShowErrorsTransformer::class)
    private fun manageErrors(action: ErrorHandler.() -> Unit) = action()
}

/**
 * TODO consider making this a publicly accessible utility class?
 *
 * Perhaps add annotation for the function argument @Visibility or something (to avoid need of custom transform implementation)?
 */
private abstract class VisibilityTransformer(
    private val transformer: FunctionTransformer = SingleFunctionTransformer
) : FunctionTransformer {
    override fun apply(functionDefinition: FunctionDefinition, categoryDefinition: CategoryDefinition) =
        if (predicate()) transformer.apply(functionDefinition, categoryDefinition) else emptyList()

    abstract val predicate: () -> Boolean
}

private fun ErrorDetails.render(): RenderedError =
    RenderedErrorImpl(
        System.nanoTime(),
        System.currentTimeMillis(),
        t.stackTraceAsString,
        title,
        body,
        functionItem?.function?.method?.toString(),
        false
    )

@Parcelize
private data class RenderedErrorImpl(
    override val nanoTime: Long,
    override val timeMs: Long,
    override val stackTrace: String,
    override val title: CharSequence,
    override val body: CharSequence,
    override val method: CharSequence?,
    override var seen: Boolean
) : RenderedError
