package com.nextfaze.devfun.demo.test;

import com.nextfaze.devfun.function.DeveloperFunction;

class PackagePrivateClass {

    @DeveloperFunction
    private void blah() {
        System.out.println("Hello World!");
    }
}
