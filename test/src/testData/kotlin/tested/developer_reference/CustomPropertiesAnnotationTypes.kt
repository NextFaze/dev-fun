@file:Suppress("UNUSED_PARAMETER", "unused", "PackageName", "ClassName")

package tested.developer_reference

import com.nextfaze.devfun.annotations.DeveloperAnnotation
import com.nextfaze.devfun.core.MethodReference
import com.nextfaze.devfun.core.ReferenceDefinition
import com.nextfaze.devfun.core.getProperties
import com.nextfaze.devfun.test.expectArrayOf
import kotlin.annotation.AnnotationRetention.SOURCE
import kotlin.annotation.AnnotationTarget.FUNCTION
import kotlin.reflect.KClass
import kotlin.test.expect


annotation class MyAnnotation(
    val intArray: IntArray = [1, 2, 3],
    val nested: NestedAnnotation = NestedAnnotation(
        intArray = intArrayOf(1, 2, 3),
        classArray = [Boolean::class]
    )
)

annotation class NestedAnnotation(
    val intArray: IntArray,
    val classArray: Array<KClass<*>>
)


annotation class CustomPropertiesAnnotationTypes

@Target(FUNCTION)
@Retention(SOURCE)
@DeveloperAnnotation(developerReference = true)
internal annotation class HasAnnotations(
    val simpleTypesWithDefaults: SimpleTypesWithDefaults =
        SimpleTypesWithDefaults(
            defaultByte = 112,
            someInt = 123
        ),
    val anotherSimpleTypesWithDefaults: SimpleTypesWithDefaults =
        SimpleTypesWithDefaults(
            someInt = 999
        ),
    val arrayTypesWithDefaults: ArrayTypesWithDefaults =
        ArrayTypesWithDefaults(
            defaultBooleans = booleanArrayOf(true, true), // cannot use short-hand notation KT-25656
            defaultBytes = byteArrayOf(),
            someFloats = floatArrayOf(0.123f, 4.567f),
            someInts = intArrayOf(1, 2, 3)
        ),
    val classArrayPropertiesWithDefaults: ClassArraysWithDefaults =
        ClassArraysWithDefaults(
            optionalClasses = [Array<String>::class, CustomPropertiesAnnotationTypes::class],
            classes = []
        ),
    val complexTypesWithDefaults: ComplexTypesWithDefaults =
        ComplexTypesWithDefaults(
            normalPets = [],
            myPet = Animal.OTTER,
            myColor = Color.PURPLE,
            myAge = 2,
            myColors = [Color.PINK, Color.PURPLE]
        ),
    val nestedAllDefault: Nested = Nested(),
    val nestedCustom: Nested = Nested(
        simpleTypesWithDefaults = SimpleTypesWithDefaults(
            defaultBoolean = false,
            someInt = 1337
        ),
        arrayTypesWithDefaults = ArrayTypesWithDefaults(
            defaultShorts = shortArrayOf(6, 7, 8),
            someFloats = floatArrayOf(-1.0f),
            someInts = intArrayOf()
        ),
        classArrayPropertiesWithDefaults = ClassArraysWithDefaults(
            badClasses = [],
            classes = [Animal::class, Color::class, Nothing::class]
        ),
        complexTypesWithDefaults = ComplexTypesWithDefaults(
            aString = "I'm nested!",
            myAge = 99,
            myColors = [],
            myColor = Color.GREEN,
            myPet = Animal.DOG
        ),
        arrayOfAnnotations = [SimpleTypesWithDefaults(someInt = 0), SimpleTypesWithDefaults(someInt = 2)]
    ),
    val emptyArrayOfAnnotations: Array<PackagePrivateEmptyAnnotation> = [],
    val arrayOfAnnotations: Array<SimpleTypesWithDefaults> = [SimpleTypesWithDefaults(someInt = 1), SimpleTypesWithDefaults(someInt = 2)]
)

annotation class SimpleTypesWithDefaults(
    val defaultBoolean: Boolean = true,
    val defaultByte: Byte = 0xA,
    val defaultShort: Short = 12,
    val someInt: Int
)

