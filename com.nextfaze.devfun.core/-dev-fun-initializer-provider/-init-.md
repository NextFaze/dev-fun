[gh-pages](../../index.md) / [com.nextfaze.devfun.core](../index.md) / [DevFunInitializerProvider](index.md) / [&lt;init&gt;](.)

# &lt;init&gt;

`DevFunInitializerProvider()`

Used to automatically initialize [DevFun](../-dev-fun/index.md) without user input.

If you want to manually initialize DevFun, remove the node using standard Android manifest merger syntax:

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

