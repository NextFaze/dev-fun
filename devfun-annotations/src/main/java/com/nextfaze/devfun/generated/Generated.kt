package com.nextfaze.devfun.generated

import com.nextfaze.devfun.core.CategoryDefinition
import com.nextfaze.devfun.core.FunctionDefinition
import java.util.ServiceLoader

/**
 * Generated classes will implement this, which will be loaded using Java's [ServiceLoader].
 *
 * @see CategoryDefinition
 * @see FunctionDefinition
 */
interface DevFunGenerated {
    val categoryDefinitions: List<CategoryDefinition>
    val functionDefinitions: List<FunctionDefinition>
}
