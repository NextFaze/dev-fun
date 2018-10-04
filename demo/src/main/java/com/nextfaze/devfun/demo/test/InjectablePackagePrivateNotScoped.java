package com.nextfaze.devfun.demo.test;

import android.app.Activity;
import androidx.appcompat.app.AlertDialog;
import com.nextfaze.devfun.function.DeveloperFunction;

import javax.inject.Inject;

@TestCat
class InjectablePackagePrivateNotScoped {

    @Inject
    InjectablePackagePrivateNotScoped() {
    }

    @DeveloperFunction
    public void validateSelf(Activity activity, InjectablePackagePrivateNotScoped self) {
        new AlertDialog.Builder(activity)
                .setMessage("this=" + this + "(" + activity + ")\nself=" + self + "\nthis===self: " + (this == self))
                .show();
    }
}
