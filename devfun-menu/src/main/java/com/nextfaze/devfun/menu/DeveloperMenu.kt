package com.nextfaze.devfun.menu

import android.content.Context
import android.support.v4.app.FragmentActivity
import com.google.auto.service.AutoService
import com.nextfaze.devfun.core.AbstractDevFunModule
import com.nextfaze.devfun.core.DevFun
import com.nextfaze.devfun.core.DevFunModule
import com.nextfaze.devfun.inject.InstanceProvider
import com.nextfaze.devfun.internal.*
import com.nextfaze.devfun.menu.controllers.CogOverlay
import com.nextfaze.devfun.menu.controllers.KeySequence
import kotlin.reflect.KClass

interface DeveloperMenu : MenuController {
    val isVisible: Boolean

    fun addController(menuController: MenuController)
    fun removeController(menuController: MenuController)
    operator fun plusAssign(menuController: MenuController) = addController(menuController)
    operator fun minusAssign(menuController: MenuController) = removeController(menuController)

    fun show(activity: FragmentActivity)
    fun hide(activity: FragmentActivity)
}

interface MenuController {
    fun attach(developerMenu: DeveloperMenu)
    fun detach()

    fun onShown()
    fun onDismissed()
}

@AutoService(DevFunModule::class)
class DevMenu : AbstractDevFunModule(), DeveloperMenu, InstanceProvider {
    override fun init(context: Context) {
        val activityProvider = get<ActivityProvider>()
        this += KeySequence(context, activityProvider)
        this += CogOverlay(context, activityProvider)
        devFun.instanceProviders += this
    }

    override fun dispose() {
        controllers.forEach(MenuController::detach)
        devFun.instanceProviders -= this
    }

    private val controllers = hashSetOf<MenuController>()
    private var visible = false

    override val isVisible get() = visible

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
}

val DevFun.devMenu get() = get<DevMenu>()
