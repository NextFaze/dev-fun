package com.nextfaze.devfun.demo.devfun

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.nextfaze.devfun.core.DebugException
import com.nextfaze.devfun.core.devFun
import com.nextfaze.devfun.demo.R
import com.nextfaze.devfun.demo.Session
import com.nextfaze.devfun.demo.User
import com.nextfaze.devfun.demo.inject.DaggerActivity
import com.nextfaze.devfun.demo.inject.Initializer
import com.nextfaze.devfun.demo.inject.applicationComponent
import com.nextfaze.devfun.inject.InstanceProvider
import com.nextfaze.devfun.inject.captureInstance
import com.nextfaze.devfun.inject.dagger2.tryGetInstanceFromComponent
import com.nextfaze.devfun.inject.dagger2.useAutomaticDagger2Injector
import com.nextfaze.devfun.internal.*
import com.nextfaze.devfun.menu.MenuHeader
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import javax.inject.Singleton
import kotlin.reflect.KClass

@Module
class DevFunModule {
    init {
        useAutomaticDagger2Injector = false
    }

    @Provides @IntoSet @Singleton
    fun initializeDevFun(application: Application, session: Session): Initializer = {
        //DevFun().initialize(application, DevMenu(), DevHttpD(), DevHttpIndex(), DevStetho(), useServiceLoader = false)
        devFun += onInitialized@ {
            instanceProviders += captureInstance { DemoMenuHeader(session) }
            instanceProviders += DemoInstanceProvider(application, devFun.get())
        }
    }
}

private class DemoInstanceProvider(private val application: Application, private val activityProvider: ActivityProvider) : InstanceProvider {
    private val applicationComponent by lazy { application.applicationComponent!! }

    override fun <T : Any> get(clazz: KClass<out T>): T? {
        tryGetInstanceFromComponent(applicationComponent, clazz)?.let { return it }

        activityProvider()?.let { activity ->
            if (activity is DaggerActivity) {
                tryGetInstanceFromComponent(activity.retainedComponent, clazz)?.let { return it }
                tryGetInstanceFromComponent(activity.activityComponent, clazz)?.let { return it }
            }
        }

        return null
    }
}

private class DemoMenuHeader(private val session: Session) : MenuHeader<DemoMenuHeaderView> {
    override fun onCreateView(parent: ViewGroup): DemoMenuHeaderView = View.inflate(parent.context, R.layout.demo_menu_header, null) as DemoMenuHeaderView
    override fun onBindView(view: DemoMenuHeaderView, parent: ViewGroup, activity: Activity) {
        with(view) {
            setTitle(activity::class.splitSimpleName)
            setCurrentUser(session.user)
        }
    }
}

private class DemoMenuHeaderView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {
    init {
        View.inflate(context, R.layout.demo_menu_header_view, this)
        findViewById<View>(R.id.crashButton).setOnClickListener { throw DebugException() }
    }

    fun setTitle(title: String) {
        findViewById<TextView>(R.id.activityTextView).text = title
    }

    @SuppressLint("SetTextI18n")
    fun setCurrentUser(user: User?) {
        val titleTextView = findViewById<TextView>(R.id.currentUserTitleTextView)
        val textView = findViewById<TextView>(R.id.currentUserTextView)
        if (user == null) {
            titleTextView.text = "User (NONE)"
            textView.text = ""
        } else {
            titleTextView.text = "User (${user.id})"
            textView.text = with(user) {
                """
                |$givenName $familyName ($gender)
                |$userName
                |$email
                |DOB: $dataOfBirth
            """.trimMargin()
            }
        }
    }
}
