@file:Suppress("UNUSED_PARAMETER", "unused", "PackageName", "ClassName")

package tested.developer_annotation

import com.nextfaze.devfun.annotations.DeveloperAnnotation
import com.nextfaze.devfun.core.DeveloperMethodReference
import com.nextfaze.devfun.core.DeveloperReference
import com.nextfaze.devfun.core.FunctionTransformer
import com.nextfaze.devfun.core.SingleFunctionTransformer
import com.nextfaze.devfun.test.expectArrayOf
import kotlin.annotation.AnnotationRetention.SOURCE
import kotlin.annotation.AnnotationTarget.FUNCTION
import kotlin.reflect.KClass
import kotlin.test.expect

annotation class CustomPropertiesClassTypes

@Target(FUNCTION)
@Retention(SOURCE)
@DeveloperAnnotation
annotation class HasClassProperties(
    val clazz: KClass<*>,
    val primitiveClass: KClass<*>,
    val typedClazz: KClass<*>,
    val nestedTypedStarClass: KClass<*>,
    val nestedTypedExplicitClass: KClass<*>,
    val privateClass: KClass<*>,
    val packagePrivateClass: KClass<*>,
    val privateTypedClass: KClass<*>,
    val packagePrivateTypedClass: KClass<*>,
    val multipleTypedClass: KClass<*>
)

@Target(FUNCTION)
@Retention(SOURCE)
@DeveloperAnnotation
annotation class HasArrayClassProperties(
    val booleanArray: KClass<*>,
    val byteArray: KClass<*>,
    val shortArray: KClass<*>,
    val intArray: KClass<*>,
    val longArray: KClass<*>,
    val charArray: KClass<*>,
    val floatArray: KClass<*>,
    val doubleArray: KClass<*>
)

@Target(FUNCTION)
@Retention(SOURCE)
@DeveloperAnnotation
annotation class HasClassPropertiesWithDefaults(
    val defaultClass: KClass<*> = Any::class,
    val defaultPrimitiveClass: KClass<*> = BooleanArray::class,
    val defaultTypedClazz: KClass<*> = MyInterface::class
)

@Target(FUNCTION)
@Retention(SOURCE)
@DeveloperAnnotation
annotation class HasClassArrayPropertiesWithDefaults(
    val defaultClasses: Array<KClass<*>> = [Any::class, Int::class, BooleanArray::class],
    val optionalClasses: Array<KClass<*>> = [Boolean::class],
    val classes: Array<KClass<*>>
)

private class PrivateClass
class TypedClass<T : DeveloperReference>

interface MyInterface<T : FunctionTransformer>
class NestedTypedStarClass<T : MyInterface<*>>
class NestedTypedExplicitTypedClass<T : MyInterface<SingleFunctionTransformer>>

private class PrivateTypedClass<T : FunctionTransformer>
internal class PackagePrivateTypedClass<T : PackagePrivateType>

class MultipleTypedClass<T : MyInterface<*>, U : TypedClass<*>>

class cpct_SomeClass {
    @HasClassProperties(
        clazz = cpct_SomeClass::class,
        primitiveClass = Byte::class,
        typedClazz = TypedClass::class,
        nestedTypedStarClass = NestedTypedStarClass::class,
        nestedTypedExplicitClass = NestedTypedExplicitTypedClass::class,
        privateClass = PrivateClass::class,
        packagePrivateClass = PackagePrivateType::class,
        privateTypedClass = PrivateTypedClass::class,
        packagePrivateTypedClass = PackagePrivateTypedClass::class,
        multipleTypedClass = MultipleTypedClass::class
    )
    fun testClassProperties(ref: DeveloperMethodReference) {
        val properties = ref.properties!!
        expect(cpct_SomeClass::class) { properties["clazz"] }
        expect(Byte::class) { properties["primitiveClass"] }
        expect(TypedClass::class) { properties["typedClazz"] }
        expect(NestedTypedStarClass::class) { properties["nestedTypedStarClass"] }
        expect(NestedTypedExplicitTypedClass::class) { properties["nestedTypedExplicitClass"] }
        expect(PrivateClass::class) { properties["privateClass"] }
        expect(PackagePrivateType::class) { properties["packagePrivateClass"] }
        expect(PrivateTypedClass::class) { properties["privateTypedClass"] }
        expect(PackagePrivateTypedClass::class) { properties["packagePrivateTypedClass"] }
        expect(MultipleTypedClass::class) { properties["multipleTypedClass"] }
    }

    @HasArrayClassProperties(
        booleanArray = BooleanArray::class,
        byteArray = ByteArray::class,
        shortArray = ShortArray::class,
        intArray = IntArray::class,
        longArray = LongArray::class,
        charArray = CharArray::class,
        floatArray = FloatArray::class,
        doubleArray = DoubleArray::class
    )
    fun testArrayClassProperties(ref: DeveloperMethodReference) {
        val properties = ref.properties!!
        expect(BooleanArray::class) { properties["booleanArray"] }
        expect(ByteArray::class) { properties["byteArray"] }
        expect(ShortArray::class) { properties["shortArray"] }
        expect(IntArray::class) { properties["intArray"] }
        expect(LongArray::class) { properties["longArray"] }
        expect(CharArray::class) { properties["charArray"] }
        expect(FloatArray::class) { properties["floatArray"] }
        expect(DoubleArray::class) { properties["doubleArray"] }
    }

    @HasClassPropertiesWithDefaults
    fun testSimpleTypesWithDefaults(ref: DeveloperMethodReference) {
        val properties = ref.properties!!
        expect(Any::class) { properties["defaultClass"] }
        expect(BooleanArray::class) { properties["defaultPrimitiveClass"] }
        expect(MyInterface::class) { properties["defaultTypedClazz"] }
    }

    @HasClassArrayPropertiesWithDefaults(
        optionalClasses = [Int::class, cpct_SomeClass::class, ByteArray::class, PrivateClass::class, NestedTypedExplicitTypedClass::class],
        classes = [
            HasClassArrayPropertiesWithDefaults::class,
            NestedTypedStarClass::class,
            PackagePrivateTypedClass::class,
            Array<Boolean>::class,
            Array<HasClassPropertiesWithDefaults>::class,
            Array<PrivateClass>::class,
            Array<PackagePrivateType>::class
        ]
    )
    fun testClassArrayPropertiesWithDefaults(ref: DeveloperMethodReference) {
        val properties = ref.properties!!
        expectArrayOf(
            Int::class,
            cpct_SomeClass::class,
            ByteArray::class,
            PrivateClass::class,
            NestedTypedExplicitTypedClass::class
        ) { properties["optionalClasses"] }
        expectArrayOf(
            HasClassArrayPropertiesWithDefaults::class,
            NestedTypedStarClass::class,
            PackagePrivateTypedClass::class,
            Array<Boolean>::class,
            Array<HasClassPropertiesWithDefaults>::class,
            Array<PrivateClass>::class,
            Array<PackagePrivateType>::class
        ) { properties["classes"] }
    }
}