annotation class ArrayTypesWithDefaults(
    val defaultBooleans: BooleanArray = [false, false, true, true],
    val defaultBytes: ByteArray = [0xA, 0xB, 0xC],
    val defaultShorts: ShortArray = [12, 34, 56],
    val someFloats: FloatArray,
    val someInts: IntArray
)

annotation class ClassArraysWithDefaults(
    val defaultClasses: Array<KClass<*>> = [Any::class, Int::class, BooleanArray::class, Unit::class, Nothing::class],
    val badClasses: Array<KClass<*>> = [Char::class, SimpleTypesWithDefaults::class],
    val optionalClasses: Array<KClass<*>> = [Boolean::class],
    val classes: Array<KClass<*>>
)

annotation class ComplexTypesWithDefaults(
    val aString: String = "Hello World",
    val normalPets: Array<Animal> = [Animal.DOG, Animal.CAT, Animal.BIRD],
    val primaryColors: Array<Color> = [Color.RED, Color.GREEN, Color.BLUE],
    val myPet: Animal,
    val myColor: Color,
    val myAge: Int,
    val myColors: Array<Color>
)

enum class Color { RED, GREEN, BLUE, AQUA, PINK, PURPLE }
enum class Animal { DOG, CAT, OTTER, BIRD }

annotation class Nested(
    val simpleTypesWithDefaults: SimpleTypesWithDefaults =
        SimpleTypesWithDefaults(
            defaultByte = 13,
            someInt = 456
        ),
    val anotherSimpleTypesWithDefaults: SimpleTypesWithDefaults =
        SimpleTypesWithDefaults(
            someInt = 777
        ),
    val arrayTypesWithDefaults: ArrayTypesWithDefaults =
        ArrayTypesWithDefaults(
            defaultBooleans = booleanArrayOf(false, false), // cannot use short-hand notation KT-25656
            defaultBytes = byteArrayOf(1, 2, 3),
            someFloats = floatArrayOf(),
            someInts = intArrayOf(3, 2, 1)
        ),
    val classArrayPropertiesWithDefaults: ClassArraysWithDefaults =
        ClassArraysWithDefaults(
            optionalClasses = [Array<Boolean>::class, SimpleTypesWithDefaults::class],
            classes = []
        ),
    val complexTypesWithDefaults: ComplexTypesWithDefaults =
        ComplexTypesWithDefaults(
            myPet = Animal.BIRD,
            myColor = Color.PINK,
            myAge = 12,
            myColors = [Color.PINK, Color.PURPLE]
        ),
    val arrayOfAnnotations: Array<SimpleTypesWithDefaults> = [SimpleTypesWithDefaults(someInt = 1), SimpleTypesWithDefaults(someInt = 2)]
)

@DeveloperAnnotation(developerReference = true)
annotation class HasNestedStringArrays(
    val hasStringArray: HasStringArray,
    val hasStringArrays: Array<HasStringArray>
)

annotation class HasStringArray(
    val value: Array<String>
)

