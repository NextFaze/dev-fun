package com.nextfaze.devfun.compiler

import com.nextfaze.devfun.compiler.processing.AnnotationProcessor
import com.nextfaze.devfun.compiler.processing.DeveloperCategoryHandler
import com.nextfaze.devfun.compiler.processing.DeveloperFunctionHandler
import com.nextfaze.devfun.compiler.processing.DeveloperReferenceHandler
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import javax.annotation.processing.Filer
import javax.annotation.processing.ProcessingEnvironment
import javax.inject.Singleton
import javax.lang.model.util.Elements


@Singleton
@Component(modules = [MainModule::class])
internal interface ApplicationComponent : Injector

@Module
internal class MainModule(
    private val devFunProcessor: DevFunProcessor,
    private val env: ProcessingEnvironment
) {
    @Provides fun devFunProcessor() = devFunProcessor

    @Provides fun processingEnvironment() = env
    @Provides fun filer(): Filer = env.filer
    @Provides fun elements(): Elements = env.elementUtils

    @Provides @IntoSet @Singleton fun developerFunction(handler: DeveloperFunctionHandler): AnnotationProcessor = handler
    @Provides @IntoSet @Singleton fun developerCategory(handler: DeveloperCategoryHandler): AnnotationProcessor = handler
    @Provides @IntoSet @Singleton fun developerReference(handler: DeveloperReferenceHandler): AnnotationProcessor = handler
}

internal interface Injector {
    fun inject(devFunProcessor: DevFunProcessor)
}
