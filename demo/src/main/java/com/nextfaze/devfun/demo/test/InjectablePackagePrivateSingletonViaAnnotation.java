package com.nextfaze.devfun.demo.test;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import com.nextfaze.devfun.annotations.DeveloperFunction;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@TestCat
class InjectablePackagePrivateSingletonViaAnnotation {

    @Inject
    InjectablePackagePrivateSingletonViaAnnotation() {
    }

    @DeveloperFunction
    public void validateSelf(Activity activity, InjectablePackagePrivateSingletonViaAnnotation self) {
        new AlertDialog.Builder(activity)
                .setMessage("this=" + this + "(" + activity + ")\nself=" + self + "\nthis===self: " + (this == self))
                .show();
    }
}