@Suppress("UNCHECKED_CAST")
class cpat_SomeClass {
    @HasAnnotations
    fun testAllDefaults(ref: MethodReference) {
        val properties = ref.getProperties<HasAnnotationsProperties>()

        run simpleTypesWithDefaults@{
            val simpleTypesWithDefaults = properties.simpleTypesWithDefaults
            expect(true) { simpleTypesWithDefaults.defaultBoolean }
            expect(112) { simpleTypesWithDefaults.defaultByte }
            expect(12) { simpleTypesWithDefaults.defaultShort }
            expect(123) { simpleTypesWithDefaults.someInt }

            val anotherSimpleTypesWithDefaults = properties.anotherSimpleTypesWithDefaults
            expect(true) { anotherSimpleTypesWithDefaults.defaultBoolean }
            expect(0xA) { anotherSimpleTypesWithDefaults.defaultByte }
            expect(12) { anotherSimpleTypesWithDefaults.defaultShort }
            expect(999) { anotherSimpleTypesWithDefaults.someInt }

            val arrayTypesWithDefaults = properties.arrayTypesWithDefaults
            expectArrayOf(true, true) { arrayTypesWithDefaults.defaultBooleans }
            expectArrayOf<Byte> { arrayTypesWithDefaults.defaultBytes }
            expectArrayOf<Short>(12, 34, 56) { arrayTypesWithDefaults.defaultShorts }
            expectArrayOf(0.123f, 4.567f) { arrayTypesWithDefaults.someFloats }
            expectArrayOf(1, 2, 3) { arrayTypesWithDefaults.someInts }

            val classArrayPropertiesWithDefaults = properties.classArrayPropertiesWithDefaults
            expectArrayOf(Any::class, Int::class, BooleanArray::class, Unit::class, Nothing::class) {
                classArrayPropertiesWithDefaults.defaultClasses
            }
            expectArrayOf(Char::class, SimpleTypesWithDefaults::class) { classArrayPropertiesWithDefaults.badClasses }
            expectArrayOf(Array<String>::class, CustomPropertiesAnnotationTypes::class) {
                classArrayPropertiesWithDefaults.optionalClasses
            }
            expectArrayOf<KClass<*>> { classArrayPropertiesWithDefaults.classes }

            val complexTypesWithDefaults = properties.complexTypesWithDefaults
            expect("Hello World") { complexTypesWithDefaults.aString }
            expectArrayOf<Enum<*>> { complexTypesWithDefaults.normalPets }
            expectArrayOf(Color.RED, Color.GREEN, Color.BLUE) { complexTypesWithDefaults.primaryColors }
            expect(Animal.OTTER) { complexTypesWithDefaults.myPet }
            expect(Color.PURPLE) { complexTypesWithDefaults.myColor }
            expect(2) { complexTypesWithDefaults.myAge }
            expectArrayOf(Color.PINK, Color.PURPLE) { complexTypesWithDefaults.myColors }

            val emptyArrayOfAnnotations = properties.emptyArrayOfAnnotations
            expectArrayOf<PackagePrivateEmptyAnnotation> { emptyArrayOfAnnotations }

            val arrayOfAnnotations = properties.arrayOfAnnotations
            arrayOfAnnotations.forEachIndexed { index, map ->
                expect(true) { map.defaultBoolean }
                expect(0xA) { map.defaultByte }
                expect(12) { map.defaultShort }
                expect(index + 1) { map.someInt }
            }
        }

        run nestedAllDefault@{
            val nestedAllDefault = properties.nestedAllDefault

            val simpleTypesWithDefaultsNested = nestedAllDefault.simpleTypesWithDefaults
            expect(true) { simpleTypesWithDefaultsNested.defaultBoolean }
            expect(13) { simpleTypesWithDefaultsNested.defaultByte }
            expect(12) { simpleTypesWithDefaultsNested.defaultShort }
            expect(456) { simpleTypesWithDefaultsNested.someInt }

            val anotherSimpleTypesWithDefaultsNested = nestedAllDefault.anotherSimpleTypesWithDefaults
            expect(true) { anotherSimpleTypesWithDefaultsNested.defaultBoolean }
            expect(0xA) { anotherSimpleTypesWithDefaultsNested.defaultByte }
            expect(12) { anotherSimpleTypesWithDefaultsNested.defaultShort }
            expect(777) { anotherSimpleTypesWithDefaultsNested.someInt }

            val arrayTypesWithDefaults = nestedAllDefault.arrayTypesWithDefaults
            expectArrayOf(false, false) { arrayTypesWithDefaults.defaultBooleans }
            expectArrayOf<Byte>(1, 2, 3) { arrayTypesWithDefaults.defaultBytes }
            expectArrayOf<Short>(12, 34, 56) { arrayTypesWithDefaults.defaultShorts }
            expectArrayOf<Float> { arrayTypesWithDefaults.someFloats }
            expectArrayOf(3, 2, 1) { arrayTypesWithDefaults.someInts }

            val classArrayPropertiesWithDefaultsNested = nestedAllDefault.classArrayPropertiesWithDefaults
            expectArrayOf(Any::class, Int::class, BooleanArray::class, Unit::class, Nothing::class) {
                classArrayPropertiesWithDefaultsNested.defaultClasses
            }
            expectArrayOf(Char::class, SimpleTypesWithDefaults::class) { classArrayPropertiesWithDefaultsNested.badClasses }
            expectArrayOf(Array<Boolean>::class, SimpleTypesWithDefaults::class) {
                classArrayPropertiesWithDefaultsNested.optionalClasses
            }
            expectArrayOf<KClass<*>> { classArrayPropertiesWithDefaultsNested.classes }

            val complexTypesWithDefaultsNested = nestedAllDefault.complexTypesWithDefaults
            expect("Hello World") { complexTypesWithDefaultsNested.aString }
            expectArrayOf(Animal.DOG, Animal.CAT, Animal.BIRD) { complexTypesWithDefaultsNested.normalPets }
            expectArrayOf(Color.RED, Color.GREEN, Color.BLUE) { complexTypesWithDefaultsNested.primaryColors }
            expect(Animal.BIRD) { complexTypesWithDefaultsNested.myPet }
            expect(Color.PINK) { complexTypesWithDefaultsNested.myColor }
            expect(12) { complexTypesWithDefaultsNested.myAge }
            expectArrayOf(Color.PINK, Color.PURPLE) { complexTypesWithDefaultsNested.myColors }

            val arrayOfAnnotations = nestedAllDefault.arrayOfAnnotations
            arrayOfAnnotations.forEachIndexed { index, map ->
                expect(true) { map.defaultBoolean }
                expect(0xA) { map.defaultByte }
                expect(12) { map.defaultShort }
                expect(index + 1) { map.someInt }
            }
        }

        run nestedCustom@{
            val nestedCustom = properties.nestedCustom

            val simpleTypesWithDefaultsNestedCustom = nestedCustom.simpleTypesWithDefaults
            expect(false) { simpleTypesWithDefaultsNestedCustom.defaultBoolean }
            expect(0xA) { simpleTypesWithDefaultsNestedCustom.defaultByte }
            expect(12) { simpleTypesWithDefaultsNestedCustom.defaultShort }
            expect(1337) { simpleTypesWithDefaultsNestedCustom.someInt }

            val anotherSimpleTypesWithDefaultsNestedCustom = nestedCustom.anotherSimpleTypesWithDefaults
            expect(true) { anotherSimpleTypesWithDefaultsNestedCustom.defaultBoolean }
            expect(0xA) { anotherSimpleTypesWithDefaultsNestedCustom.defaultByte }
            expect(12) { anotherSimpleTypesWithDefaultsNestedCustom.defaultShort }
            expect(777) { anotherSimpleTypesWithDefaultsNestedCustom.someInt }

            val arrayTypesWithDefaults = nestedCustom.arrayTypesWithDefaults
            expectArrayOf(false, false) { arrayTypesWithDefaults.defaultBooleans }
            expectArrayOf<Byte>(0xA, 0xB, 0xC) { arrayTypesWithDefaults.defaultBytes }
            expectArrayOf<Short>(6, 7, 8) { arrayTypesWithDefaults.defaultShorts }
            expectArrayOf(-1.0f) { arrayTypesWithDefaults.someFloats }
            expectArrayOf<Int> { arrayTypesWithDefaults.someInts }

            val classArrayPropertiesWithDefaultsNestedCustom = nestedCustom.classArrayPropertiesWithDefaults
            expectArrayOf(Any::class, Int::class, BooleanArray::class, Unit::class, Nothing::class) {
                classArrayPropertiesWithDefaultsNestedCustom.defaultClasses
            }
            expectArrayOf<KClass<*>> { classArrayPropertiesWithDefaultsNestedCustom.badClasses }
            expectArrayOf(Boolean::class) { classArrayPropertiesWithDefaultsNestedCustom.optionalClasses }
            expectArrayOf(Animal::class, Color::class, Nothing::class) { classArrayPropertiesWithDefaultsNestedCustom.classes }

            val complexTypesWithDefaultsNestedCustom = nestedCustom.complexTypesWithDefaults
            expect("I'm nested!") { complexTypesWithDefaultsNestedCustom.aString }
            expectArrayOf(Animal.DOG, Animal.CAT, Animal.BIRD) { complexTypesWithDefaultsNestedCustom.normalPets }
            expectArrayOf(Color.RED, Color.GREEN, Color.BLUE) { complexTypesWithDefaultsNestedCustom.primaryColors }
            expect(Animal.DOG) { complexTypesWithDefaultsNestedCustom.myPet }
            expect(Color.GREEN) { complexTypesWithDefaultsNestedCustom.myColor }
            expect(99) { complexTypesWithDefaultsNestedCustom.myAge }
            expectArrayOf<Enum<*>> { complexTypesWithDefaultsNestedCustom.myColors }

            val arrayOfAnnotations = nestedCustom.arrayOfAnnotations
            arrayOfAnnotations.forEachIndexed { index, map ->
                expect(true) { map.defaultBoolean }
                expect(0xA) { map.defaultByte }
                expect(12) { map.defaultShort }
                expect(index * 2) { map.someInt }
            }
        }
    }

