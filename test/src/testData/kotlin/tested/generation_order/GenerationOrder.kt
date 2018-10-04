@file:Suppress("UNUSED_PARAMETER", "unused", "PackageName")

package tested.generation_order

import com.nextfaze.devfun.function.DeveloperFunction

annotation class GenerationOrder

class LotsOfFunctions1 {
    @DeveloperFunction fun someFun16() = Unit
    @DeveloperFunction fun someFun18() = Unit
    @DeveloperFunction private fun someFun0() = Unit
    @DeveloperFunction fun someFun17() = Unit
    @DeveloperFunction internal fun someFun6() = Unit
    @DeveloperFunction fun someFun1() = Unit
    @DeveloperFunction fun someFun7() = Unit
    @DeveloperFunction fun someFun8() = Unit
    @DeveloperFunction internal fun someFun11() = Unit
    @DeveloperFunction fun someFun12() = Unit
    @DeveloperFunction internal fun someFun13() = Unit
    @DeveloperFunction fun someFun9() = Unit
    @DeveloperFunction fun someFun14() = Unit
    @DeveloperFunction fun someFun15() = Unit
    @DeveloperFunction private fun someFun10() = Unit
    @DeveloperFunction internal fun someFun19() = Unit
    @DeveloperFunction fun someFun26() = Unit
    @DeveloperFunction fun someFun27() = Unit
    @DeveloperFunction fun someFun28() = Unit
    @DeveloperFunction internal fun someFun21() = Unit
    @DeveloperFunction fun someFun29() = Unit
    @DeveloperFunction private fun someFun22() = Unit
    @DeveloperFunction fun someFun2() = Unit
    @DeveloperFunction fun someFun23() = Unit
    @DeveloperFunction fun someFun24() = Unit
    @DeveloperFunction private fun someFun20() = Unit
    @DeveloperFunction fun someFun4() = Unit
    @DeveloperFunction fun someFun3() = Unit
    @DeveloperFunction fun someFun25() = Unit
    @DeveloperFunction fun someFun5() = Unit
}

object LotsOfFunctions3 {
    @DeveloperFunction fun someFun14() = Unit
    @DeveloperFunction fun someFun12() = Unit
    @DeveloperFunction fun someFun2() = Unit
    @DeveloperFunction fun someFun0() = Unit
    @DeveloperFunction private fun someFun21() = Unit
    @DeveloperFunction fun someFun11() = Unit
    @DeveloperFunction fun someFun1() = Unit
    @DeveloperFunction fun someFun15() = Unit
    @DeveloperFunction internal fun someFun5() = Unit
    @DeveloperFunction fun someFun22() = Unit
    @DeveloperFunction fun someFun7() = Unit
    @DeveloperFunction fun someFun8() = Unit
    @DeveloperFunction fun someFun29() = Unit
    @DeveloperFunction fun someFun13() = Unit
    @DeveloperFunction fun someFun25() = Unit
    @DeveloperFunction internal fun someFun27() = Unit
    @DeveloperFunction private fun someFun3() = Unit
    @DeveloperFunction fun someFun4() = Unit
    @DeveloperFunction private fun someFun26() = Unit
    @DeveloperFunction fun someFun18() = Unit
    @DeveloperFunction fun someFun17() = Unit
    @DeveloperFunction private fun someFun23() = Unit
    @DeveloperFunction fun someFun20() = Unit
    @DeveloperFunction fun someFun16() = Unit
    @DeveloperFunction fun someFun6() = Unit
    @DeveloperFunction fun someFun19() = Unit
    @DeveloperFunction internal fun someFun28() = Unit
    @DeveloperFunction fun someFun10() = Unit
    @DeveloperFunction fun someFun24() = Unit
    @DeveloperFunction fun someFun9() = Unit
}

private object LotsOfFunctions2 {
    @DeveloperFunction fun someFun7() = Unit
    @DeveloperFunction fun someFun8() = Unit
    @DeveloperFunction internal fun someFun14() = Unit
    @DeveloperFunction fun someFun0() = Unit
    @DeveloperFunction fun someFun6() = Unit
    @DeveloperFunction private fun someFun10() = Unit
    @DeveloperFunction fun someFun9() = Unit
    @DeveloperFunction fun someFun12() = Unit
    @DeveloperFunction internal fun someFun1() = Unit
    @DeveloperFunction fun someFun2() = Unit
    @DeveloperFunction fun someFun11() = Unit
    @DeveloperFunction fun someFun13() = Unit
    @DeveloperFunction private fun someFun4() = Unit
    @DeveloperFunction fun someFun15() = Unit
    @DeveloperFunction internal fun someFun5() = Unit
    @DeveloperFunction fun someFun29() = Unit
    @DeveloperFunction private fun someFun3() = Unit
    @DeveloperFunction fun someFun25() = Unit
    @DeveloperFunction fun someFun27() = Unit
    @DeveloperFunction internal fun someFun16() = Unit
    @DeveloperFunction fun someFun18() = Unit
    @DeveloperFunction internal fun someFun20() = Unit
    @DeveloperFunction fun someFun22() = Unit
    @DeveloperFunction fun someFun21() = Unit
    @DeveloperFunction private fun someFun19() = Unit
    @DeveloperFunction fun someFun23() = Unit
    @DeveloperFunction fun someFun28() = Unit
    @DeveloperFunction fun someFun24() = Unit
}

class LotsOfFunctions4 {
    @DeveloperFunction internal fun someFun(int: Int) = Unit
    @DeveloperFunction private fun someFun(bool: Boolean, str: String, int: Int) = Unit
    @DeveloperFunction fun someFun(str: String, int: Int) = Unit
    @DeveloperFunction fun someFun(int: Long) = Unit
    @DeveloperFunction private fun someFun(charSequence: CharSequence) = Unit
    @DeveloperFunction internal fun someFun() = Unit
    @DeveloperFunction fun someFun(list: List<Int>) = Unit
    @DeveloperFunction internal fun someFun(list: Iterable<Long>) = Unit
    @DeveloperFunction private fun someFun(list: ArrayList<Long>) = Unit
    @DeveloperFunction fun someFun(list: Map<Long, Boolean>) = Unit
    @DeveloperFunction fun someFun(int: Boolean) = Unit
    @DeveloperFunction fun someFun(list: HashMap<Float, Int>) = Unit
}
