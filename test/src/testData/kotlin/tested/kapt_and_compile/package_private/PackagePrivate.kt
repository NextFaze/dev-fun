@file:Suppress("ClassName", "PackageName", "unused", "UNUSED_PARAMETER")

package tested.kapt_and_compile.package_private

import com.nextfaze.devfun.annotations.DeveloperFunction

annotation class PackagePrivate

class pp_SomeClass {
    @DeveloperFunction
    internal fun functionUsingPackagePrivateType(packagePrivateType: PackagePrivateType) {
    }
}
