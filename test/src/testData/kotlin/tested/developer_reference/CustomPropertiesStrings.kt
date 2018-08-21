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
annotation class HasStrings(
    val aString: String,
    val anotherString: String,
    val yas: String
)

private const val DOLLAR = "$"

@Target(FUNCTION)
@Retention(SOURCE)
@DeveloperAnnotation(developerReference = true)
annotation class HasStringsWithDefaults(
    val defaultString: String = "This is a default string.",
    val anotherDefaultString: String =
        """Another default string, but with a ${DOLLAR}var
        And another line.""",
    val andAnotherDefaultString: String = "Overwrite me!",
    val someString: String
)

class cps_SomeClass {
    @HasStrings(
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
        val propertyMap = ref.propertyMap!!
        expect("This is a String") { propertyMap["aString"] }
        expect("A string with a \$annoyingUser") { propertyMap["anotherString"] }
        expect(
            """> First Line! !@#$%^&*()_+ <

            String with multiple lines

            Another Line

            And ${dollar}anotherAnnoyingUser

            Last Line!"""
        ) { propertyMap["yas"] }

        val properties = ref.properties as HasStringsProperties
        expect("This is a String") { properties.aString }
        expect("A string with a \$annoyingUser") { properties.anotherString }
        expect(
            """> First Line! !@#$%^&*()_+ <

            String with multiple lines

            Another Line

            And ${dollar}anotherAnnoyingUser

            Last Line!"""
        ) { properties.yas }
    }

    @HasStringsWithDefaults(
        andAnotherDefaultString = "My custom value!",
        someString = "Blah!"
    )
    fun testStringPropertiesWithDefaults(ref: MethodReference) {
        val propertyMap = ref.propertyMap!!
        expect("This is a default string.") { propertyMap["defaultString"] }
        expect("Another default string, but with a ${DOLLAR}var\n        And another line.") { propertyMap["anotherDefaultString"] }
        expect("My custom value!") { propertyMap["andAnotherDefaultString"] }
        expect("Blah!") { propertyMap["someString"] }

        val properties = ref.properties as HasStringsWithDefaultsProperties
        expect("This is a default string.") { properties.defaultString }
        expect("Another default string, but with a ${DOLLAR}var\n        And another line.") { properties.anotherDefaultString }
        expect("My custom value!") { properties.andAnotherDefaultString }
        expect("Blah!") { properties.someString }
    }
}
