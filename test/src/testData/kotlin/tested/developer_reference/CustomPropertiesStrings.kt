@file:Suppress("UNUSED_PARAMETER", "unused", "PackageName", "ClassName")

package tested.developer_reference

import com.nextfaze.devfun.annotations.DeveloperAnnotation
import com.nextfaze.devfun.core.MethodReference
import kotlin.annotation.AnnotationRetention.SOURCE
import kotlin.annotation.AnnotationTarget.FUNCTION
import kotlin.test.expect

annotation class CustomPropertiesStrings

@Target(FUNCTION)
@Retention(SOURCE)
@DeveloperAnnotation(developerReference = true)
annotation class HasStringProperties(
    val aString: String,
    val anotherString: String,
    val yas: String
)

private const val DOLLAR = "$"

@Target(FUNCTION)
@Retention(SOURCE)
@DeveloperAnnotation(developerReference = true)
annotation class HasStringPropertiesWithDefaults(
    val defaultString: String = "This is a default string.",
    val anotherDefaultString: String =
        """Another default string, but with a ${DOLLAR}var
        And another line.""",
    val andAnotherDefaultString: String = "Overwrite me!",
    val someString: String
)

class cps_SomeClass {
    @HasStringProperties(
        aString = "This is a String",
        anotherString = "A string with a $" + "annoyingUser",
        yas = """> First Line! !@#$%^&*()_+ <

            String with multiple lines

            Another Line

            And ${'$'}anotherAnnoyingUser

            Last Line!"""
    )
    fun testStringProperties(ref: MethodReference) {
        val dollar = "$"
        val properties = ref.properties!!
        expect("This is a String") { properties["aString"] }
        expect("A string with a \$annoyingUser") { properties["anotherString"] }
        expect(
            """> First Line! !@#$%^&*()_+ <

            String with multiple lines

            Another Line

            And ${dollar}anotherAnnoyingUser

            Last Line!"""
        ) { properties["yas"] }
    }

    @HasStringPropertiesWithDefaults(
        andAnotherDefaultString = "My custom value!",
        someString = "Blah!"
    )
    fun testStringPropertiesWithDefaults(ref: MethodReference) {
        val properties = ref.properties!!
        expect("This is a default string.") { properties["defaultString"] }
        expect("Another default string, but with a ${DOLLAR}var\n        And another line.") { properties["anotherDefaultString"] }
        expect("My custom value!") { properties["andAnotherDefaultString"] }
        expect("Blah!") { properties["someString"] }
    }
}
