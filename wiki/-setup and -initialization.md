[gh-pages](../index.md) / [wiki](index.md) / [Setup and Initialization](.)

# Setup and Initialization

`object Setup and Initialization` [(source)](https://github.com/NextFaze/dev-fun/tree/master/dokka/src/main/java/wiki/SetupAndInitialization.kt#L49)

Compiler configuration and initialization process.

Attempts have been made to require as little set-up as possible.
However many of us inherit a code base that is not necessarily so... Nice...
Thus where possible most things can be initialized and configured manually.

If you encounter any instances where something can't be initialized manually or you have difficulties doing so (documentation
not clear etc.), then please create an issue about it - I have not extensively tested this aspect.

## Compiler Configuration

The compiler will attempt to auto-detect project details by using the class output directory and known/standard relative
paths to build files, but if necessary a number of aspects can be set as annotation processor args (see [com.nextfaze.devfun.compiler](../com.nextfaze.devfun.compiler/index.md)).

## Initialization

### Automatic

A Content Provider [DevFunInitializerProvider](../com.nextfaze.devfun.core/-dev-fun-initializer-provider/index.md) is used to automatically initialize DevFun.

This can be disabled using standard Android manifest merger syntax:

``` xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
          xmlns:tools="http://schemas.android.com/tools">

    <application>
        <!-- This will stop the provider node from being included -->
        <provider android:name="com.nextfaze.devfun.core.DevFunInitializerProvider"
                  android:authorities="*"
                  tools:ignore="ExportedContentProvider"
                  tools:node="remove"/>
    </application>
</manifest>
```

### Manual

Manual initialization can be performed at any time.
See [DevFun.initialize](../com.nextfaze.devfun.core/-dev-fun/initialize.md) for more information.

``` kotlin
DevFun().initialize(application)
```

