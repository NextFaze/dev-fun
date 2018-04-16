@file:Suppress("PackageName")

package tested.instance_providers

import kotlin.reflect.KClass

val publicObject: KClass<*> = PublicObject::class
val internalObject: KClass<*> = InternalObject::class
val privateObject: KClass<*> = PrivateObject::class

object PublicObject
internal object InternalObject
private object PrivateObject
