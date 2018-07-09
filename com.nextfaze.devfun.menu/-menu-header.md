[gh-pages](../index.md) / [com.nextfaze.devfun.menu](index.md) / [MenuHeader](./-menu-header.md)

# MenuHeader

`interface MenuHeader` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-menu/src/main/java/com/nextfaze/devfun/menu/DeveloperMenu.kt#L90)

The view type/key used by DevMenu to find/inflate the menu header view.

Example usage from demo (~line 64 `demoMenuHeaderFactory` in `com.nextfaze.devfun.demo.devfun.DevFun.kt`):

``` kotlin
// MenuHeader is the "key" (used by DevMenu to inflate the menu header)
// DemoMenuHeaderView is the custom view type
devFun.viewFactories += viewFactory<MenuHeader, DemoMenuHeaderView>(R.layout.demo_menu_header) {
    setTitle(activityProvider()!!::class.splitSimpleName)
    setCurrentUser(session.user)
}
```

**See Also**

[viewFactory](../com.nextfaze.devfun.view/view-factory.md)

