package com.nextfaze.devfun.demo.test

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [DaggerIncludedModule::class])
class DaggerTestModule {

}

@Module
class DaggerIncludedModule {
    @Provides @Singleton
    internal fun dummyClass(packagePrivateClass: PackagePrivateClass) = DummyClass(packagePrivateClass)

    @Provides @Singleton
    internal fun packagePrivateClass() = PackagePrivateClass()
}

internal class DummyClass(@Suppress("UNUSED_PARAMETER") packagePrivateClass: PackagePrivateClass)
