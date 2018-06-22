package com.nextfaze.devfun.demo.test;

import com.nextfaze.devfun.annotations.DeveloperFunction;
import com.nextfaze.devfun.demo.inject.ActivityScope;

import javax.inject.Inject;

@ActivityScope
class InjectablePackagePrivateActivityViaAnnotation {

    @Inject
    InjectablePackagePrivateActivityViaAnnotation() {
    }

    @DeveloperFunction
    public void validateSelf(InjectablePackagePrivateActivityViaAnnotation self) {
        System.out.println("this=" + this);
        System.out.println("self=" + self);
        System.out.println("this===self: " + (this == self));
    }
}
