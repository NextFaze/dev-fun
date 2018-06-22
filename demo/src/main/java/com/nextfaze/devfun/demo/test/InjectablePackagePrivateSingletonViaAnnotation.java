package com.nextfaze.devfun.demo.test;

import com.nextfaze.devfun.annotations.DeveloperFunction;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class InjectablePackagePrivateSingletonViaAnnotation {

    @Inject
    InjectablePackagePrivateSingletonViaAnnotation() {
    }

    @DeveloperFunction
    public void validateSelf(InjectablePackagePrivateSingletonViaAnnotation self) {
        System.out.println("this=" + this);
        System.out.println("self=" + self);
        System.out.println("this===self: " + (this == self));
    }
}
