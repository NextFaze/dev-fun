[gh-pages](../../index.md) / [com.nextfaze.devfun.core](../index.md) / [DevFun](index.md) / [viewFactories](./view-factories.md)

# viewFactories

`val viewFactories: `[`CompositeViewFactoryProvider`](../../com.nextfaze.devfun.view/-composite-view-factory-provider.md) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/core/DevFun.kt#L201)

Composite list of all [ViewFactoryProvider](../../com.nextfaze.devfun.view/-view-factory-provider/index.md)s.

Various DevFun modules will use these factories for rendering custom views (by providing their own initially
and/or allowing user defined implementations).

Add view factory providers using [Composite.plusAssign](../-composite/plus-assign.md); `devFun.viewFactories += MyViewFactoryProvider()`.

Providers are checked in reverse order.
i.e. Most recently added are checked first.

### Utility Functions

* [viewFactory](../../com.nextfaze.devfun.view/view-factory.md) to create a ViewFactoryProvider that returns a [ViewFactory](../../com.nextfaze.devfun.view/-view-factory/index.md) for some layout
* [inflate](../../com.nextfaze.devfun.view/inflate.md) in your custom ViewFactoryProvider to create/inflate just some layout

### Example Usage

* Simple layout inflation (e.g. to change DevMenu header view):


``` kotlin
  // Using a custom view/layout
    devFun.viewFactories += viewFactory<MenuHeader, View>(R.layout.dev_fun_menu_header)
```


* From [demo](https://github.com/NextFaze/dev-fun/blob/master/demo/src/debug/java/com/nextfaze/devfun/demo/devfun/DevFun.kt#L70)
using/configuring a custom view type:


``` kotlin
  devFun.viewFactories += viewFactory<MenuHeader, DemoMenuHeaderView>(R.layout.demo_menu_header) {
        setTitle(activityProvider()!!::class.splitSimpleName)
        setCurrentUser(session.user)
    }
```


* Using a concrete ViewFactoryProvider (from [DevFun core](https://github.com/NextFaze/dev-fun/blob/master/devfun/src/main/java/com/nextfaze/devfun/view/Factory.kt#L80)):


``` kotlin
  private class DevFunSimpleInvokeViewsFactory : ViewFactoryProvider {
        override fun get(clazz: KClass<*>): ViewFactory<View>? =
            when (clazz) {
                InjectedParameterView::class -> inflate(R.layout.df_devfun_injected)
                ErrorParameterView::class -> inflate(R.layout.df_devfun_type_error)
                else -> null
            }
        }
    }

    devFun.viewFactories += DevFunSimpleInvokeViewsFactory()
```

**See Also**

[parameterViewFactories](parameter-view-factories.md)

**Getter**

Composite list of all [ViewFactoryProvider](../../com.nextfaze.devfun.view/-view-factory-provider/index.md)s.

Various DevFun modules will use these factories for rendering custom views (by providing their own initially
and/or allowing user defined implementations).

Add view factory providers using [Composite.plusAssign](../-composite/plus-assign.md); `devFun.viewFactories += MyViewFactoryProvider()`.

Providers are checked in reverse order.
i.e. Most recently added are checked first.

### Utility Functions

* [viewFactory](../../com.nextfaze.devfun.view/view-factory.md) to create a ViewFactoryProvider that returns a [ViewFactory](../../com.nextfaze.devfun.view/-view-factory/index.md) for some layout
* [inflate](../../com.nextfaze.devfun.view/inflate.md) in your custom ViewFactoryProvider to create/inflate just some layout

### Example Usage

* Simple layout inflation (e.g. to change DevMenu header view):


``` kotlin
  // Using a custom view/layout
    devFun.viewFactories += viewFactory<MenuHeader, View>(R.layout.dev_fun_menu_header)
```


* From [demo](https://github.com/NextFaze/dev-fun/blob/master/demo/src/debug/java/com/nextfaze/devfun/demo/devfun/DevFun.kt#L70)
using/configuring a custom view type:


``` kotlin
  devFun.viewFactories += viewFactory<MenuHeader, DemoMenuHeaderView>(R.layout.demo_menu_header) {
        setTitle(activityProvider()!!::class.splitSimpleName)
        setCurrentUser(session.user)
    }
```


* Using a concrete ViewFactoryProvider (from [DevFun core](https://github.com/NextFaze/dev-fun/blob/master/devfun/src/main/java/com/nextfaze/devfun/view/Factory.kt#L80)):


``` kotlin
  private class DevFunSimpleInvokeViewsFactory : ViewFactoryProvider {
        override fun get(clazz: KClass<*>): ViewFactory<View>? =
            when (clazz) {
                InjectedParameterView::class -> inflate(R.layout.df_devfun_injected)
                ErrorParameterView::class -> inflate(R.layout.df_devfun_type_error)
                else -> null
            }
        }
    }

    devFun.viewFactories += DevFunSimpleInvokeViewsFactory()
```

**Getter See Also**

[parameterViewFactories](parameter-view-factories.md)

