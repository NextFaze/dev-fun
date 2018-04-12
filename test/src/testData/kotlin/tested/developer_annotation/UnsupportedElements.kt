@file:Suppress("UNUSED_PARAMETER", "unused", "PackageName")

package tested.developer_annotation

import com.nextfaze.devfun.annotations.DeveloperAnnotation

annotation class UnsupportedElements

@Retention(AnnotationRetention.BINARY)
@DeveloperAnnotation
@Target(AnnotationTarget.CLASS)
annotation class TypeTag

@TypeTag
class MyClass
