package com.nextfaze.devfun.stetho

import android.content.Context
import com.facebook.stetho.Stetho
import com.facebook.stetho.inspector.jsonrpc.JsonRpcPeer
import com.facebook.stetho.inspector.jsonrpc.JsonRpcResult
import com.facebook.stetho.inspector.protocol.ChromeDevtoolsMethod
import com.facebook.stetho.inspector.protocol.module.Console
import com.google.auto.service.AutoService
import com.nextfaze.devfun.core.AbstractDevFunModule
import com.nextfaze.devfun.core.DevFun
import com.nextfaze.devfun.core.DevFunModule
import org.json.JSONObject
import org.mozilla.javascript.ContextFactory
import org.mozilla.javascript.tools.shell.ShellContextFactory
import com.facebook.stetho.inspector.protocol.module.Page as StethoPage

@AutoService(DevFunModule::class)
class DevStetho : AbstractDevFunModule() {
    override fun init(context: Context) {
        val initializer = Stetho.newInitializerBuilder(context)
            .enableDumpapp(Stetho.defaultDumperPluginsProvider(context))
            .enableWebKitInspector {
                Stetho.DefaultInspectorModulesBuilder(context)
                    .runtimeRepl { buildJsRuntime(context, get(), devFun).newInstance() }
                    .finish()
                    .filter { it::class != StethoPage::class }
                    .plus(Page(context, this))
            }
            .build()

        Stetho.initialize(initializer)
        ContextFactory.initGlobal(AdvancedShellContextFactory().apply { setGeneratingDebug(true) })
    }
}

@Suppress("unused")
val DevFun.stetho get() = get<DevStetho>()

class Page(context: Context, private val devStetho: DevStetho) : StethoPage(context) {
    @ChromeDevtoolsMethod
    override fun enable(peer: JsonRpcPeer, params: JSONObject?) {
        super.enable(peer, params)
        writeConsoleText(peer, "DevFun REPL support is enabled.")
        writeConsoleText(peer, "DevFun REPL support is extremely experimental/buggy.", Console.MessageLevel.WARNING)

        writeConsoleText(
            peer,
            """Scope Info:

handler
    Main Looper Handler

post(<lambda>);
    Posts to UI thread.
    e.g.
        post(function() { viewById('signInButton').setEnabled(true) })
        post(function() { viewById('drawerLayout').openDrawer(GravityCompat.START) })

viewById(<viewId>);
    Gets a view by id
    <viewId> can be view_id, R.id.view_id, com.package.R.id.view_id
    e.g.
        toast("Navigation View: "+viewById('navigationView'))

toast(<string>);
    Displays Toast message.
    e.g.
        toast("Some test message")

scope();
    Shows all imported packages/variables/classes/functions

help();
    Help message.
"""
        )

        val allItems = devStetho.devFun.categories
            .flatMap { cat -> cat.items.map { cat to it } }
            .joinToString("\n") { (cat, item) -> "${cat.name} - ${item.name}: ${generateFunctionName(cat, item)}()" }
        writeConsoleText(peer, "Available functions:\n\n$allItems")
    }

    private fun writeConsoleText(peer: JsonRpcPeer, txt: String, lvl: Console.MessageLevel = Console.MessageLevel.LOG) {
        val messageAddedRequest = Console.MessageAddedRequest().apply {
            message = Console.ConsoleMessage().apply {
                source = Console.MessageSource.JAVASCRIPT
                level = lvl
                text = txt
            }
        }
        peer.invokeMethod("Console.messageAdded", messageAddedRequest, null /* callback */)
    }

