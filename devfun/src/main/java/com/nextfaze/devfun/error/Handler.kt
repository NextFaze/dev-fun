package com.nextfaze.devfun.error

import android.support.v4.app.FragmentActivity
import com.nextfaze.devfun.core.ActivityProvider
import com.nextfaze.devfun.core.FunctionItem
import com.nextfaze.devfun.inject.Constructable
import com.nextfaze.devfun.internal.e
import com.nextfaze.devfun.internal.logger

/**
 * Handles errors that occur during/throughout DevFun.
 *
 * You should use this in your own modules to provide consistent error handling.
 *
 * The default error handler will show a dialog with the exception stack trace.
 */
interface ErrorHandler {
    /**
     * Called when a general error occurred throughout DevFun.
     *
     * If an error occurred when processing/invoking a [FunctionItem] you should call the other [ErrorHandler.onError] which provides a little extra logging/info.
     *
     * @param t The exception that occurred.
     * @param title A title for the message/dialog.
     * @param body A short description of how/why this exception was thrown.
     *
     */
    fun onError(t: Throwable, title: String, body: String)

    /**
     * Called when an error occurred while doing something with [functionItem].
     *
     * This could be anything from reading/processing it, to actually invoking it.
     *
     * @param functionItem The relevant function item that lead to this error occurring.
     * @param t The exception that occurred.
     * @param title A title for the message/dialog.
     * @param body A short description of how/why this exception was thrown.
     */
    fun onError(functionItem: FunctionItem, t: Throwable, title: String, body: String)
}

@Constructable
internal class DefaultErrorHandler(private val activityProvider: ActivityProvider) : ErrorHandler {
    private val log = logger()
    private val activity get() = activityProvider() as? FragmentActivity

    override fun onError(t: Throwable, title: String, body: String) {
        activity?.let { ErrorDialogFragment.show(it, t, title, body) }
    }

    override fun onError(functionItem: FunctionItem, t: Throwable, title: String, body: String) {
        log.e(t) { "Exception while handling ${functionItem.function.method} ($functionItem)" }
        activity?.let { ErrorDialogFragment.show(it, t, title, body, functionItem) }
    }
}
