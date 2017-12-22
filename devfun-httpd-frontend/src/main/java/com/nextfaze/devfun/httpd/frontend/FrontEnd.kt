package com.nextfaze.devfun.httpd.frontend

import android.content.Context
import android.os.Build
import android.provider.Settings
import com.google.auto.service.AutoService
import com.nextfaze.devfun.core.AbstractDevFunModule
import com.nextfaze.devfun.core.DevFun
import com.nextfaze.devfun.core.DevFunModule
import com.nextfaze.devfun.core.devFun
import com.nextfaze.devfun.httpd.AbstractUriHandler
import com.nextfaze.devfun.httpd.DevHttpD
import com.nextfaze.devfun.httpd.devHttpD
import com.nextfaze.devfun.internal.*
import fi.iki.elonen.NanoHTTPD.*
import fi.iki.elonen.router.RouterNanoHTTPD
import fi.iki.elonen.router.RouterNanoHTTPD.UriResource
import java.lang.reflect.Modifier


private val log = logger("com.nextfaze.devfun.httpd.frontend.FrontEnd")

val DevFun.httpFrontEnd get() = get<HttpFrontEnd>()

@AutoService(DevFunModule::class)
class HttpFrontEnd : AbstractDevFunModule() {
    override val dependsOn = super.dependsOn + DevHttpD::class

    override fun init(context: Context) {
        devFun.devHttpD.apply {
            nano.addRoute("/", IndexHandler::class.java)
            nano.addRoute("(.)+", AssetHandler::class.java)
        }
    }
}

internal class AssetHandler : AbstractUriHandler() {
    private val log = logger()
    override fun get(uriResource: UriResource, urlParams: Map<String, String>, session: IHTTPSession): Response {
        log.d { "uriResource=$uriResource, session.uri=${session.uri}, urlParams=[${urlParams.entries.joinToString { "${it.key}=${it.value}" }}]" }

        val mimeType = when (session.uri.substringAfterLast('.')) {
            "css" -> "text/css"
            "html" -> "text/html"
            "js" -> "text/js"
            "svg" -> "image/svg+xml"
            "woff" -> "application/font-woff2"
            "woff2" -> "application/font-woff2"
            "otf" -> "application/font-sfnt"
            "ttf" -> "application/font-sfnt"
            "eot" -> "application/vnd.ms-fontobject"
            else -> "application/octet-stream"
        }

        val asset = devFun.devHttpD.context.assets.open(session.uri.substring(1))
        return RouterNanoHTTPD.newFixedLengthResponse(Response.Status.OK, mimeType, asset, asset.available().toLong())
    }
}

private fun javaPropertiesTable() = kvTable(System.getProperties(), "Java Properties", "fa fa-coffee fa-fw")
private fun devicePropertiesTable() = kvTable(readDeviceProperties(), "Device Properties", "fa fa-mobile fa-fw")
private fun systemSettingsTable(context: Context) = kvTable(readSystemSettings(context), "System Settings", "glyphicon glyphicon-cog")
private fun globalSettingsTable(context: Context) = kvTable(readGlobalSettings(context), "Global Settings", "fa fa-cogs fa-fw")
private fun buildConfigTable(context: Context): String {
    /**
     * By default the `BuildConfig` class is in the root application package.
     * However if the app uses an `applicationIdSuffix`, it will be included as part of the deployed app package (from [Context.getPackageName]).
     * Thus we traverse up the packages in hope of finding it.
     */
    val appBuildConfigClass = run loadClass@ {
        var pkg = context.packageName
        while (pkg.isNotBlank()) {
            val buildConfigPath = "$pkg.BuildConfig"
            log.d { "Try-get BuildConfig from $buildConfigPath" }
            try {
                return@loadClass Class.forName(buildConfigPath)
            } catch (ignore: Throwable) {
                pkg = pkg.substringBeforeLast(".", "")
            }
        }
        log.w { "Failed to find app BuildConfig class" }
        return@loadClass null
    }

    val buildConfigFields = appBuildConfigClass?.declaredFields?.associate {
        it.isAccessible = true
        it.name to it.get(null)
    } ?: mapOf("ERROR" to "Failed to find app BuildConfig class")
    return kvTable(buildConfigFields, "Build Config", "fa fa-android fa-fw")
}

