package com.nextfaze.devfun.demo.devfun

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.nextfaze.devfun.core.ActivityProvider
import com.nextfaze.devfun.core.DebugException
import com.nextfaze.devfun.core.devFun
import com.nextfaze.devfun.demo.R
import com.nextfaze.devfun.demo.Session
import com.nextfaze.devfun.demo.User
import com.nextfaze.devfun.demo.inject.DaggerActivity
import com.nextfaze.devfun.demo.inject.Initializer
import com.nextfaze.devfun.demo.inject.applicationComponent
import com.nextfaze.devfun.inject.InstanceProvider
import com.nextfaze.devfun.inject.dagger2.tryGetInstanceFromComponent
import com.nextfaze.devfun.menu.MenuHeader
import com.nextfaze.devfun.view.viewFactory
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import javax.inject.Singleton
import kotlin.reflect.KClass

@Module
class DevFunModule {
    /*
     * If we were using our own instance provider (i.e. the DemoInstanceProvider below) then we'd want to disable the automatic one.
     */
//    init {
//        useAutomaticDagger2Injector = false
//    }

    @Provides @IntoSet @Singleton
    fun initializeDevFun(application: Application, session: Session): Initializer = {
        //DevFun().initialize(application, DevMenu(), DevHttpD(), DevHttpIndex(), DevStetho(), useServiceLoader = false)
        devFun += onInitialized@{
            viewFactories += demoMenuHeaderFactory(session, get())
            /*
             * Since the demo is leveraging the @Dagger2Component annotations this isn't needed.
             */
//            instanceProviders += DemoInstanceProvider(application, get())
        }
    }
}

@Suppress("unused")
private class DemoInstanceProvider(private val app: Application, private val activityProvider: ActivityProvider) : InstanceProvider {
    private val applicationComponent by lazy { app.applicationComponent!! }

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

private fun demoMenuHeaderFactory(session: Session, activityProvider: ActivityProvider) =
    viewFactory<MenuHeader, DemoMenuHeaderView>(R.layout.demo_menu_header) {
        setTitle(activityProvider()!!::class.splitSimpleName)
        setCurrentUser(session.user)
    }

private class DemoMenuHeaderView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    FrameLayout(context, attrs, defStyleAttr) {
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

private val SPLIT_REGEX = Regex("(?<=[a-z0-9])(?=[A-Z])|[\\s]")

private fun String.splitCamelCase() = this
    .replace('_', ' ')
    .split(SPLIT_REGEX)
    .map { it.trim().capitalize() }
    .filter(String::isNotBlank)
    .joinToString(" ")

private inline val KClass<*>.splitSimpleName get() = this.java.simpleName.splitCamelCase()
