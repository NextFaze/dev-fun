package com.nextfaze.devfun.demo

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.nextfaze.devfun.demo.inject.FragmentInjector
import com.nextfaze.devfun.demo.kotlin.enabled
import com.nextfaze.devfun.demo.kotlin.startActivity
import com.nextfaze.devfun.demo.test.DummyClass
import com.nextfaze.devfun.demo.util.value
import com.nextfaze.devfun.function.DeveloperFunction
import com.nextfaze.devfun.function.DeveloperProperty
import com.nextfaze.devfun.inject.Constructable
import com.nextfaze.devfun.invoke.view.ColorPicker
import com.nextfaze.devfun.invoke.view.From
import com.nextfaze.devfun.invoke.view.ValueSource
import com.nextfaze.devfun.invoke.view.Values
import com.nextfaze.devfun.reference.DeveloperLogger
import com.uber.autodispose.android.lifecycle.scope
import com.uber.autodispose.kotlin.autoDisposable
import kotlinx.android.synthetic.main.welcome_fragment.*
import javax.inject.Inject

class WelcomeActivity : BaseActivity() {
    companion object {
        fun start(context: Context) = context.startActivity<WelcomeActivity>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_activity)

        if (savedInstanceState == null) {
            setContentFragment { WelcomeFragment() }
        }
    }

    @DeveloperFunction
    private fun somePrivateDebugFunction() = showToast("hello world")
}

class WelcomeFragment : BaseFragment() {
    @Inject lateinit var config: Config
    @Inject internal lateinit var dummyClass: DummyClass

    @DeveloperLogger(top = 0.137f)
    @DeveloperProperty
    private var signInButtonEnabled
        get() = signInButton.enabled
        set(value) = run { signInButton.enabled = value }

    @DeveloperLogger(top = 0.104f)
    @DeveloperProperty
    private var createAccountButtonEnabled
        get() = createAccountButton.enabled
        set(value) = run { createAccountButton.enabled = value }

    override fun inject(injector: FragmentInjector) = injector.inject(this)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.welcome_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        signInButton.apply {
            enabled = config.signInEnabled
            setOnClickListener { AuthenticateActivity.start(activity!!) }
        }
        createAccountButton.apply {
            enabled = config.registrationEnabled
            setOnClickListener { RegisterActivity.start(activity!!) }
        }
        motdText.text = config.welcomeString
    }

    override fun onStart() {
        super.onStart()
        config.signInEnabled().observable.autoDisposable(scope()).subscribe { signInButton.isEnabled = it }
        config.registrationEnabled().observable.autoDisposable(scope()).subscribe { createAccountButton.isEnabled = it }
        config.welcomeString().observable.autoDisposable(scope()).subscribe { motdText.text = it.value }
    }

    @Constructable
    private inner class CurrentBackgroundColor : ValueSource<Int> {
        override val value get() = (view?.background as? ColorDrawable)?.color ?: Color.rgb(250, 250, 250)
    }

    @DeveloperFunction
    internal fun setBackgroundColor(@ColorPicker @From(CurrentBackgroundColor::class) color: Int) {
        view?.setBackgroundColor(color)
    }

    @DeveloperFunction
    private fun thrownExceptionsShowErrorDialog(): Unit = throw RuntimeException("Test")

    private enum class SomeEnum { OPTION_1, OPTION_2 }

    @DeveloperFunction
    private fun invokeUiForSimpleTypes(context: Context, anIntParam: Int, anEnumParam: SomeEnum, aStringParam: String) =
        Toast.makeText(context, "You entered: $anIntParam, $anEnumParam, '$aStringParam'", Toast.LENGTH_LONG).show()

    @DeveloperFunction
    @Suppress("UNUSED_PARAMETER")
    private fun invokeUiWithMissingType(context: Context, anIntParam: Int, anEnumParam: SomeEnum, someType: SomeType) = Unit

    data class SomeObject(val hello: String)

    private val listItems = listOf(SomeObject("hello"), SomeObject("world"))

    @Constructable
    private inner class SomeObjects : ValueSource<List<SomeObject>> {
        override val value get() = listItems
    }

    @Constructable
    private inner class InitialSelected : ValueSource<SomeObject> {
        override val value get() = listItems.last()
    }

    @DeveloperFunction
    private fun invokeUiWithList(@Values(SomeObjects::class) @From(InitialSelected::class) someObject: SomeObject) =
        Toast.makeText(activity!!, "selected=$someObject", Toast.LENGTH_LONG).show()
}

/**
 * This type is not an `object`, nor injected by Dagger, or annotated @[Constructable].
 * Thus if we try to call a function via DevFun that takes this type an error will be shown that it can't find/instantiate it.
 */
private class SomeType
