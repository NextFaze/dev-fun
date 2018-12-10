[gh-pages](../../index.md) / [com.nextfaze.devfun.core](../index.md) / [DevFunInitializerProvider](./index.md)

# DevFunInitializerProvider

`class DevFunInitializerProvider : `[`ContentProvider`](https://developer.android.com/reference/android/content/ContentProvider.html) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/core/DevFun.kt#L75)

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

**See Also**

[DevFun.initialize](../-dev-fun/initialize.md)

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `DevFunInitializerProvider()`<br>Used to automatically initialize [DevFun](../-dev-fun/index.md) without user input. |

### Functions

| Name | Summary |
|---|---|
| [delete](delete.md) | `fun delete(uri: `[`Uri`](https://developer.android.com/reference/android/net/Uri.html)`, selection: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, selectionArgs: `[`Array`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)`<out `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>?): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>NOP |
| [getType](get-type.md) | `fun getType(uri: `[`Uri`](https://developer.android.com/reference/android/net/Uri.html)`): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>NOP |
| [insert](insert.md) | `fun insert(uri: `[`Uri`](https://developer.android.com/reference/android/net/Uri.html)`, values: `[`ContentValues`](https://developer.android.com/reference/android/content/ContentValues.html)`): `[`Uri`](https://developer.android.com/reference/android/net/Uri.html)<br>NOP |
| [onCreate](on-create.md) | `fun onCreate(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [query](query.md) | `fun query(uri: `[`Uri`](https://developer.android.com/reference/android/net/Uri.html)`, projection: `[`Array`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)`<out `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>?, selection: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, selectionArgs: `[`Array`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)`<out `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>?, sortOrder: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): `[`Cursor`](https://developer.android.com/reference/android/database/Cursor.html)`?`<br>NOP |
| [update](update.md) | `fun update(uri: `[`Uri`](https://developer.android.com/reference/android/net/Uri.html)`, values: `[`ContentValues`](https://developer.android.com/reference/android/content/ContentValues.html)`, selection: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, selectionArgs: `[`Array`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)`<out `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>?): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>NOP |
