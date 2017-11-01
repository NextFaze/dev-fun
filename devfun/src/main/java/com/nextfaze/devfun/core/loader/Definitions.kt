package com.nextfaze.devfun.core.loader

import com.nextfaze.devfun.annotations.DeveloperCategory
import com.nextfaze.devfun.annotations.DeveloperFunction
import com.nextfaze.devfun.generated.DevFunGenerated
import java.util.ServiceLoader

@DeveloperCategory("DevFun")
internal class DefinitionsLoader {
    var definitions: List<DevFunGenerated> = listOf()
        private set
        get() {
            if (field.isNotEmpty()) return field

            field = ServiceLoader.load(DevFunGenerated::class.java).run {
                reload()
                toList()
            }

            return field
        }

    @DeveloperFunction
    fun reloadItemDefinitions() {
        definitions = listOf()
    }
}
