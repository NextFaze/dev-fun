@file:Suppress("UNUSED_PARAMETER", "unused", "PackageName", "ClassName", "MemberVisibilityCanBePrivate")

package tested.developer_reference

import com.nextfaze.devfun.annotations.DeveloperReference
import com.nextfaze.devfun.core.FieldReference
import com.nextfaze.devfun.test.WithFieldReferenceTest
import kotlin.reflect.jvm.javaField
import kotlin.test.expect

annotation class VarReferences


class vr_PublicClass : WithFieldReferenceTest {
    @field:DeveloperReference
    var publicVar = true

    override fun testFieldReference(ref: FieldReference) {
        expect(this::publicVar.javaField) { ref.field }
    }
}

class vr_PublicClassWithPrivateVar : WithFieldReferenceTest {
    @field:DeveloperReference
    private var privateVar = true

    override fun testFieldReference(ref: FieldReference) {
        expect(this::privateVar.javaField) { ref.field }
    }
}

private class vr_PrivateClass : WithFieldReferenceTest {
    @field:DeveloperReference
    var publicVar = true

    override fun testFieldReference(ref: FieldReference) {
        expect(this::publicVar.javaField) { ref.field }
    }
}

object vr_PublicObject : WithFieldReferenceTest {
    @field:DeveloperReference
    var publicVar = true

    override fun testFieldReference(ref: FieldReference) {
        expect(this::publicVar.javaField) { ref.field }
    }
}

private object vr_PrivateObject : WithFieldReferenceTest {
    @field:DeveloperReference
    var publicVar = true

    override fun testFieldReference(ref: FieldReference) {
        expect(this::publicVar.javaField) { ref.field }
    }
}

/**
 * Companion object fields are compiled into enclosing class as static fields (synthetic getters/setters on both delegate to that field,
 * hence we get the enclosing class when we use `Field.declaringClass`.
 */
class vr_PublicClassWithCompanionObject : WithFieldReferenceTest {
    companion object {
        @field:DeveloperReference
        var publicVar = true
    }

    override fun testFieldReference(ref: FieldReference) {
        expect(vr_PublicClassWithCompanionObject.Companion::publicVar.javaField) { ref.field }
    }
}

/**
 * Companion object fields are compiled into enclosing class as static fields (synthetic getters/setters on both delegate to that field,
 * hence we get the enclosing class when we use `Field.declaringClass`.
 */
private class vr_PrivateClassWithCompanionObject : WithFieldReferenceTest {
    companion object {
        @field:DeveloperReference
        var publicVar = true
    }

    override fun testFieldReference(ref: FieldReference) {
        expect(vr_PrivateClassWithCompanionObject.Companion::publicVar.javaField) { ref.field }
    }
}
