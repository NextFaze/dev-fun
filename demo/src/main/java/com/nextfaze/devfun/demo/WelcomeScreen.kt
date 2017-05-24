package com.nextfaze.devfun.demo

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nextfaze.devfun.annotations.DeveloperFunction
import com.nextfaze.devfun.demo.inject.FragmentInjector
import com.nextfaze.devfun.demo.kotlin.enabled
import com.nextfaze.devfun.demo.kotlin.findOrCreate
import com.nextfaze.devfun.demo.kotlin.startActivity
import kotlinx.android.synthetic.main.welcome_layout.*
import javax.inject.Inject

class WelcomeActivity : BaseActivity() {
    companion object {
        fun start(context: Context) = context.startActivity<WelcomeActivity>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_activity)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.activity_fragment_view, findOrCreate { WelcomeFragment() }).commit()
        }
    }
}

class WelcomeFragment : BaseFragment() {
    @Inject lateinit var session: Session

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.welcome_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        signInButton.apply {
            enabled = session.isSignInEnabled
            setOnClickListener { AuthenticateActivity.start(activity) }
        }
        createAccountButton.apply {
            enabled = session.isRegistrationEnabled
            setOnClickListener { RegisterActivity.start(activity) }
        }
    }

    @DeveloperFunction
    private fun enableSignInButton() {
        signInButton.enabled = true
    }

    @DeveloperFunction
    private fun enableCreateAccountButton() {
        createAccountButton.enabled = true
    }

    override fun inject(injector: FragmentInjector) = injector.inject(this)
}
