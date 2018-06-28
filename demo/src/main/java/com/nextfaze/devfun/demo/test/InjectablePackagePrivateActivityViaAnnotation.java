package com.nextfaze.devfun.demo.test;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import com.nextfaze.devfun.annotations.DeveloperFunction;
import com.nextfaze.devfun.demo.inject.ActivityScope;

import javax.inject.Inject;

@ActivityScope
@TestCat
class InjectablePackagePrivateActivityViaAnnotation {

    @Inject
    InjectablePackagePrivateActivityViaAnnotation() {
    }

    @DeveloperFunction
    public void validateSelf(Activity activity, InjectablePackagePrivateActivityViaAnnotation self) {
        new AlertDialog.Builder(activity)
                .setMessage("this=" + this + "(" + activity + ")\nself=" + self + "\nthis===self: " + (this == self))
                .show();
    }
}
