@file:Suppress("UNUSED_PARAMETER", "unused", "PackageName", "ClassName")

package tested.developer_reference

import com.nextfaze.devfun.annotations.DeveloperAnnotation
import com.nextfaze.devfun.core.MethodReference
import com.nextfaze.devfun.core.getProperties
import kotlin.annotation.AnnotationRetention.SOURCE
import kotlin.annotation.AnnotationTarget.FUNCTION
import kotlin.test.expect

annotation class CustomPropertiesSimpleTypes

@Target(FUNCTION)
@Retention(SOURCE)
@DeveloperAnnotation(developerReference = true)
annotation class HasSimpleTypes(
    // NOTE: can't use 'boolean', 'byte', etc. as they are reserved words in Java realm (should probably create a bug for this as it fails silently...)
    val aBoolean: Boolean,
    val aByte: Byte,
    val aShort: Short,
    val aInt: Int,
    val aLong: Long,
    val aChar: Char,
    val aFloat: Float,
    val aDouble: Double
)

@Target(FUNCTION)
@Retention(SOURCE)
@DeveloperAnnotation(developerReference = true)
annotation class HasSimpleTypesWithDefaults(
    val defaultBoolean: Boolean = true,
    val defaultByte: Byte = 0xA,
    val defaultShort: Short = 12,
    val someInt: Int
)

class cpst_SomeClass {
    @HasSimpleTypes(
        aBoolean = true,
        aByte = 0xD,
        aShort = 0xEA,
        aInt = 0xDBEEF,
        aLong = 0xCAFEBABE,
        aChar = 'N',
        aFloat = 0.123f,
        aDouble = 45.6789
    )
    fun testSimpleTypeProperties(ref: MethodReference) {
        val properties = ref.getProperties<HasSimpleTypesProperties>()
        expect(true) { properties.aBoolean }
        expect(0xD) { properties.aByte }
        expect(0xEA) { properties.aShort }
        expect(0xDBEEF) { properties.aInt }
        expect(0xCAFEBABE) { properties.aLong }
        expect('N') { properties.aChar }
        expect(0.123f) { properties.aFloat }
        expect(45.6789) { properties.aDouble }
    }

    @HasSimpleTypesWithDefaults(
        defaultShort = 99,
        someInt = 123
    )
    fun testSimpleTypePropertiesWithDefaults(ref: MethodReference) {
        val properties = ref.getProperties<HasSimpleTypesWithDefaultsProperties>()
        expect(true) { properties.defaultBoolean }
        expect(0xA) { properties.defaultByte }
        expect(99) { properties.defaultShort }
        expect(123) { properties.someInt }
    }
}
