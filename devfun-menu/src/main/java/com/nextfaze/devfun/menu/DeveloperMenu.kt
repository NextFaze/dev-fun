package com.nextfaze.devfun.menu

import android.app.Activity
import android.content.Context
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AlertDialog
import android.text.SpannableStringBuilder
import com.google.auto.service.AutoService
import com.nextfaze.devfun.annotations.DeveloperCategory
import com.nextfaze.devfun.annotations.DeveloperFunction
import com.nextfaze.devfun.core.AbstractDevFunModule
import com.nextfaze.devfun.core.ActivityProvider
import com.nextfaze.devfun.core.DevFun
import com.nextfaze.devfun.core.DevFunModule
import com.nextfaze.devfun.error.ErrorHandler
import com.nextfaze.devfun.inject.InstanceProvider
import com.nextfaze.devfun.internal.string.*
import com.nextfaze.devfun.menu.controllers.CogOverlay
import com.nextfaze.devfun.menu.controllers.GRAVE_KEY_SEQUENCE
import com.nextfaze.devfun.menu.controllers.KeySequence
import com.nextfaze.devfun.menu.controllers.VOLUME_KEY_SEQUENCE
import com.nextfaze.devfun.overlay.OverlayManager
import com.nextfaze.devfun.view.ViewFactoryProvider
import com.nextfaze.devfun.view.viewFactory
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.reflect.KClass

interface DeveloperMenu : MenuController {
    /** Flag indicating if the menu is currently visible. */
    val isVisible: Boolean

    fun addController(menuController: MenuController)
    fun removeController(menuController: MenuController)
    operator fun plusAssign(menuController: MenuController) = addController(menuController)
    operator fun minusAssign(menuController: MenuController) = removeController(menuController)

    fun show(activity: FragmentActivity)
    fun hide(activity: FragmentActivity)
}

interface MenuController {
    /** A human-readable description of the name of this controller. */
    val title: String

    /**
     * A human-readable description of how this controller is used.
     *
     * It should be `null` when the controller is not available for use (e.g. permissions or hidden, etc.)
     *
     * Can contain spannable formatting.
     */
    val actionDescription: CharSequence?

    /**
     * Called when this controller is being attached/initialized to a [DeveloperMenu].
     *
     * You should create any activity or lifecycle related listeners here.
     */
    fun attach(developerMenu: DeveloperMenu)

    /**
     * Called when this controller is being detached/removed from a [DeveloperMenu].
     *
     * Ensure all resources/references (such as activity listeners) are cleaned up here.
     */
    fun detach()

    /** Called when [DeveloperMenu] is shown. */
    fun onShown()

    /** Called when [DeveloperMenu] is dismissed. */
    fun onDismissed()
}

/**
 * The view type/key used by DevMenu to find/inflate the menu header view.
 *
 * Example usage from demo (~line 64 `demoMenuHeaderFactory` in `com.nextfaze.devfun.demo.devfun.DevFun.kt`):
 * ```kotlin
 * // MenuHeader is the "key" (used by DevMenu to inflate the menu header)
 * // DemoMenuHeaderView is the custom view type
 * devFun.viewFactories += viewFactory<MenuHeader, DemoMenuHeaderView>(R.layout.demo_menu_header) {
 *     setTitle(activityProvider()!!::class.splitSimpleName)
 *     setCurrentUser(session.user)
 * }
 * ```
 *
 * @see viewFactory
 */
interface MenuHeader

val DevFun.devMenu get() = get<DevMenu>()

@AutoService(DevFunModule::class)
@DeveloperCategory("DevMenu", order = 90_000)
class DevMenu : AbstractDevFunModule(), DeveloperMenu {
    private val overlays by lazy { devFun.get<OverlayManager>() }

    private val views = object : ViewFactoryProvider {
        override fun get(clazz: KClass<*>) = clazz.takeIf { it == MenuHeader::class }?.let { DefaultMenuHeaderViewFactory() }
    }

    @Suppress("UNCHECKED_CAST")
    private val instances = object : InstanceProvider {
        override fun <T : Any> get(clazz: KClass<out T>): T? = controllers.firstOrNull { it::class == clazz } as T?
    }

    override fun init(context: Context) {
        val activityProvider = get<ActivityProvider>()
        this += KeySequence(context, activityProvider).also {
            it += GRAVE_KEY_SEQUENCE
            it += VOLUME_KEY_SEQUENCE
        }
        this += CogOverlay(context, activityProvider, get())

        devFun.instanceProviders += instances
        devFun.viewFactories += views
    }

    override fun dispose() {
        controllers.forEach(MenuController::detach)
        devFun.instanceProviders -= instances
        devFun.viewFactories -= views
        overlays.releaseFullScreenLock(this)
    }

    private val controllers = hashSetOf<MenuController>()
    private val showing = AtomicBoolean(false)

    override val isVisible get() = showing.get()
    override val title: String get() = context.getString(R.string.df_menu_developer_menu)
    override val actionDescription: CharSequence?
        get() = controllers
            .mapNotNull { c -> c.actionDescription?.let { c.title to it } }
            .takeIf { it.isNotEmpty() }
            ?.joinTo(SpannableStringBuilder(), "\n\n") {
                SpannableStringBuilder().apply {
                    this += b(it.first)
                    this += "\n"
                    this += it.second
                }
            }

    override fun addController(menuController: MenuController) {
        controllers += menuController.also {
            it.attach(this)
        }
    }

    override fun removeController(menuController: MenuController) {
        menuController.takeIf { controllers.remove(menuController) }?.detach()
    }

    override fun show(activity: FragmentActivity) {
        if (!showing.get() && overlays.takeFullScreenLock(this)) {
            try {
                showing.set(true)
                DeveloperMenuDialogFragment.show(activity)
            } catch (t: Throwable) {
                showing.set(false)
                overlays.releaseFullScreenLock(this)
                get<ErrorHandler>().onError(t, "Show Menu Failed", "Please report this error!")
            }
        }
    }

    override fun hide(activity: FragmentActivity) = DeveloperMenuDialogFragment.hide(activity)
    override fun attach(developerMenu: DeveloperMenu) = Unit
    override fun detach() = Unit

    override fun onShown() {
        showing.set(true)
        overlays.takeFullScreenLock(this)
        controllers.forEach { it.onShown() }
    }

    override fun onDismissed() {
        showing.set(false)
        overlays.releaseFullScreenLock(this)
        controllers.forEach { it.onDismissed() }
    }

    @DeveloperFunction
    fun showAvailableControllers(activity: Activity): String {
        val title = activity.getString(R.string.df_menu_available_controllers)
        val msg = actionDescription ?: activity.getString(R.string.df_menu_no_controllers)
        AlertDialog.Builder(activity)
            .setTitle(title)
            .setMessage(msg)
            .show()
        return "$title\n$msg"
    }
}