    @HasAnnotations(
        anotherSimpleTypesWithDefaults = SimpleTypesWithDefaults(
            defaultBoolean = false,
            someInt = 163
        ),
        nestedCustom = Nested(
            complexTypesWithDefaults = ComplexTypesWithDefaults(
                normalPets = [],
                myAge = 50,
                myColor = Color.PINK,
                myColors = [Color.RED],
                myPet = Animal.OTTER
            )
        )
    )
    fun testWithUseSiteValues(ref: MethodReference) {
        val properties = ref.getProperties<HasAnnotationsProperties>()

        run simpleTypesWithDefaults@{
            val simpleTypesWithDefaults = properties.simpleTypesWithDefaults
            expect(true) { simpleTypesWithDefaults.defaultBoolean }
            expect(112) { simpleTypesWithDefaults.defaultByte }
            expect(12) { simpleTypesWithDefaults.defaultShort }
            expect(123) { simpleTypesWithDefaults.someInt }

            val anotherSimpleTypesWithDefaults = properties.anotherSimpleTypesWithDefaults
            expect(false) { anotherSimpleTypesWithDefaults.defaultBoolean }
            expect(0xA) { anotherSimpleTypesWithDefaults.defaultByte }
            expect(12) { anotherSimpleTypesWithDefaults.defaultShort }
            expect(163) { anotherSimpleTypesWithDefaults.someInt }

            val arrayTypesWithDefaults = properties.arrayTypesWithDefaults
            expectArrayOf(true, true) { arrayTypesWithDefaults.defaultBooleans }
            expectArrayOf<Byte> { arrayTypesWithDefaults.defaultBytes }
            expectArrayOf<Short>(12, 34, 56) { arrayTypesWithDefaults.defaultShorts }
            expectArrayOf(0.123f, 4.567f) { arrayTypesWithDefaults.someFloats }
            expectArrayOf(1, 2, 3) { arrayTypesWithDefaults.someInts }

            val classArrayPropertiesWithDefaults = properties.classArrayPropertiesWithDefaults
            expectArrayOf(Any::class, Int::class, BooleanArray::class, Unit::class, Nothing::class) {
                classArrayPropertiesWithDefaults.defaultClasses
            }
            expectArrayOf(Char::class, SimpleTypesWithDefaults::class) { classArrayPropertiesWithDefaults.badClasses }
            expectArrayOf(Array<String>::class, CustomPropertiesAnnotationTypes::class) {
                classArrayPropertiesWithDefaults.optionalClasses
            }
            expectArrayOf<KClass<*>> { classArrayPropertiesWithDefaults.classes }

            val complexTypesWithDefaults = properties.complexTypesWithDefaults
            expect("Hello World") { complexTypesWithDefaults.aString }
            expectArrayOf<Enum<*>> { complexTypesWithDefaults.normalPets }
            expectArrayOf(Color.RED, Color.GREEN, Color.BLUE) { complexTypesWithDefaults.primaryColors }
            expect(Animal.OTTER) { complexTypesWithDefaults.myPet }
            expect(Color.PURPLE) { complexTypesWithDefaults.myColor }
            expect(2) { complexTypesWithDefaults.myAge }
            expectArrayOf(Color.PINK, Color.PURPLE) { complexTypesWithDefaults.myColors }

            val emptyArrayOfAnnotations = properties.emptyArrayOfAnnotations
            expectArrayOf<PackagePrivateEmptyAnnotation> { emptyArrayOfAnnotations }

            val arrayOfAnnotations = properties.arrayOfAnnotations
            arrayOfAnnotations.forEachIndexed { index, map ->
                expect(true) { map.defaultBoolean }
                expect(0xA) { map.defaultByte }
                expect(12) { map.defaultShort }
                expect(index + 1) { map.someInt }
            }
        }

        run nestedAllDefault@{
            val nestedAllDefault = properties.nestedAllDefault

            val simpleTypesWithDefaultsNested = nestedAllDefault.simpleTypesWithDefaults
            expect(true) { simpleTypesWithDefaultsNested.defaultBoolean }
            expect(13) { simpleTypesWithDefaultsNested.defaultByte }
            expect(12) { simpleTypesWithDefaultsNested.defaultShort }
            expect(456) { simpleTypesWithDefaultsNested.someInt }

            val anotherSimpleTypesWithDefaultsNested = nestedAllDefault.anotherSimpleTypesWithDefaults
            expect(true) { anotherSimpleTypesWithDefaultsNested.defaultBoolean }
            expect(0xA) { anotherSimpleTypesWithDefaultsNested.defaultByte }
            expect(12) { anotherSimpleTypesWithDefaultsNested.defaultShort }
            expect(777) { anotherSimpleTypesWithDefaultsNested.someInt }

            val arrayTypesWithDefaults = nestedAllDefault.arrayTypesWithDefaults
            expectArrayOf(false, false) { arrayTypesWithDefaults.defaultBooleans }
            expectArrayOf<Byte>(1, 2, 3) { arrayTypesWithDefaults.defaultBytes }
            expectArrayOf<Short>(12, 34, 56) { arrayTypesWithDefaults.defaultShorts }
            expectArrayOf<Float> { arrayTypesWithDefaults.someFloats }
            expectArrayOf(3, 2, 1) { arrayTypesWithDefaults.someInts }

            val classArrayPropertiesWithDefaultsNested = nestedAllDefault.classArrayPropertiesWithDefaults
            expectArrayOf(Any::class, Int::class, BooleanArray::class, Unit::class, Nothing::class) {
                classArrayPropertiesWithDefaultsNested.defaultClasses
            }
            expectArrayOf(Char::class, SimpleTypesWithDefaults::class) { classArrayPropertiesWithDefaultsNested.badClasses }
            expectArrayOf(Array<Boolean>::class, SimpleTypesWithDefaults::class) {
                classArrayPropertiesWithDefaultsNested.optionalClasses
            }
            expectArrayOf<KClass<*>> { classArrayPropertiesWithDefaultsNested.classes }

            val complexTypesWithDefaultsNested = nestedAllDefault.complexTypesWithDefaults
            expect("Hello World") { complexTypesWithDefaultsNested.aString }
            expectArrayOf(Animal.DOG, Animal.CAT, Animal.BIRD) { complexTypesWithDefaultsNested.normalPets }
            expectArrayOf(Color.RED, Color.GREEN, Color.BLUE) { complexTypesWithDefaultsNested.primaryColors }
            expect(Animal.BIRD) { complexTypesWithDefaultsNested.myPet }
            expect(Color.PINK) { complexTypesWithDefaultsNested.myColor }
            expect(12) { complexTypesWithDefaultsNested.myAge }
            expectArrayOf(Color.PINK, Color.PURPLE) { complexTypesWithDefaultsNested.myColors }

            val arrayOfAnnotations = nestedAllDefault.arrayOfAnnotations
            arrayOfAnnotations.forEachIndexed { index, map ->
                expect(true) { map.defaultBoolean }
                expect(0xA) { map.defaultByte }
                expect(12) { map.defaultShort }
                expect(index + 1) { map.someInt }
            }
        }

        run nestedCustom@{
            val nestedCustom = properties.nestedCustom

            val simpleTypesWithDefaultsNestedCustom = nestedCustom.simpleTypesWithDefaults
            expect(true) { simpleTypesWithDefaultsNestedCustom.defaultBoolean }
            expect(13) { simpleTypesWithDefaultsNestedCustom.defaultByte }
            expect(12) { simpleTypesWithDefaultsNestedCustom.defaultShort }
            expect(456) { simpleTypesWithDefaultsNestedCustom.someInt }

            val anotherSimpleTypesWithDefaultsNestedCustom = nestedCustom.anotherSimpleTypesWithDefaults
            expect(true) { anotherSimpleTypesWithDefaultsNestedCustom.defaultBoolean }
            expect(0xA) { anotherSimpleTypesWithDefaultsNestedCustom.defaultByte }
            expect(12) { anotherSimpleTypesWithDefaultsNestedCustom.defaultShort }
            expect(777) { anotherSimpleTypesWithDefaultsNestedCustom.someInt }

            val arrayTypesWithDefaults = nestedCustom.arrayTypesWithDefaults
            expectArrayOf(false, false) { arrayTypesWithDefaults.defaultBooleans }
            expectArrayOf<Byte>(1, 2, 3) { arrayTypesWithDefaults.defaultBytes }
            expectArrayOf<Short>(12, 34, 56) { arrayTypesWithDefaults.defaultShorts }
            expectArrayOf<Float> { arrayTypesWithDefaults.someFloats }
            expectArrayOf(3, 2, 1) { arrayTypesWithDefaults.someInts }

            val classArrayPropertiesWithDefaultsNestedCustom = nestedCustom.classArrayPropertiesWithDefaults
            expectArrayOf(Any::class, Int::class, BooleanArray::class, Unit::class, Nothing::class) {
                classArrayPropertiesWithDefaultsNestedCustom.defaultClasses
            }
            expectArrayOf(Char::class, SimpleTypesWithDefaults::class) { classArrayPropertiesWithDefaultsNestedCustom.badClasses }
            expectArrayOf(Array<Boolean>::class, SimpleTypesWithDefaults::class) {
                classArrayPropertiesWithDefaultsNestedCustom.optionalClasses
            }
            expectArrayOf<KClass<*>> { classArrayPropertiesWithDefaultsNestedCustom.classes }

            val complexTypesWithDefaultsNestedCustom = nestedCustom.complexTypesWithDefaults
            expect("Hello World") { complexTypesWithDefaultsNestedCustom.aString }
            expectArrayOf<Enum<*>> { complexTypesWithDefaultsNestedCustom.normalPets }
            expectArrayOf(Color.RED, Color.GREEN, Color.BLUE) { complexTypesWithDefaultsNestedCustom.primaryColors }
            expect(Animal.OTTER) { complexTypesWithDefaultsNestedCustom.myPet }
            expect(Color.PINK) { complexTypesWithDefaultsNestedCustom.myColor }
            expect(50) { complexTypesWithDefaultsNestedCustom.myAge }
            expectArrayOf(Color.RED) { complexTypesWithDefaultsNestedCustom.myColors }

            val arrayOfAnnotations = nestedCustom.arrayOfAnnotations
            arrayOfAnnotations.forEachIndexed { index, map ->
                expect(true) { map.defaultBoolean }
                expect(0xA) { map.defaultByte }
                expect(12) { map.defaultShort }
                expect(index + 1) { map.someInt }
            }
        }
    }

    @HasNestedStringArrays(
        hasStringArray = HasStringArray(["Next", "Faze"]),
        hasStringArrays = [
            HasStringArray(["foo@example.com", "hello", "Normal"]),
            HasStringArray(["bar@example.com", "world", "Normal"])
        ]
    )
    fun testNestedStringArray(ref: ReferenceDefinition) {
        val properties = ref.getProperties<HasNestedStringArraysProperties>()
        expectArrayOf("Next", "Faze") { properties.hasStringArray.value }
        expectArrayOf("foo@example.com", "hello", "Normal") { properties.hasStringArrays[0].value }
        expectArrayOf("bar@example.com", "world", "Normal") { properties.hasStringArrays[1].value }
    }
}
