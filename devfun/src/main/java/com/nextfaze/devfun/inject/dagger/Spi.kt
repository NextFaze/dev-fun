package com.nextfaze.devfun.inject.dagger

import kotlin.reflect.KClass

interface DaggerSpiGraph {
    val components: List<Component>
    val bindingNodes: List<BindingNode>
}

interface Component {
    val name: String
    val parent: String?
    val scopes: List<String>
    val entryPoints: List<Node>
}

interface BindingNode : Node {
    val scope: String?
    val module: String?
    val dependencies: List<Node>
}

interface Node {
    val key: String
    val kind: String
    val element: String?
    val isNullable: Boolean
}

interface Key {
    val qualifier: KClass<*>?
    val type: KClass<*>
    val multiBind: String?
}

/**
 * Entry points give us potential locations for instances - ideally we get it from one of these before checking components and modules.
 *
 * This would be needed if an object is scoped (e.g. ActivityScoped) and you have a Dev. function in some object that is injected into the
 * activity - then to correctly invoke said function you need to get the instance from the activity.
 *
 *
 * i.e. An Activity will have a `MEMBERS_INJECTION` entry point. Thus for Dev. functions in that activity we can pull it from the field.
 * This is not a typical use-case (as usually the user would just reference the field directly rather than trying to inject it), but for
 * the purposes of testing & validation we'll check them anyway.
 *
 * TODO or not?
 */
interface EntryPoint : Node
