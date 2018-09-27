@file:Suppress("unused")

package com.nextfaze.devfun.annotations

import com.nextfaze.devfun.core.FunctionTransformer
import com.nextfaze.devfun.core.SingleFunctionTransformer
import com.nextfaze.devfun.overlay.Dock
import kotlin.reflect.KClass

/**
 * Properties interface for @[DeveloperCategory].
 *
 * TODO: This interface should be generated by DevFun at compile time, but as the annotations are in a separate module to the compiler
 * that itself depends on the annotations module, it is non-trivial to run the DevFun processor upon it (module dependencies become cyclic).
 */
interface DeveloperCategoryProperties {
    val value: String get() = ""
    val group: String get() = ""
    val order: Int get() = 0
}

/**
 * Properties interface for @[DeveloperFunction].
 *
 * TODO: This interface should be generated by DevFun at compile time, but as the annotations are in a separate module to the compiler
 * that itself depends on the annotations module, it is non-trivial to run the DevFun processor upon it (module dependencies become cyclic).
 */
interface DeveloperFunctionProperties {
    val value: String get() = ""
    val category: DeveloperCategoryProperties get() = object : DeveloperCategoryProperties {}
    val requiresApi: Int get() = 0
    val transformer: KClass<out FunctionTransformer> get() = SingleFunctionTransformer::class
}

/**
 * Properties interface for @[DeveloperReference].
 *
 * TODO: This interface should be generated by DevFun at compile time, but as the annotations are in a separate module to the compiler
 * that itself depends on the annotations module, it is non-trivial to run the DevFun processor upon it (module dependencies become cyclic).
 */
interface DeveloperReferenceProperties

/** Properties interface for @[Args].
 *
 * TODO: This interface should be generated by DevFun at compile time, but as the annotations are in a separate module to the compiler
 * that itself depends on the annotations module, it is non-trivial to run the DevFun processor upon it (module dependencies become cyclic).
 */
interface ArgsProperties {
    val value: Array<String>
}

/**
 * Properties interface for @[DeveloperArguments].
 *
 * TODO: This interface should be generated by DevFun at compile time, but as the annotations are in a separate module to the compiler
 * that itself depends on the annotations module, it is non-trivial to run the DevFun processor upon it (module dependencies become cyclic).
 */
interface DeveloperArgumentsProperties {
    val name: String get() = "%0"
    val args: Array<ArgsProperties>
    val group: String get() = "%FUN_SN%"
    val category: DeveloperCategoryProperties get() = object : DeveloperCategoryProperties {}
    val requiresApi: Int get() = 0
    val transformer: KClass<out FunctionTransformer> get() = ArgumentsTransformer::class
}

/**
 * Properties interface for @[Dagger2Component].
 *
 * TODO: This interface should be generated by DevFun at compile time, but as the annotations are in a separate module to the compiler
 * that itself depends on the annotations module, it is non-trivial to run the DevFun processor upon it (module dependencies become cyclic).
 */
interface Dagger2ComponentProperties {
    val scope: Dagger2Scope get() = Dagger2Scope.UNDEFINED
    val priority: Int get() = 0
    val isActivityRequired: Boolean get() = false
    val isFragmentActivityRequired: Boolean get() = false
}

/**
 * Properties interface for @[DeveloperLogger].
 *
 * TODO: This interface should be generated by DevFun at compile time, but as the annotations are in a separate module to the compiler
 * that itself depends on the annotations module, it is non-trivial to run the DevFun processor upon it (module dependencies become cyclic).
 */
interface DeveloperLoggerProperties {
    val enabled: Boolean get() = true
    val refreshRate: Long get() = 1000L
    val snapToEdge: Boolean get() = false
    val dock: Dock get() = Dock.TOP_LEFT
    val delta: Float get() = 0.0f
    val top: Float get() = 0.0f
    val left: Float get() = 0.0f
}

/**
 * Properties interface for @[DeveloperProperty].
 *
 * TODO: This interface should be generated by DevFun at compile time, but as the annotations are in a separate module to the compiler
 * that itself depends on the annotations module, it is non-trivial to run the DevFun processor upon it (module dependencies become cyclic).
 */
interface DeveloperPropertyProperties {
    val value: String get() = ""
    val category: DeveloperCategoryProperties
        get() = object : DeveloperCategoryProperties {
            override val group: String = "Properties"
        }

    val requiresApi: Int get() = 0
    val transformer: KClass<out FunctionTransformer> get() = PropertyTransformer::class
}