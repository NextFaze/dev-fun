package com.nextfaze.devfun.demo.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nextfaze.devfun.demo.BaseActivity
import com.nextfaze.devfun.demo.BaseFragment
import com.nextfaze.devfun.demo.R
import com.nextfaze.devfun.demo.inject.ActivityInjector
import com.nextfaze.devfun.demo.inject.FragmentInjector
import javax.inject.Inject

class DaggerScopesActivity : BaseActivity() {
    @Inject lateinit var injectableNotScopedWithZeroArgsConstructor: InjectableNotScopedWithZeroArgsConstructor
    @Inject lateinit var injectableNotScopedWithMultipleArgsConstructor: InjectableNotScopedWithMultipleArgsConstructor

    @Inject lateinit var injectableActivityScopedViaAnnotation: InjectableActivityScopedViaAnnotation
    @Inject lateinit var injectableActivityScopedViaAnnotationWithArgs: InjectableActivityScopedViaAnnotationWithArgs
    @Inject lateinit var injectableActivityScopedViaProvides: InjectableActivityScopedViaProvides
    @Inject lateinit var injectableActivityScopedViaProvidesWithArgs: InjectableActivityScopedViaProvidesWithArgs

    @Inject lateinit var injectableRetainedScopedViaAnnotation: InjectableRetainedScopedViaAnnotation
    @Inject lateinit var injectableRetainedScopedViaAnnotationWithArgs: InjectableRetainedScopedViaAnnotationWithArgs
    @Inject lateinit var injectableRetainedScopedViaProvides: InjectableRetainedScopedViaProvides
    @Inject lateinit var injectableRetainedScopedViaProvidesWithArgs: InjectableRetainedScopedViaProvidesWithArgs

    @Inject lateinit var injectableSingletonScopedViaAnnotation: InjectableSingletonScopedViaAnnotation
    @Inject lateinit var injectableSingletonScopedViaAnnotationWithArgs: InjectableSingletonScopedViaAnnotationWithArgs
    @Inject lateinit var injectableSingletonScopedViaProvides: InjectableSingletonScopedViaProvides
    @Inject lateinit var injectableSingletonScopedViaProvidesWithArgs: InjectableSingletonScopedViaProvidesWithArgs

    @Inject internal lateinit var injectablePackagePrivateNotScoped: InjectablePackagePrivateNotScoped
    @Inject internal lateinit var injectablePackagePrivateNotScopedWithArgs: InjectablePackagePrivateNotScopedWithArgs
    @Inject internal lateinit var injectablePackagePrivateActivityViaAnnotation: InjectablePackagePrivateActivityViaAnnotation
    @Inject internal lateinit var injectablePackagePrivateRetainedViaAnnotation: InjectablePackagePrivateRetainedViaAnnotation
    @Inject internal lateinit var injectablePackagePrivateSingletonViaAnnotation: InjectablePackagePrivateSingletonViaAnnotation

    override fun inject(injector: ActivityInjector) = injector.inject(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_activity)

        if (savedInstanceState == null) {
            setContentFragment { DaggerScopesFragment() }
        }
    }
}

class DaggerScopesFragment : BaseFragment() {
    @Inject lateinit var injectableNotScopedWithZeroArgsConstructor: InjectableNotScopedWithZeroArgsConstructor
    @Inject lateinit var injectableNotScopedWithMultipleArgsConstructor: InjectableNotScopedWithMultipleArgsConstructor

    @Inject lateinit var injectableRetainedScopedViaAnnotation: InjectableRetainedScopedViaAnnotation
    @Inject lateinit var injectableRetainedScopedViaAnnotationWithArgs: InjectableRetainedScopedViaAnnotationWithArgs
    @Inject lateinit var injectableRetainedScopedViaProvides: InjectableRetainedScopedViaProvides
    @Inject lateinit var injectableRetainedScopedViaProvidesWithArgs: InjectableRetainedScopedViaProvidesWithArgs

    @Inject lateinit var injectableSingletonScopedViaAnnotation: InjectableSingletonScopedViaAnnotation
    @Inject lateinit var injectableSingletonScopedViaAnnotationWithArgs: InjectableSingletonScopedViaAnnotationWithArgs
    @Inject lateinit var injectableSingletonScopedViaProvides: InjectableSingletonScopedViaProvides
    @Inject lateinit var injectableSingletonScopedViaProvidesWithArgs: InjectableSingletonScopedViaProvidesWithArgs

    @Inject internal lateinit var injectablePackagePrivateNotScoped: InjectablePackagePrivateNotScoped
    @Inject internal lateinit var injectablePackagePrivateNotScopedWithArgs: InjectablePackagePrivateNotScopedWithArgs
    @Inject internal lateinit var injectablePackagePrivateRetainedViaAnnotation: InjectablePackagePrivateRetainedViaAnnotation
    @Inject internal lateinit var injectablePackagePrivateSingletonViaAnnotation: InjectablePackagePrivateSingletonViaAnnotation

    override fun inject(injector: FragmentInjector) = injector.inject(this)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.dagger_scopes_fragment, container, false)
}
