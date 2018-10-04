@file:Suppress("UNUSED_PARAMETER", "unused", "PackageName", "ClassName")

package tested.developer_reference

import com.nextfaze.devfun.reference.DeveloperReference
import com.nextfaze.devfun.reference.TypeReference
import com.nextfaze.devfun.test.WithTypeReferenceTest
import kotlin.test.expect

annotation class TypeReferences

@DeveloperReference
class tr_PublicClass : WithTypeReferenceTest {
    override fun testTypeReference(ref: TypeReference) {
        expect(ref.type) { tr_PublicClass::class }
    }
}

@DeveloperReference
private class tr_PrivateClass : WithTypeReferenceTest {
    override fun testTypeReference(ref: TypeReference) {
        expect(ref.type) { tr_PrivateClass::class }
    }
}

@DeveloperReference
object tr_PublicObject : WithTypeReferenceTest {
    override fun testTypeReference(ref: TypeReference) {
        expect(ref.type) { tr_PublicObject::class }
    }
}

@DeveloperReference
private object tr_PrivateObject : WithTypeReferenceTest {
    override fun testTypeReference(ref: TypeReference) {
        expect(ref.type) { tr_PrivateObject::class }
    }
}

class tr_PublicClassWithCompanionObject {
    @DeveloperReference
    companion object : WithTypeReferenceTest {
        override fun testTypeReference(ref: TypeReference) {
            expect(ref.type) { tr_PublicClassWithCompanionObject.Companion::class }
        }
    }
}

private class tr_PrivateClassWithCompanionObject {
    @DeveloperReference
    companion object : WithTypeReferenceTest {
        override fun testTypeReference(ref: TypeReference) {
            expect(ref.type) { tr_PrivateClassWithCompanionObject.Companion::class }
        }
    }
}

private class tr_PrivateClassWithPrivateCompanionObject {
    @DeveloperReference
    private companion object : WithTypeReferenceTest {
        override fun testTypeReference(ref: TypeReference) {
            expect(ref.type) { tr_PrivateClassWithPrivateCompanionObject.Companion::class }
        }
    }
}
