package com.nextfaze.devfun.demo.test;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import com.nextfaze.devfun.annotations.DeveloperFunction;

import javax.inject.Inject;

@TestCat
class InjectablePackagePrivateNotScopedWithArgs {

    private Context context;

    @Inject
    InjectablePackagePrivateNotScopedWithArgs(Context context) {
        this.context = context;
    }

    @DeveloperFunction
    public void validateSelf(Activity activity, InjectablePackagePrivateNotScopedWithArgs self) {
        new AlertDialog.Builder(activity)
                .setMessage("this=" + this + "(" + context + ")\nself=" + self + "\nthis===self: " + (this == self))
                .show();
    }
}