private fun kvTable(map: Map<*, *>, title: String, icon: String = "fa fa-info fa-fw"): String {
    return """
                <div class="panel panel-default">
                    <div class="panel-heading">${if (icon.isNotBlank()) """<i class="$icon"></i> """ else ""}$title</div>

                    <div class="panel-body">
                        <div class="table-responsive">
                            <table class="table table-hover">
                                <thead>
                                <tr>
                                    <th>Key</th>
                                    <th>Value</th>
                                </tr>
                                </thead>
                                <tbody>
${map.entries.joinToString("\n") {
        """
                                <tr>
                                    <td>${it.key}</td>
                                    <td>${it.value}</td>
                                </tr>
"""
    }}
                                </tbody>
                            </table>
                        </div>
                    </div>

                </div>
                <!-- /.panel -->
"""
}


internal class IndexHandler : AbstractUriHandler() {
    private val log = logger()
    private val context = devFun.httpFrontEnd.context

    override fun get(uriResource: UriResource, urlParams: Map<String, String>, session: IHTTPSession): Response {
        val categories = devFun.categories
        log.t { categories.flatMap { it.items }.joinToString("\n") { "item[${it.hashCode()}]=$it" } }
        log.d { "urlParams=${urlParams.values.joinToString()}" }

        var bodyTitle: CharSequence? = null
        val body = session.parameters["c"]?.firstOrNull()?.toIntOrNull().let { c ->
            categories.firstOrNull { it.name.hashCode() == c }?.let {
                bodyTitle = it.name
                val groups = it.items.groupBy { it.group }
                val hasGroups = groups.size != 1 || groups.keys.single() != null

                groups.entries.sortedBy { it.key.toString() }.joinToString("\n") { (group, items) ->
                    """
${if (hasGroups) """
                                <div class="panel panel-default">
                                    <div class="panel-heading">
                                        <h4 class="panel-title">${group ?: "Misc"}</h4>
                                    </div>
                                    <div class="panel-body">
""" else ""}
                                        <div class="table-responsive">
                                            <table class="table table-hover">
                                                <thead>
                                                    <tr>
                                                        <th>Function</th>
                                                        <th>User Args</th>
                                                        <th></th>
                                                    </tr>
                                                </thead>
                                                <tbody>
        ${items.joinToString("\n") {
                        val funDef = "${it.function.clazz.java.name}::${it.function.method.name}(${it.function.method.parameterTypes.joinToString { it.simpleName }})"
                        """
                                                    <tr>
                                                        <td>${it.name}<br /><small class="text-muted">$funDef</small></td>
                                                        <td>[${it.args.orEmpty().joinToString().takeIf { it.isNotBlank() } ?: " "}]</td>
                                                        <td><button type="button" class="btn btn-primary" onClick="postInvoke('${it.hashCode()}', '$funDef')">Invoke</button></td>
                                                    </tr>
        """
                    }}
                                                </tbody>
                                            </table>
                                        </div>
${if (hasGroups) """
                                    </div>
                                </div>
""" else ""}
"""
                }
            }
        } ?:
                // Dashboard
                """
        <div class="col-lg-6">
${buildConfigTable(context)}
${javaPropertiesTable()}
${systemSettingsTable(context)}
${globalSettingsTable(context)}
        </div>
        <div class="col-lg-6">${devicePropertiesTable()}</div>
"""

        val categoryListItems = categories.joinToString("\n") {
            """
                        <li>
                            <a href="/?c=${it.name.hashCode()}">${it.name}</a>
                        </li>
"""
        }

        //language=HTML
        return newFixedLengthResponse(
                """<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="DevFun web interface for ${context.applicationName}">
    <meta name="author" content="NextFaze">

    <title>${context.applicationName}</title>

    <!-- Favicon -->
    <link rel="icon" type="image/png" href="/favicon.png" />

    <!-- Bootstrap Core CSS -->
    ${addCss("/vendor/bootstrap/css/bootstrap.min.css")}

    <!-- MetisMenu CSS -->
    ${addCss("/vendor/metisMenu/metisMenu.min.css")}

    <!-- Custom CSS -->
    ${addCss("/dist/css/sb-admin-2.min.css")}

    <!-- Footer CSS -->
    ${addCss("/css/footer.css")}

    <!-- Morris Charts CSS -->
    ${addCss("/vendor/morrisjs/morris.css")}

    <!-- Custom Fonts -->
    ${addCss("/vendor/font-awesome/css/font-awesome.min.css")}

    <!-- Animate -->
    ${addCss("/vendor/animate/animate.min.css")}

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

</head>

<body>

<script>
function postInvoke(hashCode, funDef) {
    var notify = $.notify({
        icon: 'glyphicon glyphicon-play',
        message: 'Invoke...<br /><small class="text-muted">' + funDef + '</small>'
    },{
        type: 'info',
        showProgressbar: true
    });

    $.ajax({
        url: '/invoke/' + hashCode,
        type: 'POST',
        data: {},
        success: function(data) {
            notify.update('type', 'success');
            notify.update('icon', 'glyphicon glyphicon-ok');
            notify.update('message', 'Invoke Success<br />' + data);
        },
        error: function(xhr) {
            notify.update('type', 'warning');
            notify.update('icon', 'glyphicon glyphicon-warning-sign');
            notify.update('message', 'Invoke Error!<br />' + xhr.responseText);
        }
    });
}
</script>

    <div id="wrapper">

        <!-- Navigation -->
        <nav class="navbar navbar-default navbar-static-top" role="navigation" style="margin-bottom: 0">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="index.html">${context.applicationName} on ${Build.DEVICE}</a>
            </div>
            <!-- /.navbar-header -->

            <div class="navbar-default sidebar" role="navigation">
                <div class="sidebar-nav navbar-collapse">
                    <ul class="nav" id="side-menu">
                        <li class="sidebar-search">
                            <div class="input-group custom-search-form">
                                <input type="text" class="form-control" placeholder="Search... (WIP)">
                                <span class="input-group-btn">
                                <button class="btn btn-default" type="button">
                                    <i class="fa fa-search"></i>
                                </button>
                            </span>
                            </div>
                            <!-- /input-group -->
                        </li>
                        <li>
                            <a href="/"><i class="fa fa-dashboard fa-fw"></i> Dashboard</a>
                        </li>
$categoryListItems
                    </ul>
                </div>
                <!-- /.sidebar-collapse -->
            </div>
            <!-- /.navbar-static-side -->
        </nav>

        <div id="page-wrapper">
            <div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header">${bodyTitle ?: "Dashboard"}</h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- /.row -->
            <div class="row">
$body
            </div>
            <!-- /.row -->
        </div>
        <!-- /#page-wrapper -->

        <footer class="footer">
          <div class="container">
            <p class="text-muted"  style="text-align: center">
                <br />
                NextFaze DevFun web interface &mdash; version ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})<br />
                <a href="https://github.com/NextFaze/dev-fun">GitHub</a> &mdash;
                <a href="https://nextfaze.github.io/dev-fun/wiki/">Wiki</a> &mdash;
                <a href="https://nextfaze.github.io/dev-fun/">Dokka</a> &mdash;
                <a href="https://github.com/NextFaze/dev-fun/issues">Issues</a><br />
            </p>
          </div>
        </footer>
    </div>
    <!-- /#wrapper -->

    <!-- jQuery -->
    ${addScript("/vendor/jquery/jquery.min.js")}

    <!-- Bootstrap Core JavaScript -->
    ${addScript("/vendor/bootstrap/js/bootstrap.min.js")}

    <!-- Metis Menu Plugin JavaScript -->
    ${addScript("/vendor/metisMenu/metisMenu.min.js")}

    <!-- Morris Charts JavaScript -->
    ${addScript("/vendor/raphael/raphael.min.js")}
    ${addScript("/vendor/morrisjs/morris.min.js")}

    <!-- Custom Theme JavaScript -->
    ${addScript("/dist/js/sb-admin-2.min.js")}

    <!-- Notify -->
    ${addScript("/vendor/notify/bootstrap-notify.min.js")}

    <!-- Flot Charts JavaScript -->
    <script src="/vendor/flot/excanvas.min.js"></script>
    <script src="/vendor/flot/jquery.flot.js"></script>
    <script src="/vendor/flot/jquery.flot.pie.js"></script>
    <script src="/vendor/flot/jquery.flot.resize.js"></script>
    <script src="/vendor/flot/jquery.flot.time.js"></script>
    <script src="/vendor/flot-tooltip/jquery.flot.tooltip.min.js"></script>
    <!--<script src="/data/flot-data.js"></script>-->


</body>

</html>
""")
    }
}

