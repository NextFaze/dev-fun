package com.nextfaze.devfun.demo.test;

import android.app.Activity;
import androidx.appcompat.app.AlertDialog;
import com.nextfaze.devfun.function.DeveloperFunction;

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
