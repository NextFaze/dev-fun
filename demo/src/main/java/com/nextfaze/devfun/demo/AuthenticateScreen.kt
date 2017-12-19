package com.nextfaze.devfun.demo

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.nextfaze.devfun.annotations.DeveloperFunction
import com.nextfaze.devfun.core.CategoryDefinition
import com.nextfaze.devfun.core.FunctionDefinition
import com.nextfaze.devfun.core.FunctionTransformer
import com.nextfaze.devfun.core.SimpleFunctionItem
import com.nextfaze.devfun.demo.inject.FragmentInjector
import com.nextfaze.devfun.demo.kotlin.*
import com.nextfaze.devfun.inject.Constructable
import kotlinx.android.synthetic.main.authenticate_layout.*
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.UI
import org.joda.time.DateTime
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AuthenticateActivity : BaseActivity() {
    companion object {
        fun start(context: Context) = context.startActivity<AuthenticateActivity>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_activity)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.activity_fragment_view, findOrCreate { AuthenticateFragment() }).commit()
        }
    }

    @DeveloperFunction("Do A Barrel Roll")
    private fun barrelRoll() {
        with(Handler(Looper.getMainLooper())) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            post(delay = 500) { requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT }
            post(delay = 1000) { requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE }
            post(delay = 1500) { requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT }
        }
    }
}

private val CREDENTIALS = arrayOf("foo@example.com:hello", "bar@example.com:world")

class AuthenticateFragment : BaseFragment() {
    @Inject lateinit var session: Session

    private var authJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.authenticate_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        passwordEditText.apply {
            setOnEditorActionListener { _, id, _ ->
                when (id) {
                    R.id.login, EditorInfo.IME_NULL -> {
                        attemptLogin()
                        true
                    }
                    else -> false
                }
            }
        }

        signInButton.apply {
            setOnClickListener { attemptLogin() }
        }

        updateProgressView(authJob?.isActive ?: false)
    }

    private fun attemptLogin() {
        authJob?.let { if (!it.isCompleted) return }

        // error state
        var focusView: View? = null
        fun TextView.focusError(stringId: Int) {
            this.error = getString(stringId)
            focusView = this
        }

        fun TextView.focusRequired() = focusError(R.string.error_field_required)

        // email
        val email = emailEditText.text.trim()
        emailEditText.apply {
            when {
                email.isBlank() -> focusRequired()
                !email.contains("@") -> focusError(R.string.error_invalid_email)
                else -> clearError()
            }
        }

        // password
        val password = passwordEditText.text.trim()
        passwordEditText.apply {
            when {
                password.isBlank() -> focusRequired()
                password.length <= 4 -> focusError(R.string.error_invalid_password)
                else -> clearError()
            }
        }

        if (focusView != null) {
            focusView?.requestFocus()
        } else {
            authJob = asyncPerformLogin(email, password)
        }
    }

    private fun asyncPerformLogin(email: CharSequence, password: CharSequence) = launch(CommonPool) {
        try {
            asyncShowProgress(true)

            delay(2, TimeUnit.SECONDS) // simulate network delay
            val isValid = CREDENTIALS.contains("$email:$password")

            run(UI) {
                if (isValid) {
                    session.user = User("givenName", "familyName", "userName", password, email, DateTime.now(), Gender.OTHER)
                    activity?.finish()
                    activity?.let { MainActivity.start(it) }
                } else {
                    passwordEditText.error = getString(R.string.error_incorrect_password)
                    passwordEditText.requestFocus()
                }
            }
        } finally {
            run(NonCancellable) {
                asyncShowProgress(false)
            }
        }
    }

    private suspend fun asyncShowProgress(show: Boolean) = run(UI) { updateProgressView(show) }
    private fun updateProgressView(show: Boolean) {
        val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
        authenticationForm.apply {
            visible = !show
            animate().setDuration(shortAnimTime)
                    .alpha(if (show) 0f else 1f)
                    .onAnimationEnd { visible = !show }
        }
        authenticationProgressBar.apply {
            visible = show
            animate().setDuration(shortAnimTime)
                    .alpha(if (show) 1f else 0f)
                    .onAnimationEnd { visible = show }
        }
    }

    override fun inject(injector: FragmentInjector) = injector.inject(this)

    @DeveloperFunction(transformer = SignInFunctionTransformer::class)
    private fun signInAs(email: String, password: String) {
        emailEditText.setText(email)
        passwordEditText.setText(password)
        attemptLogin()
    }
}

@Constructable
private class SignInFunctionTransformer : FunctionTransformer {
    private data class TestAccount(val email: String, val password: String) {
        val title = "Authenticate as $email"
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