private fun addScript(path: String) = """<script src="${if (BuildConfig.VERSION_SNAPSHOT) path.replace(".min.js", ".js") else path}"></script>"""
private fun addCss(path: String) = """<link href="${if (BuildConfig.VERSION_SNAPSHOT) path.replace(".min.css", ".css") else path}" rel="stylesheet" type="text/css">"""

private val Context.applicationName: String get() {
    val applicationInfo = applicationInfo
    val stringId = applicationInfo.labelRes
    return if (stringId == 0) applicationInfo.nonLocalizedLabel.toString() else getString(stringId)
}

private val PROPERTY_REGEX = Regex("\\[(.*)]: \\[(.*)]")
private fun readDeviceProperties(): Map<String, String> {
    val properties = mutableMapOf<String, String>()
    var getPropProc: Process? = null
    try {
        getPropProc = Runtime.getRuntime().exec("getprop")
        getPropProc.inputStream.bufferedReader().forEachLine {
            val match = PROPERTY_REGEX.matchEntire(it)
            if (match == null) {
                log.w { "Property line failed to match: '$it'" }
            } else {
                properties[match.groupValues[1]] = match.groupValues[2]
            }
        }
    } catch (t: Throwable) {
        log.w(t) { "Exec failed" }
    } finally {
        getPropProc?.destroy()
    }
    return properties.toSortedMap(compareBy<String> { it })
}

