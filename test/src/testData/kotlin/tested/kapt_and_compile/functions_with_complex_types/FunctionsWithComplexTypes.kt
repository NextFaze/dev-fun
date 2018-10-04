@file:Suppress("UNUSED_PARAMETER", "unused", "PackageName")

package tested.kapt_and_compile.functions_with_complex_types

import com.nextfaze.devfun.function.DeveloperFunction
import java.util.ArrayDeque
import kotlin.reflect.KClass

annotation class FunctionsWithComplexTypes

internal class SimpleTypedClass<T : Number>
internal class TypedTypedClass<T : Number, U : SimpleTypedClass<T>>

internal class ComplexTypesClass {
    fun someFun() = Unit
    internal fun someInternalFun() = Unit
    private fun somePrivateFun() = Unit

    @DeveloperFunction
    fun intArg(arg1: Int) = Unit

    @DeveloperFunction
    fun intArrayArg(arg1: IntArray) = Unit

    @DeveloperFunction
    fun arrayIntArg(arg1: Array<Int>) = Unit

    @DeveloperFunction
    fun listArg(arg1: List<Int>) = Unit

    @DeveloperFunction
    fun intArrays(arg1: Int, arg2: Array<Int>, arg3: Array<Array<Int>>) = Unit

    @DeveloperFunction
    fun listArrayLong(arg1: List<Array<Long>>) = Unit

    @DeveloperFunction
    fun arrayListLong(arg1: ArrayList<in Long>) = Unit

    @DeveloperFunction
    fun arrayDequeLong(arg1: ArrayDeque<out Long>) = Unit

    @DeveloperFunction
    fun <T : TypedTypedClass<*, *>> kClass(arg1: KClass<out T>) = Unit

    @DeveloperFunction
    fun various1(arg1: Short, arg2: IntArray, arg3: List<Array<in Long>>) = Unit

    @DeveloperFunction
    fun various2(arg1: Int, arg2: Array<out Int>) = Unit

    @DeveloperFunction
    fun various3(arg1: Long, arg2: Array<in Number>, arg3: Array<out Array<Int>>) = Unit

    @DeveloperFunction
    fun various4(arg1: Byte, arg2: ByteArray, arg3: Array<ComplexTypesClass>) = Unit

    @DeveloperFunction
    fun various5(arg1: Char, arg2: Array<CharSequence>, arg3: List<Boolean>) = Unit

    @DeveloperFunction
    fun various6(arg1: Boolean, arg2: Iterable<Array<List<Float>>>) = Unit

    @DeveloperFunction
    fun various7(arg1: Float, arg2: DoubleArray) = Unit

    @DeveloperFunction
    fun various8(arg1: Double, arg2: List<Iterable<Array<SimpleTypedClass<Float>>>>) = Unit

    @DeveloperFunction
    fun various9(
        arg1: Short, arg2: Int, arg3: Long, arg4: Byte, arg5: Char, arg6: Boolean, arg7: Float, arg8: Double,
        arg9: Array<ComplexTypesClass>,
        arg10: List<Iterable<Array<TypedTypedClass<in Float, SimpleTypedClass<Float>>>>>
    ) = Unit
}

private class PrivateComplexTypesClass {
    fun someFun() = Unit
    internal fun someInternalFun() = Unit
    private fun somePrivateFun() = Unit

    @DeveloperFunction
    fun intArg(arg1: Int) = Unit

    @DeveloperFunction
    fun intArrayArg(arg1: IntArray) = Unit

    @DeveloperFunction
    fun arrayIntArg(arg1: Array<Int>) = Unit

    @DeveloperFunction
    fun listArg(arg1: List<Int>) = Unit

    @DeveloperFunction
    fun intArrays(arg1: Int, arg2: Array<Int>, arg3: Array<Array<Int>>) = Unit

    @DeveloperFunction
    fun listArrayLong(arg1: List<Array<Long>>) = Unit

    @DeveloperFunction
    fun arrayListLong(arg1: ArrayList<Long>) = Unit

    @DeveloperFunction
    fun arrayDequeLong(arg1: ArrayDeque<Long>) = Unit

    @DeveloperFunction
    fun <T : TypedTypedClass<*, *>> kClass(arg1: KClass<out T>) = Unit

    @DeveloperFunction
    fun various1(arg1: Short, arg2: IntArray, arg3: List<Array<Long>>) = Unit

    @DeveloperFunction
    fun various2(arg1: Int, arg2: Array<Int>) = Unit

    @DeveloperFunction
    fun various3(arg1: Long, arg2: Array<Number>, arg3: Array<Array<Int>>) = Unit

    @DeveloperFunction
    fun various4(arg1: Byte, arg2: ByteArray, arg3: Array<ComplexTypesClass>) = Unit

    @DeveloperFunction
    fun various5(arg1: Char, arg2: Array<CharSequence>, arg3: List<Boolean>) = Unit

    @DeveloperFunction
    fun various6(arg1: Boolean, arg2: Iterable<Array<List<Float>>>) = Unit

    @DeveloperFunction
    fun various7(arg1: Float, arg2: DoubleArray) = Unit

    @DeveloperFunction
    fun various8(arg1: Double, arg2: List<Iterable<Array<SimpleTypedClass<Float>>>>) = Unit

    @DeveloperFunction
    fun various9(
        arg1: Short, arg2: Int, arg3: Long, arg4: Byte, arg5: Char, arg6: Boolean, arg7: Float, arg8: Double,
        arg9: Array<ComplexTypesClass>,
        arg10: List<Iterable<Array<TypedTypedClass<Float, SimpleTypedClass<Float>>>>>
    ) = Unit
}
