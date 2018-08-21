package com.nextfaze.devfun.compiler

import com.nextfaze.devfun.compiler.processing.AnnotationProcessor
import com.nextfaze.devfun.compiler.processing.DeveloperCategoryHandler
import com.nextfaze.devfun.compiler.processing.DeveloperFunctionHandler
import com.nextfaze.devfun.compiler.processing.DeveloperReferenceHandler
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Filer
import javax.annotation.processing.ProcessingEnvironment
import javax.inject.Singleton
import javax.lang.model.util.Elements
import javax.lang.model.util.Types


@Singleton
@Component(modules = [MainModule::class])
internal interface ApplicationComponent : Injector

@Module
internal class MainModule(private val env: ProcessingEnvironment) {
    @Provides fun processingEnvironment() = env
    @Provides fun filer(): Filer = env.filer
    @Provides fun elements(): Elements = env.elementUtils
    @Provides fun types(): Types = env.typeUtils

    @Provides @IntoSet @Singleton fun developerFunction(handler: DeveloperFunctionHandler): AnnotationProcessor = handler
    @Provides @IntoSet @Singleton fun developerCategory(handler: DeveloperCategoryHandler): AnnotationProcessor = handler
    @Provides @IntoSet @Singleton fun developerReference(handler: DeveloperReferenceHandler): AnnotationProcessor = handler
}

/**
 * Dagger injector for [DaggerProcessor].
 *
 * _Visible for testing purposes only! Use at your own risk._
 */
interface Injector {
    fun inject(devFunProcessor: DevFunProcessor)
    fun inject(devAnnotationProcessor: DevAnnotationProcessor)
}

/**
 * Base [AbstractProcessor] class with Dagger support.
 *
 * _Visible for testing purposes only! Use at your own risk._
 */
abstract class DaggerProcessor : AbstractProcessor() {
    companion object {
        /*
        We can't use lazy/lateinit as testing is in-process and thus would only work on first init.
        TODO? update tests to use per-test classloader?
         */
        private var env: ProcessingEnvironment? = null
        private var applicationComponent: ApplicationComponent? = null

        private fun createApplicationComponent(env: ProcessingEnvironment) =
            DaggerApplicationComponent.builder()
                .mainModule(MainModule(env))
                .build()

        private fun getApplicationComponent(env: ProcessingEnvironment): ApplicationComponent {
            if (this.env != env) {
                this.env = env
                applicationComponent = createApplicationComponent(env)
            }

            return applicationComponent!!
        }
    }

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        inject(getApplicationComponent(processingEnv) as Injector)
    }

    protected abstract fun inject(injector: Injector)
}
