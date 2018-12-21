package com.nextfaze.devfun.internal

import android.content.Context
import android.text.SpannableStringBuilder
import com.nextfaze.devfun.core.R
import com.nextfaze.devfun.error.ErrorHandler
import com.nextfaze.devfun.error.SimpleError
import com.nextfaze.devfun.internal.string.*
import java.util.ServiceConfigurationError
import java.util.ServiceLoader
import kotlin.reflect.KClass

internal fun <T : Any> loadServices(clazz: KClass<T>, context: Context, errorHandler: ErrorHandler): Sequence<T> {
    fun Throwable.toLoaderError(body: CharSequence? = null) =
        SimpleError(
            this,
            context.getString(R.string.df_devfun_service_loader_exception),
            body ?: context.getString(R.string.df_devfun_service_loader_error, clazz.simpleName!!)
        )

    fun ServiceConfigurationError.toConfigurationError() =
        serviceConfigurationErrorRegex.find(toString())?.groups?.get(1)?.value?.let { moduleName ->
            val body = SpannableStringBuilder().apply {
                this += context.getString(R.string.df_devfun_service_loader_module_error, clazz.simpleName!!)
                this += " "
                this += pre(moduleName)
            }
            toLoaderError(body)
        } ?: toLoaderError()

    ServiceLoader.load(clazz.java).map { }

    val it = ServiceLoader.load(clazz.java).iterator()

    class SafeIterator : Iterator<T?> {
        override fun hasNext() = it.hasNext()
        override fun next() =
            try {
                it.next()
            } catch (t: ServiceConfigurationError) {
                errorHandler.onError(t.toConfigurationError())
                null
            } catch (t: Throwable) {
                errorHandler.onError(t.toLoaderError())
                null
            }
    }

    return SafeIterator().asSequence().filterNotNull()
}

private val serviceConfigurationErrorRegex =
    Regex("""^java.util.ServiceConfigurationError: com.nextfaze.devfun.core.DevFunModule: Provider ([^\s]*)""")
