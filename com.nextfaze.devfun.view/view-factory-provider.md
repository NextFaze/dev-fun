[gh-pages](../index.md) / [com.nextfaze.devfun.view](index.md) / [viewFactoryProvider](./view-factory-provider.md)

# viewFactoryProvider

`inline fun <reified K : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`, reified V : `[`View`](https://developer.android.com/reference/android/view/View.html)`> viewFactoryProvider(@LayoutRes layoutId: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, crossinline apply: `[`V`](view-factory-provider.md#V)`.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)` = {}): `[`ViewFactoryProvider`](-view-factory-provider/index.md) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/view/Helpers.kt#L57)

Convenience method to create a view factory provider for a single type key.

Example usage from demo (~line 64 `demoMenuHeaderFactory` in `com.nextfaze.devfun.demo.devfun.DevFun.kt`):

``` kotlin
// MenuHeader is the "key" (used by DevMenu to inflate the menu header)
// DemoMenuHeaderView is the custom view type
devFun.viewFactories += viewFactoryProvider<MenuHeader, DemoMenuHeaderView>(R.layout.demo_menu_header) {
    setTitle(activityProvider()!!::class.splitSimpleName)
    setCurrentUser(session.user)
}
```

*Be aware that this creates a [ViewFactoryProvider](-view-factory-provider/index.md) that returns a [ViewFactory](-view-factory/index.md) - thus inflation code could be executed at any time in any context.*

If you only need to create a [ViewFactory](-view-factory/index.md) then use convenience method [viewFactory](view-factory.md).

**See Also**

[ViewFactory](-view-factory/index.md)

