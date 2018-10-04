@file:Suppress("UNUSED_PARAMETER", "unused", "PackageName", "MemberVisibilityCanBePrivate", "ClassName")

package tested.developer_reference

import com.nextfaze.devfun.reference.DeveloperReference
import com.nextfaze.devfun.reference.MethodReference
import kotlin.reflect.jvm.javaMethod
import kotlin.test.expect

annotation class ExecutableReferences


class er_PublicClass {
    @DeveloperReference
    fun testPublicFunction(ref: MethodReference) {
        expect(ref.method, this::testPublicFunction::javaMethod)
    }

    @DeveloperReference
    private fun testPrivateFunction(ref: MethodReference) {
        expect(ref.method, this::testPrivateFunction::javaMethod)
    }
}

private class er_PrivateClass {
    @DeveloperReference
    fun testPublicFunction(ref: MethodReference) {
        expect(ref.method, this::testPublicFunction::javaMethod)
    }

    @DeveloperReference
    private fun testPrivateFunction(ref: MethodReference) {
        expect(ref.method, this::testPrivateFunction::javaMethod)
    }
}

object er_PublicObject {
    @DeveloperReference
    fun testPublicFunction(ref: MethodReference) {
        expect(ref.method, this::testPublicFunction::javaMethod)
    }

    @DeveloperReference
    private fun testPrivateFunction(ref: MethodReference) {
        expect(ref.method, this::testPrivateFunction::javaMethod)
    }
}

private object er_PrivateObject {
    @DeveloperReference
    fun testPublicFunction(ref: MethodReference) {
        expect(ref.method, this::testPublicFunction::javaMethod)
    }

    @DeveloperReference
    private fun testPrivateFunction(ref: MethodReference) {
        expect(ref.method, this::testPrivateFunction::javaMethod)
    }
}

class er_PublicClassCompanionObject {
    companion object {
        @DeveloperReference
        fun testPublicFunction(ref: MethodReference) {
            expect(ref.method, this::testPublicFunction::javaMethod)
        }

        @DeveloperReference
        private fun testPrivateFunction(ref: MethodReference) {
            expect(ref.method, this::testPrivateFunction::javaMethod)
        }
    }
}

private class er_PrivateClassCompanionObject {
    companion object SomeName {
        @DeveloperReference
        fun testPublicFunction(ref: MethodReference) {
            expect(ref.method, this::testPublicFunction::javaMethod)
        }

        @DeveloperReference
        private fun testPrivateFunction(ref: MethodReference) {
            expect(ref.method, this::testPrivateFunction::javaMethod)
        }
    }
}

private class er_PrivateClassPrivateCompanionObject {
    private companion object PrivateCompanion {
        @DeveloperReference
        fun testPublicFunction(ref: MethodReference) {
            expect(ref.method, this::testPublicFunction::javaMethod)
        }

        @DeveloperReference
        private fun testPrivateFunction(ref: MethodReference) {
            expect(ref.method, this::testPrivateFunction::javaMethod)
        }
    }
}

@DeveloperReference
fun testPublicTopLevelFunction(ref: MethodReference) {
    expect(ref.method, ::testPublicTopLevelFunction::javaMethod)
}

@DeveloperReference
private fun testPrivateTopLevelFunction(ref: MethodReference) {
    expect(ref.method, ::testPrivateTopLevelFunction::javaMethod)
}

@DeveloperReference
fun String.testPublicTopLevelExtensionFunction(ref: MethodReference) {
    expect(ref.method, ::testPublicTopLevelExtensionFunction::javaMethod)
}

@DeveloperReference
private fun String.testPrivateTopLevelExtensionFunction(ref: MethodReference) {
    expect(ref.method, ::testPrivateTopLevelExtensionFunction::javaMethod)
}
