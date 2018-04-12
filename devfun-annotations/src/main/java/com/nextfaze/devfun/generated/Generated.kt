package com.nextfaze.devfun.generated

import com.nextfaze.devfun.annotations.DeveloperAnnotation
import com.nextfaze.devfun.annotations.DeveloperCategory
import com.nextfaze.devfun.annotations.DeveloperFunction
import com.nextfaze.devfun.core.CategoryDefinition
import com.nextfaze.devfun.core.DeveloperReference
import com.nextfaze.devfun.core.FunctionDefinition
import java.util.ServiceLoader

/**
 * Generated classes will implement this, which will be loaded using Java's [ServiceLoader].
 *
 * @see DeveloperCategory
 * @see CategoryDefinition
 * @see DeveloperFunction
 * @see FunctionDefinition
 * @see DeveloperAnnotation
 * @see DeveloperReference
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
     * @see DeveloperAnnotation
     */
    val developerReferences: List<DeveloperReference> get() = emptyList()
}
