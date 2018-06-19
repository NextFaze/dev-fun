@file:Suppress("UNUSED_PARAMETER", "unused", "PackageName", "ClassName")

package tested.developer_annotation

import com.nextfaze.devfun.annotations.DeveloperAnnotation
import kotlin.annotation.AnnotationRetention.BINARY

annotation class VarReferences

@Retention(BINARY)
@DeveloperAnnotation
@Target(AnnotationTarget.FIELD)
annotation class VarReferenceTagging

class vr_SomeClass {
    @VarReferenceTagging
    var myVar = true
}

private class vr_SomePrivateClass {
    @VarReferenceTagging
    var myVar = true
}

object vr_SomeObject {
    @VarReferenceTagging
    var myVar = true
}

private object vr_SomePrivateObject {
    @VarReferenceTagging
    var myVar = true
}


class vr_SomeClassWithCompanionObject {
    companion object {
        @VarReferenceTagging
        var myVar = true
    }
}

private class vr_SomePrivateClassWithCompanionObject {
    companion object {
        @VarReferenceTagging
        var myVar = true
    }
}
