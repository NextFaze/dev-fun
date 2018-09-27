package com.nextfaze.devfun.demo.test;

import android.app.Activity;
import androidx.appcompat.app.AlertDialog;
import com.nextfaze.devfun.annotations.DeveloperFunction;
import com.nextfaze.devfun.demo.inject.RetainedScope;

import javax.inject.Inject;

@RetainedScope
@TestCat
class InjectablePackagePrivateRetainedViaAnnotation {

    @Inject
    InjectablePackagePrivateRetainedViaAnnotation() {
    }

    @DeveloperFunction
    public void validateSelf(Activity activity, InjectablePackagePrivateRetainedViaAnnotation self) {
        new AlertDialog.Builder(activity)
                .setMessage("this=" + this + "(" + activity + ")\nself=" + self + "\nthis===self: " + (this == self))
                .show();
    }
}
