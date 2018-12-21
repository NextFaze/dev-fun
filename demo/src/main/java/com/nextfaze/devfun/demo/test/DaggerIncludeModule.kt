@file:Suppress("unused")

package com.nextfaze.devfun.demo.test

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import javax.inject.Qualifier
import javax.inject.Singleton

@Module(includes = [DaggerTestIncludedModule::class, DaggerTestScopingModule::class])
class DaggerTestModule

//////////////////////////

@Module
class DaggerTestIncludedModule {
    @Provides @Singleton
    internal fun dummyClass(packagePrivateClass: PackagePrivateClass) = DummyClass(packagePrivateClass)

    @Provides @Singleton
    internal fun packagePrivateClass() = PackagePrivateClass()
}

internal class DummyClass(@Suppress("UNUSED_PARAMETER") packagePrivateClass: PackagePrivateClass)

//////////////////////////

@Module
class DaggerTestScopingModule {
//    @Provides @QualifiedString1 fun qualifiedString1() = "123.4"
//    @Provides @QualifiedString2 fun qualifiedString2() = "9.876"

    @Provides @MyQualified("some_name1") fun something1() = SomeQualifiedClass()
    @Provides @MyQualified("some_name1", 1234) fun something1234() = SomeQualifiedClass()
    @Provides @MyQualified("some_name2", 456) fun something2() = SomeQualifiedClass()

    @MyQualified("double_set", 999)
    @Provides @IntoSet fun double1() = 1.23

    @MyQualified("double_set", 999)
    @Provides @IntoSet fun double2() = 12.3

    @MyQualified("double_set", 999)
    @Provides @IntoSet fun double3(/*@QualifiedString1 string: String*/) = 123.4 // string.toDouble()

    @MyQualified("double_set", 999)
    @Provides @IntoSet fun double4(/*@QualifiedString1 string: String*/) = 9.876 // string.toDouble()

    @Provides @IntoSet fun aDouble() = 4.56
}

@Qualifier
@Retention(AnnotationRetention.SOURCE)
annotation class QualifiedString1

@Qualifier
@Retention(AnnotationRetention.SOURCE)
annotation class QualifiedString2

@Qualifier
@Retention(AnnotationRetention.SOURCE)
annotation class MyQualified(val name: String, val intVal: Int = 123)

class SomeQualifiedClass
