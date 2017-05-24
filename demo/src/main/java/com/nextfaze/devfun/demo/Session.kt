package com.nextfaze.devfun.demo

import com.nextfaze.devfun.annotations.DeveloperFunction
import org.joda.time.DateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Session @Inject constructor() {
    private val log = logger()

    val isSignInEnabled = false
    val isRegistrationEnabled = false

    var user: User? = null

    @DeveloperFunction
    private fun logCurrentUser() = log.d { "user=$user" }
}

enum class Gender { OTHER, FEMALE, MALE }

data class User(val givenName: CharSequence,
                val familyName: CharSequence,
                val userName: CharSequence,
                val password: CharSequence,
                val email: CharSequence,
                val dataOfBirth: DateTime,
                val gender: Gender)
