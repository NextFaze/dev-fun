@file:Suppress("unused")

package tested.requires_api

import android.os.Build
import com.nextfaze.devfun.annotations.DeveloperFunction
import com.nextfaze.devfun.test.ExpectedItemCount
import com.nextfaze.devfun.test.SingleItemRequiresApiTest

internal annotation class FunctionsWithRequiresApi

class fra_FunctionsWithRequiresApi {
    @DeveloperFunction
    fun noApiRequired() = listOf(
            ExpectedItemCount(1),
            SingleItemRequiresApiTest(null)
    )

    @DeveloperFunction(requiresApi = Build.VERSION_CODES.GINGERBREAD)
    fun requiresGingerbread() = listOf(
            ExpectedItemCount(1),
            SingleItemRequiresApiTest(Build.VERSION_CODES.GINGERBREAD)
    )

    @DeveloperFunction(requiresApi = Build.VERSION_CODES.GINGERBREAD_MR1)
    fun requiresGingerbreadMr1() = listOf(
            ExpectedItemCount(1),
            SingleItemRequiresApiTest(Build.VERSION_CODES.GINGERBREAD_MR1)
    )

    @DeveloperFunction(requiresApi = Build.VERSION_CODES.KITKAT)
    fun requiresKitKat() = listOf(
            ExpectedItemCount(0),
            SingleItemRequiresApiTest(Build.VERSION_CODES.KITKAT)
    )

    @DeveloperFunction(requiresApi = 99)
    fun requiresApi99() = listOf(
            ExpectedItemCount(0),
            SingleItemRequiresApiTest(99)
    )

    @DeveloperFunction(requiresApi = 0)
    fun setsDefaultApiRequired() = listOf(
            ExpectedItemCount(1),
            SingleItemRequiresApiTest(0)
    )

    @DeveloperFunction(requiresApi = -4)
    fun hasNegativeApi() = listOf(
            ExpectedItemCount(1),
            SingleItemRequiresApiTest(-4)
    )
}
