@file:Suppress("UNUSED_PARAMETER", "unused", "PackageName", "ClassName")

package tested.developer_reference

import com.nextfaze.devfun.annotations.DeveloperAnnotation
import com.nextfaze.devfun.core.MethodReference
import com.nextfaze.devfun.core.getProperties
import com.nextfaze.devfun.test.expectArrayOf
import kotlin.annotation.AnnotationRetention.SOURCE
import kotlin.annotation.AnnotationTarget.FUNCTION

annotation class CustomPropertiesSimpleArrayTypes

@Target(FUNCTION)
@Retention(SOURCE)
@DeveloperAnnotation(developerReference = true)
annotation class HasSimpleArrayTypes(
    val booleans: BooleanArray,
    val bytes: ByteArray,
    val shorts: ShortArray,
    val ints: IntArray,
    val longs: LongArray,
    val chars: CharArray,
    val floats: FloatArray,
    val doubles: DoubleArray
)

@Target(FUNCTION)
@Retention(SOURCE)
@DeveloperAnnotation(developerReference = true)
annotation class HasSimpleArrayTypesWithDefaults(
    val defaultBooleans: BooleanArray = [false, false, true, true],
    val defaultBytes: ByteArray = [0xA, 0xB, 0xC],
    val defaultShorts: ShortArray = [12, 34, 56],
    val someInts: IntArray
)

class cpsat_SomeClass {
    @HasSimpleArrayTypes(
        booleans = [false, true, false, false],
        bytes = [0xD, 0xE, 0xA, 0xD],
        shorts = [0xBE, 0xEF],
        ints = [0xCAFE, 0xBABE],
        longs = [214748383, 214748581, 214748803, 214749011, 214749211, 214749397],
        chars = ['N', 'e', 'x', 't', 'F', 'a', 'z', 'e'],
        floats = [0.123f, 4.567f, 89.1010f],
        doubles = [0.0, 0.00001, 1234567.8, -0.0]
    )
    fun testSimpleTypeArrayTypeProperties(ref: MethodReference) {
        val properties = ref.getProperties<HasSimpleArrayTypesProperties>()
        expectArrayOf(false, true, false, false) { properties.booleans }
        expectArrayOf<Byte>(0xD, 0xE, 0xA, 0xD) { properties.bytes }
        expectArrayOf<Short>(0xBE, 0xEF) { properties.shorts }
        expectArrayOf(0xCAFE, 0xBABE) { properties.ints }
        expectArrayOf(214748383L, 214748581L, 214748803L, 214749011L, 214749211L, 214749397L) { properties.longs }
        expectArrayOf('N', 'e', 'x', 't', 'F', 'a', 'z', 'e') { properties.chars }
        expectArrayOf(0.123f, 4.567f, 89.1010f) { properties.floats }
        expectArrayOf(0.0, 0.00001, 1234567.8, -0.0) { properties.doubles }
    }

    @HasSimpleArrayTypesWithDefaults(
        defaultBooleans = [true, true, false, true],
        someInts = [1, 2, 3, 4]
    )
    fun testSimpleTypeArrayTypePropertiesWithDefaults(ref: MethodReference) {
        val properties = ref.getProperties<HasSimpleArrayTypesWithDefaultsProperties>()
        expectArrayOf(true, true, false, true) { properties.defaultBooleans }
        expectArrayOf<Byte>(0xA, 0xB, 0xC) { properties.defaultBytes }
        expectArrayOf<Short>(12, 34, 56) { properties.defaultShorts }
        expectArrayOf(1, 2, 3, 4) { properties.someInts }
    }
}
