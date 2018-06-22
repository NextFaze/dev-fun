package com.nextfaze.devfun.demo.test;

import com.nextfaze.devfun.annotations.DeveloperFunction;
import com.nextfaze.devfun.demo.inject.RetainedScope;

import javax.inject.Inject;

@RetainedScope
class InjectablePackagePrivateRetainedViaAnnotation {

    @Inject
    InjectablePackagePrivateRetainedViaAnnotation() {
    }

    @DeveloperFunction
    public void validateSelf(InjectablePackagePrivateRetainedViaAnnotation self) {
        System.out.println("this=" + this);
        System.out.println("self=" + self);
        System.out.println("this===self: " + (this == self));
    }
}
