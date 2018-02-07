package com.nextfaze.devfun.menu

import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AlertDialog
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.View
import android.view.ViewGroup
import com.google.auto.service.AutoService
import com.nextfaze.devfun.annotations.DeveloperCategory
import com.nextfaze.devfun.annotations.DeveloperFunction
import com.nextfaze.devfun.core.AbstractDevFunModule
import com.nextfaze.devfun.core.ActivityProvider
import com.nextfaze.devfun.core.DevFun
import com.nextfaze.devfun.core.DevFunModule
import com.nextfaze.devfun.inject.InstanceProvider
import com.nextfaze.devfun.inject.captureInstance
import com.nextfaze.devfun.menu.controllers.*
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
 * Provide an implementation of this to define your own header view.
 *
 * See `DemoMenuHeader` for example.
 */
interface MenuHeader<T : View> {
    fun onCreateView(parent: ViewGroup): T
    fun onBindView(view: T, parent: ViewGroup, activity: Activity)
}

val DevFun.devMenu get() = get<DevMenu>()

@AutoService(DevFunModule::class)
@DeveloperCategory("DevFun", "Developer Menu")
class DevMenu : AbstractDevFunModule(), DeveloperMenu, InstanceProvider {
    override fun init(context: Context) {
        val activityProvider = get<ActivityProvider>()
        this += KeySequence(context, activityProvider).also {
            it += GRAVE_KEY_SEQUENCE
            it += VOLUME_KEY_SEQUENCE
        }
        this += CogOverlay(context, activityProvider)

        devFun.instanceProviders += captureInstance { DefaultMenuHeader() }
        devFun.instanceProviders += this
    }

    override fun dispose() {
        controllers.forEach(MenuController::detach)
        devFun.instanceProviders -= this
    }

    private val controllers = hashSetOf<MenuController>()
    private var visible = false

    override val isVisible get() = visible
    override val title: String get() = context.getString(R.string.df_menu_developer_menu)
    override val actionDescription: CharSequence?
        get() = controllers
            .mapNotNull { c -> c.actionDescription?.let { c.title to it } }
            .takeIf { it.isNotEmpty() }
            ?.joinTo(SpannableStringBuilder(), "\n\n") {
                SpannableStringBuilder().apply {
                    this += it.first
                    setSpan(StyleSpan(Typeface.BOLD), 0, it.first.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
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

    override fun show(activity: FragmentActivity) = DeveloperMenuDialogFragment.show(activity)
    override fun hide(activity: FragmentActivity) = DeveloperMenuDialogFragment.hide(activity)

    override fun attach(developerMenu: DeveloperMenu) = Unit
    override fun detach() = Unit

    override fun onShown() {
        visible = true
        controllers.forEach { it.onShown() }
    }

    override fun onDismissed() {
        visible = false
        controllers.forEach { it.onDismissed() }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> get(clazz: KClass<out T>) = controllers.firstOrNull { it::class == clazz } as T?

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
