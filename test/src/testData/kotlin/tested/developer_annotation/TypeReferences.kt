@file:Suppress("UNUSED_PARAMETER", "unused", "PackageName", "ClassName")

package tested.developer_annotation

import com.nextfaze.devfun.annotations.DeveloperAnnotation
import kotlin.annotation.AnnotationRetention.BINARY
import kotlin.annotation.AnnotationTarget.CLASS

annotation class TypeReferences

@Retention(BINARY)
@DeveloperAnnotation
@Target(CLASS)
annotation class TypeReferenceTagging

@TypeReferenceTagging
class tr_SomeClass

@TypeReferenceTagging
private class tr_SomePrivateClass

@TypeReferenceTagging
object tr_SomeObject

@TypeReferenceTagging
private object tr_SomePrivateObject

class tr_SomeClassWithCompanionObject {
    @TypeReferenceTagging
    companion object
}

private class tr_SomePrivateClassWithCompanionObject {
    @TypeReferenceTagging
    companion object
}
