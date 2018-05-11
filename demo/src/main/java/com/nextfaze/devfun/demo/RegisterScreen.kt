package com.nextfaze.devfun.demo

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.nextfaze.devfun.annotations.DeveloperFunction
import com.nextfaze.devfun.annotations.DeveloperProperty
import com.nextfaze.devfun.demo.inject.FragmentInjector
import com.nextfaze.devfun.demo.kotlin.*
import kotlinx.android.synthetic.main.register_layout.*
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.UI
import org.joda.time.DateTime
import java.util.Random
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RegisterActivity : BaseActivity() {
    companion object {
        fun start(context: Context) = context.startActivity<RegisterActivity>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_activity)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.activity_fragment_view, findOrCreate { RegisterFragment() }).commit()
        }
    }
}

class RegisterFragment : BaseFragment() {
    @Inject lateinit var session: Session

    @DeveloperProperty
    private var validateForm = true
    private var registerJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.register_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        registerButton.apply {
            setOnClickListener { attemptRegister() }
        }

        updateProgressView(registerJob?.isActive ?: false)
    }

    private fun attemptRegister() {
        registerJob?.let { if (!it.isCompleted) return }

        // error state
        var focusView: View? = null
        fun TextView.focusError(stringId: Int) {
            if (validateForm) {
                this.error = getString(stringId)
                focusView = this
            }
        }

        fun TextView.focusRequired() = focusError(R.string.error_field_required)

        // date of birth
        val dob by lazy {
            try {
                DateTime.parse(dateOfBirthEditText.text.trim().toString())
            } catch (ignore: Throwable) {
                DateTime.now()
            }
        }
        dateOfBirthEditText.apply {
            when {
                isBlank() -> focusRequired()
                dob == null -> focusError(R.string.error_invalid_dob)
                else -> clearError()
            }
        }

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

        // user
        val userName = userNameEditText.text.trim()
        userNameEditText.apply {
            when {
                userName.isBlank() -> focusRequired()
                userName.length <= 4 -> focusError(R.string.error_invalid_user)
                else -> clearError()
            }
        }

        // family name
        val familyName = familyNameEditText.text.trim()
        familyNameEditText.apply {
            when {
                familyName.isBlank() -> focusRequired()
                else -> clearError()
            }
        }

        // given name
        val givenName = givenNameEditText.text.trim()
        givenNameEditText.apply {
            when {
                givenName.isBlank() -> focusRequired()
                else -> clearError()
            }
        }

        // gender
        val gender = Gender.values()[genderSpinner.selectedItemPosition]

        if (focusView != null) {
            focusView?.requestFocus()
        } else {
            registerJob = asyncPerformRegistration(givenName, familyName, userName, password, email, dob, gender)
        }
    }

    private fun asyncPerformRegistration(
        givenName: CharSequence,
        familyName: CharSequence,
        userName: CharSequence,
        password: CharSequence,
        email: CharSequence,
        dateOfBirth: DateTime,
        gender: Gender
    ) = launch(CommonPool) {
        try {
            asyncShowProgress(true)

            delay(2, TimeUnit.SECONDS) // simulate network delay
            val createdUser = User(givenName, familyName, userName, password, email, dateOfBirth, gender)

            withContext(UI) {
                session.user = createdUser
                activity?.finish()
                activity?.let { MainActivity.start(it) }
            }
        } finally {
            withContext(NonCancellable) {
                asyncShowProgress(false)
            }
        }
    }

    private suspend fun asyncShowProgress(show: Boolean) = withContext(UI) { updateProgressView(show) }
    private fun updateProgressView(show: Boolean) {
        val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
        registrationForm.apply {
            visible = !show
            animate().setDuration(shortAnimTime)
                .alpha(if (show) 0f else 1f)
                .onAnimationEnd { visible = !show }
        }
        registrationProgressBar.apply {
            visible = show
            animate().setDuration(shortAnimTime)
                .alpha(if (show) 1f else 0f)
                .onAnimationEnd { visible = show }
        }
    }

    override fun inject(injector: FragmentInjector) = injector.inject(this)

    @SuppressLint("SetTextI18n")
    @DeveloperFunction("Populate Randomly (password=password)")
    private fun populateRandomly() {
        val rand = Random()
        val ascii = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"

        fun Random.string(length: Int) = StringBuilder().apply {
            (0 until length).forEach { append(ascii[this@string.nextInt(ascii.length)]) }
        }.toString()

        fun Random.nextInt(min: Int, max: Int) = this.nextInt((max - min) + 1) + min

        // Gender
        val gender = rand.nextInt(genderSpinner.adapter.count)
            .also { genderSpinner.setSelection(it, true) }
            .let { genderSpinner.selectedItem as String }

        // Given Name
        val maleNames = listOf(
            "Jackson", "Aiden", "Liam", "Lucas", "Noah", "Mason", "Ethan", "Caden", "Logan", "Jacob", "Santiago",
            "Mateo", "Matías", "Sebastián", "Martín", "Alejandro", "Samuel", "Benjamín", "Nicolás", "Diego", "DevFun"
        )
        val femaleNames = listOf(
            "Sophia", "Emma", "Olivia", "Ava", "Mia", "Isabella", "Zoe", "Lily", "Emily", "Madison", "Sofía",
            "Isabella", "Lucía", "Valentina", "Emma", "Martina", "Luciana", "Camila", "Victoria", "Valeria", "DevFun"
        )
        val genders = context!!.resources.getStringArray(R.array.genders)
        val names = when (gender) {
            genders.getOrNull(0) -> maleNames + femaleNames
            genders.getOrNull(1) -> femaleNames
            genders.getOrNull(2) -> maleNames
            else -> listOf("NextFaze")
        }
        givenNameEditText.setText(names[rand.nextInt(names.size)])

        // Family Name
        familyNameEditText.setText(R.string.tester)

        // User Name
        userNameEditText.setText("df_tester_${rand.string(4)}")

        // Password
        passwordEditText.setText("password")

        // Email
        emailEditText.setText(userNameEditText.text + "@mailinator.com")

        // Date of Birth
        val year = DateTime.now().year - rand.nextInt(18, 50)
        val month = rand.nextInt(1, 12)

        val monthOfBirth = DateTime(year, month, 1, 0, 0)
        val day = rand.nextInt(1, monthOfBirth.dayOfMonth().maximumValue)
        dateOfBirthEditText.setText(DateTime(year, month, day, 0, 0).toString("yyyy-MM-dd"))
    }

    @DeveloperFunction
    private fun toggleFormValidation() {
        validateForm = !validateForm
        Toast.makeText(context, "Validate form: $validateForm", Toast.LENGTH_SHORT).show()
    }
}
