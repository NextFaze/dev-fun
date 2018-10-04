package com.nextfaze.devfun.demo.devfun

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.nextfaze.devfun.category.CategoryDefinition
import com.nextfaze.devfun.category.DeveloperCategory
import com.nextfaze.devfun.function.DeveloperFunction
import com.nextfaze.devfun.function.FunctionDefinition
import com.nextfaze.devfun.function.FunctionItem
import com.nextfaze.devfun.function.FunctionTransformer
import com.nextfaze.devfun.function.SimpleFunctionItem
import com.nextfaze.devfun.inject.Constructable
import com.nextfaze.devfun.invoke.view.From
import com.nextfaze.devfun.invoke.view.ValueSource

@DeveloperCategory("Debugging")
object ActivityDebugging {
    /** TODO Consider checking for [RequiresApi] instead of using [DeveloperFunction.requiresApi]? */
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    @DeveloperFunction(
        "Start ACTION_CREATE_DOCUMENT intent",
        requiresApi = Build.VERSION_CODES.KITKAT,
        category = DeveloperCategory(group = "Intents")
    )
    fun startCreateDocumentIntent(activity: Activity) {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "text/plain"
            putExtra(Intent.EXTRA_TITLE, "foobar.txt")
        }
        activity.startActivityForResult(intent, 1234)
    }

    @DeveloperFunction("Start ACTION_SEND intent", category = DeveloperCategory(group = "Intents"))
    fun startSendIntent(activity: Activity) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_STREAM, "content://com/blah/qwerty")
        }
        activity.startActivity(Intent.createChooser(intent, "Save something to"))
    }

    @DeveloperFunction(transformer = OrientationTransformer::class, category = DeveloperCategory("Screen Orientation"))
    fun setOrientation(activity: Activity, orientation: Int) {
        activity.requestedOrientation = orientation
    }

    @DeveloperFunction(category = DeveloperCategory("Screen Orientation", group = "Enum List UI"))
    fun chooseOrientation2(activity: Activity, @From(CurrentOrientation::class) orientation: ScreenOrientations) {
        activity.requestedOrientation = orientation.value
    }
}

enum class ScreenOrientations(val value: Int) {
    SCREEN_ORIENTATION_UNSPECIFIED(-1),
    SCREEN_ORIENTATION_LANDSCAPE(0),
    SCREEN_ORIENTATION_PORTRAIT(1),
    SCREEN_ORIENTATION_USER(2),
    SCREEN_ORIENTATION_BEHIND(3),
    SCREEN_ORIENTATION_SENSOR(4),
    SCREEN_ORIENTATION_NO_SENSOR(5),
    SCREEN_ORIENTATION_SENSOR_LANDSCAPE(6),
    SCREEN_ORIENTATION_SENSOR_PORTRAIT(7),
    SCREEN_ORIENTATION_REVERSE_LANDSCAPE(8),
    SCREEN_ORIENTATION_REVERSE_PORTRAIT(9),
    SCREEN_ORIENTATION_FULL_SENSOR(10),
    SCREEN_ORIENTATION_USER_LANDSCAPE(11),
    SCREEN_ORIENTATION_USER_PORTRAIT(12),
    SCREEN_ORIENTATION_FULL_USER(13),
    SCREEN_ORIENTATION_LOCKED(14);

    companion object {
        private val values = values().associateBy { it.value }
        fun fromValue(value: Int): ScreenOrientations {
            return values[value] ?: SCREEN_ORIENTATION_UNSPECIFIED
        }
    }
}

/**
 * Provides the current orientation for use with @[From].
 *
 * @param activity This will be injected upon construction.
 */
@Constructable
class CurrentOrientation(private val activity: Activity) : ValueSource<ScreenOrientations> {
    override val value get() = ScreenOrientations.fromValue(activity.requestedOrientation)
}

/**
 * Function -> Item transformer for [ActivityDebugging.setOrientation].
 *
 * This class is not handled by Dagger (though DevFun will check Dagger for it).
 * Thus it will be constructed upon request (i.e. `OrientationTransformer::class.constructors.single().call(...)`).
 *
 * Arguments will then also injected/constructed.
 *
 * @param currentOrientation This will be injected upon construction.
 */
@Constructable
class OrientationTransformer(currentOrientation: CurrentOrientation) : FunctionTransformer {
    private val currentOrientation = currentOrientation.value.value

    /**
     * Maps the the available screen orientations to function items that will invoke [ActivityDebugging.setOrientation].
     *
     * Since the first argument is the current activity, the [SimpleFunctionItem.args] value uses [Unit] for the first value.
     *
     * i.e.
     * ```
     * listOf(Unit, orientation.value)
     * ```
     *
     * NB: If the orientation was first then `listOf(orientation.value)` would suffice.
     */
    override fun apply(functionDefinition: FunctionDefinition, categoryDefinition: CategoryDefinition): Collection<FunctionItem>? =
        ScreenOrientations.values().map { orientation ->
            object : SimpleFunctionItem(functionDefinition, categoryDefinition) {
                override val name by lazy {
                    val shortName = orientation.toString().substringAfter("SCREEN_ORIENTATION_")
                    val selected = if (currentOrientation == orientation.value) "****" else ""
                    "$selected $shortName (${orientation.value}) $selected".trim()
                }
                override val args = listOf(Unit /* Unit means inject */, orientation.value)
                override val group = "Set Orientation"
            }
        }
}
