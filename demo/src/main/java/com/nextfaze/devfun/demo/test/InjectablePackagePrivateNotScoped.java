package com.nextfaze.devfun.demo.test;

import com.nextfaze.devfun.annotations.DeveloperFunction;

import javax.inject.Inject;

class InjectablePackagePrivateNotScoped {

    @Inject
    InjectablePackagePrivateNotScoped() {
    }

    @DeveloperFunction
    public void validateSelf(InjectablePackagePrivateNotScoped self) {
        System.out.println("this=" + this);
        System.out.println("self=" + self);
        System.out.println("this===self: " + (this == self));
    }
}
