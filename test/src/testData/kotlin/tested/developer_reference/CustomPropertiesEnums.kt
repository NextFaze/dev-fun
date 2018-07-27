@file:Suppress("UNUSED_PARAMETER", "unused", "PackageName", "ClassName", "UNCHECKED_CAST", "CAST_NEVER_SUCCEEDS")

package tested.developer_reference

import com.nextfaze.devfun.annotations.DeveloperAnnotation
import com.nextfaze.devfun.core.MethodReference
import com.nextfaze.devfun.test.expectArrayOf
import kotlin.annotation.AnnotationRetention.SOURCE
import kotlin.annotation.AnnotationTarget.FUNCTION
import kotlin.test.expect

annotation class CustomPropertiesEnums

@Target(FUNCTION)
@Retention(SOURCE)
@DeveloperAnnotation(developerReference = true)
private annotation class HasEnumProperties(
    val myNumbers: MyNumbers,
    val myNumbersDefault: MyNumbers = MyNumbers.TWO,
    val myNumbersDefaultChangeMe: MyNumbers = MyNumbers.ONE,
    val myPrivateTypes: MyPrivateTypes,
    val myPrivateTypesDefault: MyPrivateTypes = MyPrivateTypes.NEXT,
    val myPrivateTypesDefaultChangeMe: MyPrivateTypes = MyPrivateTypes.FAZE
)

@Target(FUNCTION)
@Retention(SOURCE)
@DeveloperAnnotation(developerReference = true)
private annotation class HasEnumArrayProperties(
    val myNumbers: Array<MyNumbers>,
    val myNumbersDefault: Array<MyNumbers> = [MyNumbers.ONE, MyNumbers.TWO],
    val myNumbersDefaultChangeMe: Array<MyNumbers> = [MyNumbers.TWO, MyNumbers.TWO],
    val myPrivateTypes: Array<MyPrivateTypes>,
    val myPrivateTypesDefault: Array<MyPrivateTypes> = [MyPrivateTypes.NEXT, MyPrivateTypes.FAZE],
    val myPrivateTypesDefaultChangeMe: Array<MyPrivateTypes> = [MyPrivateTypes.HELLO, MyPrivateTypes.WORLD]
)

enum class MyNumbers { ONE, TWO, THREE }

private enum class MyPrivateTypes { HELLO, WORLD, NEXT, FAZE }

class cpe_SomeClass {
    @HasEnumProperties(
        myNumbers = MyNumbers.ONE,
        myNumbersDefaultChangeMe = MyNumbers.THREE,
        myPrivateTypes = MyPrivateTypes.HELLO,
        myPrivateTypesDefaultChangeMe = MyPrivateTypes.WORLD
    )
    fun testEnumProperties(ref: MethodReference) {
        val properties = ref.properties!!
        expect(MyNumbers.ONE) { properties["myNumbers"] }
        expect(MyNumbers.TWO) { properties["myNumbersDefault"] }
        expect(MyNumbers.THREE) { properties["myNumbersDefaultChangeMe"] }
        expect(MyPrivateTypes.HELLO) { properties["myPrivateTypes"] }
        expect(MyPrivateTypes.NEXT) { properties["myPrivateTypesDefault"] }
        expect(MyPrivateTypes.WORLD) { properties["myPrivateTypesDefaultChangeMe"] }
    }

    @HasEnumArrayProperties(
        myNumbers = [MyNumbers.ONE, MyNumbers.ONE, MyNumbers.THREE],
        myNumbersDefaultChangeMe = [MyNumbers.THREE, MyNumbers.ONE, MyNumbers.TWO],
        myPrivateTypes = [MyPrivateTypes.HELLO, MyPrivateTypes.NEXT, MyPrivateTypes.FAZE],
        myPrivateTypesDefaultChangeMe = [MyPrivateTypes.HELLO, MyPrivateTypes.HELLO, MyPrivateTypes.HELLO]
    )
    fun testEnumArrayProperties(ref: MethodReference) {
        val properties = ref.properties!!
        expectArrayOf(MyNumbers.ONE, MyNumbers.ONE, MyNumbers.THREE) { properties["myNumbers"] }
        expectArrayOf(MyNumbers.ONE, MyNumbers.TWO) { properties["myNumbersDefault"] }
        expectArrayOf(MyNumbers.THREE, MyNumbers.ONE, MyNumbers.TWO) { properties["myNumbersDefaultChangeMe"] }
        expectArrayOf(MyPrivateTypes.HELLO, MyPrivateTypes.NEXT, MyPrivateTypes.FAZE) { properties["myPrivateTypes"] }
        expectArrayOf(MyPrivateTypes.NEXT, MyPrivateTypes.FAZE) { properties["myPrivateTypesDefault"] }
        expectArrayOf(MyPrivateTypes.HELLO, MyPrivateTypes.HELLO, MyPrivateTypes.HELLO) { properties["myPrivateTypesDefaultChangeMe"] }
    }
}
