package com.nextfaze.devfun.compiler

import com.nextfaze.devfun.compiler.handlers.AnnotationHandler
import com.nextfaze.devfun.compiler.handlers.DeveloperAnnotationHandler
import com.nextfaze.devfun.compiler.handlers.DeveloperCategoryHandler
import com.nextfaze.devfun.compiler.handlers.DeveloperFunctionHandler
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

    @Provides @IntoSet @Singleton fun developerFunction(handler: DeveloperFunctionHandler): AnnotationHandler = handler
    @Provides @IntoSet @Singleton fun developerCategory(handler: DeveloperCategoryHandler): AnnotationHandler = handler
    @Provides @IntoSet @Singleton fun developerAnnotation(handler: DeveloperAnnotationHandler): AnnotationHandler = handler
}

internal interface Injector {
    fun inject(devFunProcessor: DevFunProcessor)
}
