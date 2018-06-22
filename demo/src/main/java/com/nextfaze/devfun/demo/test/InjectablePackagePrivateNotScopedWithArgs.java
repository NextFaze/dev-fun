package com.nextfaze.devfun.demo.test;

import android.content.Context;
import com.nextfaze.devfun.annotations.DeveloperFunction;

import javax.inject.Inject;

class InjectablePackagePrivateNotScopedWithArgs {

    private Context context;

    @Inject
    InjectablePackagePrivateNotScopedWithArgs(Context context) {
        this.context = context;
    }

    @DeveloperFunction
    public void validateSelf(InjectablePackagePrivateNotScopedWithArgs self) {
        System.out.println("this=" + this + "(" + context + ")");
        System.out.println("self=" + self);
        System.out.println("this===self: " + (this == self));
    }
}