private val Context.androidId: String get() = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)

private fun readSystemSettings(context: Context): Map<String, String> {
    if (true) {
        return mapOf("TODO" to "TODO")
    }
    try {
        val resolver = context.contentResolver
        val settings = mutableMapOf<String, String>()
        val globalClass = Class.forName("android.provider.Settings\$Secure")
        globalClass.declaredFields.forEach {
            try {
                if (Modifier.isStatic(it.modifiers) && it.type == String::class.java) {
                    it.isAccessible = true
                    val key = it.get(null)
                    if (key is String) {
                        settings[it.name] = Settings.Secure.getString(resolver, key) ?: "null"
                    } else {
                        settings[it.name] = "NOT_STRING"
                    }
                }
            } catch (t: Throwable) {
                settings[it.name] = t.message ?: t.toString()
            }
        }
        return settings
    } catch (t: Throwable) {
        return mapOf("ERROR" to (t.message ?: t.toString()))
    }
}

private fun readGlobalSettings(context: Context): Map<String, String> {
    if (true) {
        return mapOf("TODO" to "TODO")
    }
    try {
        val resolver = context.contentResolver
        val settings = mutableMapOf<String, String>()
        val globalClass = Class.forName("android.provider.Settings\$Global")
        globalClass.declaredFields.forEach {
            try {
                if (Modifier.isStatic(it.modifiers) && it.type == String::class.java) {
                    it.isAccessible = true
                    val key = it.get(null)
                    if (key is String) {
                        settings[it.name] = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
                            Settings.Secure.getString(resolver, key) ?: "null"
                        } else {
                            Settings.Global.getString(resolver, key) ?: "null"
                        }
                    } else {
                        settings[it.name] = "NOT_STRING"
                    }
                }
            } catch (t: Throwable) {
                settings[it.name] = t.message ?: t.toString()
            }
        }
        return settings
    } catch (t: Throwable) {
        return mapOf("ERROR" to (t.message ?: t.toString()))
    }
}
