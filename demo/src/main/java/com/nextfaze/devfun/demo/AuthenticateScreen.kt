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
import com.nextfaze.devfun.annotations.Args
import com.nextfaze.devfun.annotations.DeveloperArguments
import com.nextfaze.devfun.annotations.DeveloperFunction
import com.nextfaze.devfun.annotations.DeveloperProperty
import com.nextfaze.devfun.core.CategoryDefinition
import com.nextfaze.devfun.core.FunctionDefinition
import com.nextfaze.devfun.core.FunctionTransformer
import com.nextfaze.devfun.core.SimpleFunctionItem
import com.nextfaze.devfun.demo.inject.FragmentInjector
import com.nextfaze.devfun.demo.kotlin.*
import com.nextfaze.devfun.inject.Constructable
import kotlinx.android.synthetic.main.authenticate_fragment.*
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.Main
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

private val normalUsers = arrayOf(
    "foo@example.com:hello",
    "bar@example.com:world",
    "mary@mailinator.com:ContainDateNeck76",
    "eli@example.com:EveningVermontNeck29",
    "trevor@example.com:OftenJumpCost02"
)

class AuthenticateFragment : BaseFragment() {
    @Inject lateinit var session: Session

    @DeveloperProperty
    private var validateForm = true

    private var authJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.authenticate_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        passwordEditText.apply {
            setOnEditorActionListener { _, id, _ ->
                when (id) {
                    resources.getInteger(R.integer.login), EditorInfo.IME_NULL -> {
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
            if (validateForm) {
                this.error = getString(stringId)
                focusView = this
            }
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
        val password = passwordEditText.value
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

    private fun asyncPerformLogin(email: CharSequence, password: CharSequence) = GlobalScope.launch {
        try {
            asyncShowProgress(true)

            delay(TimeUnit.SECONDS.toMillis(2)) // simulate network delay
            val isValid = normalUsers.contains("$email:$password")

            withContext(Dispatchers.Main) {
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
            withContext(NonCancellable) {
                asyncShowProgress(false)
            }
        }
    }

    private suspend fun asyncShowProgress(show: Boolean) = withContext(Dispatchers.Main) { updateProgressView(show) }
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

    @DeveloperArguments(
        // name of function for each Args entry is the first element; foo@example.com, bar@example.com, etc.
        // name = "%0", // "%0" is the default value
        args = [
            Args(["foo@example.com", "hello", "Normal"]), // group = "Authenticate as Normal"
            Args(["bar@example.com", "world", "Normal"]),
            Args(["mary@mailinator.com", "ContainDateNeck76", "Normal"]),
            Args(["eli@example.com", "EveningVermontNeck29", "Normal"]),
            Args(["trevor@example.com", "OftenJumpCost02", "Normal"]),
            Args(["rude.user@example.com", "TakePlayThought95", "Restricted"]),
            Args(["block.stars@mailinator.com", "DeviceSeedsSeason16", "Restricted"]),
            Args(["vulgar@user.com", "ChinaMisterGeneral11", "Banned"]),
            Args(["thirteen@years.old", "my.password", "Underage"]),
            Args(["twelve@years.old", "password", "Underage"]),
            Args(["bad.password.1@example.com", "D3;d<HF-", "Invalid Password"]),
            Args(["bad.password.2@example.com", "r2Z@fMhA", "Invalid Password"]),
            Args(["unknown@example.com", "RV[(x43@", "Unknown User"])
        ],
        group = "Authenticate as %2" // %n as index to args array for each Args entry
    )
//    @DeveloperFunction(transformer = SignInFunctionTransformer::class) // Alternative to @DeveloperArguments
    private fun signInAs(email: String, password: String) {
        emailEditText.setText(email)
        passwordEditText.setText(password)
        attemptLogin()
    }
}

@Constructable
@Suppress("unused")
private class SignInFunctionTransformer : FunctionTransformer {
    private enum class Type { NORMAL, RESTRICTED, BANNED, UNDERAGE, INVALID_PASSWORD, UNKNOWN_USER }
    private data class TestAccount(val email: String, val password: String, val type: Type) {
        val group = "Authenticate as ${type.toString().toLowerCase().splitCamelCase()}"
    }

    private val accounts = if (BuildConfig.DEBUG) { // BuildConfig.DEBUG for dead-code removal
        listOf(
            TestAccount("foo@example.com", "hello", Type.NORMAL),
            TestAccount("bar@example.com", "world", Type.NORMAL),
            TestAccount("mary@mailinator.com", "ContainDateNeck76", Type.NORMAL),
            TestAccount("eli@example.com", "EveningVermontNeck29", Type.NORMAL),
            TestAccount("trevor@example.com", "OftenJumpCost02", Type.NORMAL),
            TestAccount("rude.user@example.com", "TakePlayThought95", Type.RESTRICTED),
            TestAccount("block.stars@mailinator.com", "DeviceSeedsSeason16", Type.RESTRICTED),
            TestAccount("vulgar@user.com", "ChinaMisterGeneral11", Type.BANNED),
            TestAccount("thirteen@years.old", "my.password", Type.UNDERAGE),
            TestAccount("twelve@years.old", "password", Type.UNDERAGE),
            TestAccount("bad.password.1@example.com", "D3;d<HF-", Type.INVALID_PASSWORD),
            TestAccount("bad.password.2@example.com", "r2Z@fMhA", Type.INVALID_PASSWORD),
            TestAccount("unknown@example.com", "RV[(x43@", Type.UNKNOWN_USER)
        )
    } else {
        emptyList()
    }

    override fun apply(functionDefinition: FunctionDefinition, categoryDefinition: CategoryDefinition): List<SimpleFunctionItem> =
        accounts.map {
            object : SimpleFunctionItem(functionDefinition, categoryDefinition) {
                override val name = it.email
                override val args = listOf(it.email, it.password) // arguments as expected from signInAs(...)
                override val group = it.group
            }
        }

    companion object {
        private val SPLIT_REGEX = Regex("(?<=[a-z0-9])(?=[A-Z])|[\\s]")

        private fun String.splitCamelCase() = this
            .replace('_', ' ')
            .split(SPLIT_REGEX)
            .map { it.trim().capitalize() }
            .filter(String::isNotBlank)
            .joinToString(" ")
    }
}