    @ChromeDevtoolsMethod override fun disable(peer: JsonRpcPeer, params: JSONObject?) = super.disable(peer, params)
    @ChromeDevtoolsMethod override fun getResourceTree(p: JsonRpcPeer, o: JSONObject?): JsonRpcResult = super.getResourceTree(p, o)
    @ChromeDevtoolsMethod override fun canScreencast(p: JsonRpcPeer, o: JSONObject?): JsonRpcResult = super.canScreencast(p, o)
    @ChromeDevtoolsMethod override fun hasTouchInputs(p: JsonRpcPeer, o: JSONObject?): JsonRpcResult = super.hasTouchInputs(p, o)
    @ChromeDevtoolsMethod override fun setDeviceMetricsOverride(p: JsonRpcPeer, o: JSONObject?) = super.setDeviceMetricsOverride(p, o)
    @ChromeDevtoolsMethod override fun startScreencast(peer: JsonRpcPeer, params: JSONObject?) = super.startScreencast(peer, params)
    @ChromeDevtoolsMethod override fun stopScreencast(peer: JsonRpcPeer, params: JSONObject?) = super.stopScreencast(peer, params)
    @ChromeDevtoolsMethod override fun screencastFrameAck(peer: JsonRpcPeer, params: JSONObject?) = super.screencastFrameAck(peer, params)
    @ChromeDevtoolsMethod override fun clearGeolocationOverride(p: JsonRpcPeer, o: JSONObject?) = super.clearGeolocationOverride(p, o)
    @ChromeDevtoolsMethod override fun setTouchEmulationEnabled(p: JsonRpcPeer, o: JSONObject?) = super.setTouchEmulationEnabled(p, o)
    @ChromeDevtoolsMethod override fun setEmulatedMedia(peer: JsonRpcPeer, params: JSONObject?) = super.setEmulatedMedia(peer, params)
    @ChromeDevtoolsMethod override fun setShowViewportSizeOnResize(p: JsonRpcPeer, o: JSONObject?) = super.setShowViewportSizeOnResize(p, o)
    @ChromeDevtoolsMethod override fun clearDeviceOrientationOverride(p: JsonRpcPeer, o: JSONObject?) =
        super.clearDeviceOrientationOverride(p, o)
}

private class AdvancedShellContextFactory : ShellContextFactory() {
    @Suppress("DEPRECATION")
    private enum class Feature(val id: Int) {
        NON_ECMA_GET_YEAR(JsContext.FEATURE_NON_ECMA_GET_YEAR),
        MEMBER_EXPR_AS_FUNCTION_NAME(JsContext.FEATURE_MEMBER_EXPR_AS_FUNCTION_NAME),
        RESERVED_KEYWORD_AS_IDENTIFIER(JsContext.FEATURE_RESERVED_KEYWORD_AS_IDENTIFIER),
        TO_STRING_AS_SOURCE(JsContext.FEATURE_TO_STRING_AS_SOURCE),
        PARENT_PROTO_PROPERTIES(JsContext.FEATURE_PARENT_PROTO_PROPERTIES),
        PARENT_PROTO_PROPERTIES_OLD(JsContext.FEATURE_PARENT_PROTO_PROPRTIES),
        E4X(JsContext.FEATURE_E4X),
        DYNAMIC_SCOPE(JsContext.FEATURE_DYNAMIC_SCOPE),
        STRICT_VARS(JsContext.FEATURE_STRICT_VARS),
        STRICT_EVAL(JsContext.FEATURE_STRICT_EVAL),
        LOCATION_INFORMATION_IN_ERROR(JsContext.FEATURE_LOCATION_INFORMATION_IN_ERROR),
        STRICT_MODE(JsContext.FEATURE_STRICT_MODE),
        WARNING_AS_ERROR(JsContext.FEATURE_WARNING_AS_ERROR),
        ENHANCED_JAVA_ACCESS(JsContext.FEATURE_ENHANCED_JAVA_ACCESS),
        V8_EXTENSIONS(JsContext.FEATURE_V8_EXTENSIONS);

        companion object {
            private val ID_MAP = values().associateBy { it.id }
            operator fun get(id: Int) = ID_MAP[id]
        }
    }

    override fun hasFeature(cx: JsContext, featureIndex: Int) = when (Feature[featureIndex]) {
        Feature.ENHANCED_JAVA_ACCESS -> true
        Feature.DYNAMIC_SCOPE -> true
        else -> super.hasFeature(cx, featureIndex)
    }
}
