@file:Suppress("PackageName")

package tested.instance_providers

import kotlin.reflect.KClass

val classes = listOf(
    PublicObject::class,
    InternalObject::class,
    PrivateObject::class,
    PublicClass.Companion::class,
    PublicClassNamed.CustomName::class,
    PublicClassPrivateCompanion().companionClass,
    PublicClassPrivateNamedCompanion().companionClass,
    PrivateClass.Companion::class,
    PrivateClassNamed.CustomName::class,
    PrivateClassPrivateCompanion().companionClass,
    PrivateClassPrivateNamedCompanion().companionClass
)

object PublicObject
internal object InternalObject
private object PrivateObject

class PublicClass {
    companion object
}

class PublicClassNamed {
    companion object CustomName
}

class PublicClassPrivateCompanion {
    private companion object

    val companionClass: KClass<*> get() = Companion::class
}

class PublicClassPrivateNamedCompanion {
    private companion object CustomName

    val companionClass: KClass<*> get() = CustomName::class
}

class PrivateClass {
    companion object
}

class PrivateClassNamed {
    companion object CustomName
}

class PrivateClassPrivateCompanion {
    private companion object

    val companionClass: KClass<*> get() = Companion::class
}

class PrivateClassPrivateNamedCompanion {
    private companion object CustomName

    val companionClass: KClass<*> get() = CustomName::class
}
