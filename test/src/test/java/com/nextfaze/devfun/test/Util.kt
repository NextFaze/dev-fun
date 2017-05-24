@file:Suppress("unused")

package com.nextfaze.devfun.test

import com.nextfaze.devfun.internal.*
import org.jetbrains.kotlin.com.intellij.openapi.application.ApplicationManager
import org.jetbrains.kotlin.utils.KotlinPaths
import org.jetbrains.kotlin.utils.PathUtil
import java.net.URL
import kotlin.reflect.KClass

private val log = logger("TestUtil")

private fun dumpKotlinPaths() {
    log.i { "jdkClassesRoots=\n${PathUtil.getJdkClassesRoots().joinToString("\n") { it.canonicalPath }}" }
//    log.i { "jdkClassesRoots=\n${PathUtil.getJdkClassesRootsFromCurrentJre().joinToString("\n") { it.canonicalPath }}" }
    log.i { "pathUtilJar=${PathUtil.getPathUtilJar()}" }
    if (ApplicationManager.getApplication() == null) {
        log.i { "Local application environment not initialized (create KotlinCoreEnvironment first)" }
        return
    }
    logKotlinPaths(PathUtil.getKotlinPathsForIdeaPlugin(), "kotlinPathsForIdeaPlugin")
    logKotlinPaths(PathUtil.getKotlinPathsForJpsPlugin(), "kotlinPathsForJpsPlugin")
    logKotlinPaths(PathUtil.getKotlinPathsForJpsPluginOrJpsTests(), "kotlinPathsForJpsPluginOrJpsTests")
    logKotlinPaths(PathUtil.getKotlinPathsForDistDirectory(), "kotlinPathsForDistDirectory")
    logKotlinPaths(PathUtil.getKotlinPathsForCompiler(), "kotlinPathsForCompiler")
}

private fun logKotlinPaths(kotlinPaths: KotlinPaths?, name: String) {
    log.i { "$name=$kotlinPaths" }
    kotlinPaths ?: return
    with(kotlinPaths) {
        log.i {
            """homePath=$homePath, exists=${homePath.exists()}
            |libPath=$libPath, exists=${libPath.exists()}
            |runtimePath=$runtimePath, exists=${runtimePath.exists()}
            |reflectPath=$reflectPath, exists=${reflectPath.exists()}
            |scriptRuntimePath=$scriptRuntimePath, exists=${scriptRuntimePath.exists()}
            |kotlinTestPath=$kotlinTestPath, exists=${kotlinTestPath.exists()}
            |runtimeSourcesPath=$runtimeSourcesPath, exists=${runtimeSourcesPath.exists()}
            |jsStdLibJarPath=$jsStdLibJarPath, exists=${jsStdLibJarPath.exists()}
            |jsStdLibSrcJarPath=$jsStdLibSrcJarPath, exists=${jsStdLibSrcJarPath.exists()}
            |jsKotlinTestJarPath=$jsKotlinTestJarPath, exists=${jsKotlinTestJarPath.exists()}
            |allOpenPluginJarPath=$allOpenPluginJarPath, exists=${allOpenPluginJarPath.exists()}
            |noArgPluginJarPath=$noArgPluginJarPath, exists=${noArgPluginJarPath.exists()}
            |compilerPath=$compilerPath, exists=${compilerPath.exists()}
            |buildNumberFile=$buildNumberFile, exists=${buildNumberFile.exists()}""".trimMargin()
        }
    }
}

internal inline val <T : Any> T.srcLocation: URL get() = this::class.java.srcLocation
internal inline val <T : Any> KClass<T>.srcLocation: URL get() = this.java.srcLocation
internal inline val <T : Any> Class<T>.srcLocation: URL get() = this.protectionDomain.codeSource.location

internal fun List<*>.combine(other: List<*>): List<Pair<*, *>> {
    return this.mapIndexed { i, s -> s to other[i] }
}
