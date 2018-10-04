package com.nextfaze.devfun.generated

import com.nextfaze.devfun.category.CategoryDefinition
import com.nextfaze.devfun.category.DeveloperCategory
import com.nextfaze.devfun.function.DeveloperFunction
import com.nextfaze.devfun.function.FunctionDefinition
import com.nextfaze.devfun.reference.DeveloperReference
import com.nextfaze.devfun.reference.ReferenceDefinition
import java.util.ServiceLoader

/**
 * Generated classes will implement this, which will be loaded using Java's [ServiceLoader].
 *
 * @see DeveloperCategory
 * @see CategoryDefinition
 * @see DeveloperFunction
 * @see FunctionDefinition
 * @see DeveloperReference
 * @see ReferenceDefinition
 */
interface DevFunGenerated {
    /**
     * List of category definitions.
     *
     * @see DeveloperCategory
     */
    val categoryDefinitions: List<CategoryDefinition> get() = emptyList()

    /**
     * List of function definitions.
     *
     * @see DeveloperFunction
     */
    val functionDefinitions: List<FunctionDefinition> get() = emptyList()

    /**
     * List of developer references.
     *
     * @see DeveloperReference
     */
    val developerReferences: List<ReferenceDefinition> get() = emptyList()
}
