@file:Suppress("unused")

package tested.generation_order

import com.nextfaze.devfun.annotations.DeveloperFunction

annotation class GenerationOrder

class LotsOfFunctions1 {
    @DeveloperFunction fun someFun16() = Unit
    @DeveloperFunction fun someFun18() = Unit
    private @DeveloperFunction fun someFun0() = Unit
    @DeveloperFunction fun someFun17() = Unit
    internal @DeveloperFunction fun someFun6() = Unit
    @DeveloperFunction fun someFun1() = Unit
    @DeveloperFunction fun someFun7() = Unit
    @DeveloperFunction fun someFun8() = Unit
    internal @DeveloperFunction fun someFun11() = Unit
    @DeveloperFunction fun someFun12() = Unit
    internal @DeveloperFunction fun someFun13() = Unit
    @DeveloperFunction fun someFun9() = Unit
    @DeveloperFunction fun someFun14() = Unit
    @DeveloperFunction fun someFun15() = Unit
    private @DeveloperFunction fun someFun10() = Unit
    internal @DeveloperFunction fun someFun19() = Unit
    @DeveloperFunction fun someFun26() = Unit
    @DeveloperFunction fun someFun27() = Unit
    @DeveloperFunction fun someFun28() = Unit
    internal @DeveloperFunction fun someFun21() = Unit
    @DeveloperFunction fun someFun29() = Unit
    private @DeveloperFunction fun someFun22() = Unit
    @DeveloperFunction fun someFun2() = Unit
    @DeveloperFunction fun someFun23() = Unit
    @DeveloperFunction fun someFun24() = Unit
    private @DeveloperFunction fun someFun20() = Unit
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
    private @DeveloperFunction fun someFun21() = Unit
    @DeveloperFunction fun someFun11() = Unit
    @DeveloperFunction fun someFun1() = Unit
    @DeveloperFunction fun someFun15() = Unit
    internal @DeveloperFunction fun someFun5() = Unit
    @DeveloperFunction fun someFun22() = Unit
    @DeveloperFunction fun someFun7() = Unit
    @DeveloperFunction fun someFun8() = Unit
    @DeveloperFunction fun someFun29() = Unit
    @DeveloperFunction fun someFun13() = Unit
    @DeveloperFunction fun someFun25() = Unit
    internal @DeveloperFunction fun someFun27() = Unit
    private @DeveloperFunction fun someFun3() = Unit
    @DeveloperFunction fun someFun4() = Unit
    private @DeveloperFunction fun someFun26() = Unit
    @DeveloperFunction fun someFun18() = Unit
    @DeveloperFunction fun someFun17() = Unit
    private @DeveloperFunction fun someFun23() = Unit
    @DeveloperFunction fun someFun20() = Unit
    @DeveloperFunction fun someFun16() = Unit
    @DeveloperFunction fun someFun6() = Unit
    @DeveloperFunction fun someFun19() = Unit
    internal @DeveloperFunction fun someFun28() = Unit
    @DeveloperFunction fun someFun10() = Unit
    @DeveloperFunction fun someFun24() = Unit
    @DeveloperFunction fun someFun9() = Unit
}

private object LotsOfFunctions2 {
    @DeveloperFunction fun someFun7() = Unit
    @DeveloperFunction fun someFun8() = Unit
    internal @DeveloperFunction fun someFun14() = Unit
    @DeveloperFunction fun someFun0() = Unit
    @DeveloperFunction fun someFun6() = Unit
    private @DeveloperFunction fun someFun10() = Unit
    @DeveloperFunction fun someFun9() = Unit
    @DeveloperFunction fun someFun12() = Unit
    internal @DeveloperFunction fun someFun1() = Unit
    @DeveloperFunction fun someFun2() = Unit
    @DeveloperFunction fun someFun11() = Unit
    @DeveloperFunction fun someFun13() = Unit
    private @DeveloperFunction fun someFun4() = Unit
    @DeveloperFunction fun someFun15() = Unit
    internal @DeveloperFunction fun someFun5() = Unit
    @DeveloperFunction fun someFun29() = Unit
    private @DeveloperFunction fun someFun3() = Unit
    @DeveloperFunction fun someFun25() = Unit
    @DeveloperFunction fun someFun27() = Unit
    internal @DeveloperFunction fun someFun16() = Unit
    @DeveloperFunction fun someFun18() = Unit
    internal @DeveloperFunction fun someFun20() = Unit
    @DeveloperFunction fun someFun22() = Unit
    @DeveloperFunction fun someFun21() = Unit
    private @DeveloperFunction fun someFun19() = Unit
    @DeveloperFunction fun someFun23() = Unit
    @DeveloperFunction fun someFun28() = Unit
    @DeveloperFunction fun someFun24() = Unit
}

class LotsOfFunctions4 {
    internal @DeveloperFunction fun someFun(int: Int) = Unit
    private @DeveloperFunction fun someFun(bool: Boolean, str: String, int: Int) = Unit
    @DeveloperFunction fun someFun(str: String, int: Int) = Unit
    @DeveloperFunction fun someFun(int: Long) = Unit
    private @DeveloperFunction fun someFun(charSequence: CharSequence) = Unit
    internal @DeveloperFunction fun someFun() = Unit
    @DeveloperFunction fun someFun(list: List<Int>) = Unit
    internal @DeveloperFunction fun someFun(list: Iterable<Long>) = Unit
    private @DeveloperFunction fun someFun(list: ArrayList<Long>) = Unit
    @DeveloperFunction fun someFun(list: Map<Long, Boolean>) = Unit
    @DeveloperFunction fun someFun(int: Boolean) = Unit
    @DeveloperFunction fun someFun(list: HashMap<Float, Int>) = Unit
}
