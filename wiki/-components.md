[gh-pages](../index.md) / [wiki](index.md) / [Components](./-components.md)

# Components

`object Components` [(source)](https://github.com/NextFaze/dev-fun/tree/master/dokka/src/main/java/wiki/Components.kt#L422)

DevFun is designed to be modular, in terms of both its dependencies (limiting impact to main source tree) and its plugin-like architecture. ![Component Dependencies](https://github.com/NextFaze/dev-fun/raw/gh-pages/assets/uml/components.png)

* [Main Modules](#main-modules)
  * [Annotations](#annotations)
  * [Compiler](#compiler)
  * [Gradle Plugin](#gradle-plugin)
* [Core Modules](#core-modules)
  * [DevFun](#devfun)
  * [Menu](#menu)
* [Inject Modules](#inject-modules)
  * [Dagger 2](#dagger-2)
      * [Supported Versions](#supported-versions)
      * [Limitations](#limitations)
      * [Instance and Component Resolution](#instance-and-component-resolution)
          * [Reflection Based](#reflection-based)
          * [Annotation Based](#annotation-based)
          * [Custom Instance Provider](#custom-instance-provider)
* [Util Modules](#util-modules)
  * [Glide](#glide)
  * [Leak Canary](#leak-canary)
* [Invoke Modules](#invoke-modules)
  * [Color Picker View](#color-picker-view)
* [Experimental Modules](#experimental-modules)
  * [HttpD](#httpd)
      * [Custom Port](#custom-port)
  * [Http Front-end](#http-front-end)
  * [Stetho](#stetho)

## Main Modules

Minimum required libraries - annotations and annotation processor.

<img src="https://github.com/NextFaze/dev-fun/raw/gh-pages/assets/gif/enable-sign-in.gif" alt="DevFun demonstration" width="35%" align="right"/>

### Annotations

Provides DevFun annotations and various interface definitions:

* [DeveloperFunction](../com.nextfaze.devfun.function/-developer-function/index.md)
* [DeveloperCategory](../com.nextfaze.devfun.category/-developer-category/index.md)
* [DeveloperReference](../com.nextfaze.devfun.reference/-developer-reference/index.md)
* [DeveloperAnnotation](../com.nextfaze.devfun/-developer-annotation/index.md)
* [DeveloperLogger](../com.nextfaze.devfun.reference/-developer-logger/index.md)
* [DeveloperProperty](../com.nextfaze.devfun.function/-developer-property/index.md)
* [Dagger2Component](../com.nextfaze.devfun.reference/-dagger2-component/index.md)

This library contains primarily interface definitions and inline functions, and will have a
negligible impact on your method count and dex sizes. Apply to your main `compile` configuration:

``` kotlin
implementation("com.nextfaze.devfun:devfun-annotations:2.1.0")
```

### Compiler

Annotation processor [DevFunProcessor](../com.nextfaze.devfun.compiler/-dev-fun-processor/index.md) that handles [DeveloperFunction](../com.nextfaze.devfun.function/-developer-function/index.md), [DeveloperCategory](../com.nextfaze.devfun.category/-developer-category/index.md), [DeveloperReference](../com.nextfaze.devfun.reference/-developer-reference/index.md), and [DeveloperAnnotation](../com.nextfaze.devfun/-developer-annotation/index.md) annotations.

This should be applied to your non-main kapt configuration 'kaptDebug' to avoid running/using it on release builds.

``` kotlin
kaptDebug("com.nextfaze.devfun:devfun-compiler:2.1.0")
```

Configuration options can be applied using Android DSL:

``` kotlin
android {
     defaultConfig {
         javaCompileOptions {
             annotationProcessorOptions {
                 argument("devfun.argument", "value")
             }
         }
     }
}
```

Full list available at [com.nextfaze.devfun.compiler](../com.nextfaze.devfun.compiler/index.md).

### Gradle Plugin

Used to configure/provide the compiler with the project/build configurations.

In your `build.gradle` add the DevFun Gradle plugin to your build script.

If you can use the Gradle `plugins` block (which you should be able to do - this locates and downloads it for you):

``` kotlin
plugins {
    id("com.nextfaze.devfun") version "2.1.0"
}
```

**Or** the legacy method using `apply`;
Add the plugin to your classpath (found in the `jcenter()` repository):

``` kotlin
buildscript {
    dependencies {
        classpath("com.nextfaze.devfun:devfun-gradle-plugin:2.1.0")
    }
}
```

And in your `build.gradle`:

``` kotlin
apply {
    plugin("com.nextfaze.devfun")
}
```

## Core Modules

Modules that extend the accessibility of DevFun (e.g. add menu/http server).

*Also see [Experimental Modules](#experimental-modules) below.*

### DevFun

Core of [DevFun](../com.nextfaze.devfun.core/-dev-fun/index.md). Loads modules and definitions.

Apply to your non-main configuration:

``` kotlin
debugImplementation("com.nextfaze.devfun:devfun:2.1.0")
```

Modules are loaded by [DevFun](../com.nextfaze.devfun.core/-dev-fun/index.md) using Java's [ServiceLoader](https://developer.android.com/reference/java/util/ServiceLoader.html).

[DevFun](../com.nextfaze.devfun.core/-dev-fun/index.md) loads, transforms, and sorts the generated function definitions, again via the [ServiceLoader](https://developer.android.com/reference/java/util/ServiceLoader.html) mechanism.
To inject function invocations, [InstanceProvider](../com.nextfaze.devfun.inject/-instance-provider/index.md)s are used, which will attempt to locate (or create) object instances.
A composite instance provider [CompositeInstanceProvider](../com.nextfaze.devfun.inject/-composite-instance-provider.md) at [DevFun.instanceProviders](../com.nextfaze.devfun.core/-dev-fun/instance-providers.md) is used via convenience
function (extension) [FunctionItem.call](../com.nextfaze.devfun.core/call.md) that uses the currently loaded [devFun](../com.nextfaze.devfun.core/dev-fun.md) instance.

If using Dagger 2.x, you can use the `devfun-inject-dagger2` module for a simple reflection based provider or related helper
functions. A heavily reflective version will be used automatically, but if it fails (e.g. it expects a `Component` in
your application class), a manual implementation can be provided.
See the demo app [DemoInstanceProvider](https://github.com/NextFaze/dev-fun/tree/master/demo/src/debug/java/com/nextfaze/devfun/demo/devfun/DevFun.kt#L52) for a sample implementation.

<img src="https://github.com/NextFaze/dev-fun/raw/gh-pages/assets/gif/registration-flow.gif" alt="Menu demonstration" width="35%" align="right"/>

### Menu

Adds a developer menu [DevMenu](../com.nextfaze.devfun.menu/-dev-menu/index.md), accessible by a floating cog [CogOverlay](../com.nextfaze.devfun.menu.controllers/-cog-overlay/index.md) (long-press to drag) or device button sequence [KeySequence](../com.nextfaze.devfun.menu.controllers/-key-sequence/index.md).

``` kotlin
debugImplementation("com.nextfaze.devfun:menu:2.1.0")
```

Button sequences: *(this are not configurable at the moment but are intended to be eventually)*

``` kotlin
internal val GRAVE_KEY_SEQUENCE = KeySequence.Definition(
    keyCodes = intArrayOf(KeyEvent.KEYCODE_GRAVE),
    description = R.string.df_menu_grave_sequence,
    consumeEvent = true
)
internal val VOLUME_KEY_SEQUENCE = KeySequence.Definition(
    keyCodes = intArrayOf(
        KeyEvent.KEYCODE_VOLUME_DOWN,
        KeyEvent.KEYCODE_VOLUME_DOWN,
        KeyEvent.KEYCODE_VOLUME_UP,
        KeyEvent.KEYCODE_VOLUME_DOWN
    ),
    description = R.string.df_menu_volume_sequence,
    consumeEvent = false
)
```

Menu controllers implement [MenuController](../com.nextfaze.devfun.menu/-menu-controller/index.md) and can be added via `devFun.module<DevMenu>() += MyMenuController()`.

## Inject Modules

Modules to facilitate dependency injection for function invocation.

### Dagger 2

Adds module [InjectFromDagger2](../com.nextfaze.devfun.inject.dagger2/-inject-from-dagger2/index.md) which adds an [InstanceProvider](../com.nextfaze.devfun.inject/-instance-provider/index.md) that can reflectively locate components or (if used) resolve
[Dagger2Component](../com.nextfaze.devfun.reference/-dagger2-component/index.md) uses. Tested from Dagger 2.4 to 2.17.

``` kotlin
debugImplementation("com.nextfaze.devfun:devfun-inject-dagger2:2.1.0")
```

Simply graphs should be well supported. More complex graphs *should* work (it has been working well in-house). Please report any issues you
encounter.

The module also provides a variety of utility functions for manually providing your own instance provider using your components. See below
for more details.

*I'm always looking into better ways to support this, comments/suggestions are welcome.*

* Currently kapt doesn't support multi-staged processing of generated Kotlin code.
* Possibly consider generating Java `Component` interfaces for some types?
* Likely will investigate the new SPI functionality in Dagger 2.17+ once it becomes more stable.

#### Supported Versions

Dagger has been tested on the demo app from versions 2.4 to 2.17, and various in-house apps on more recent versions, and should function
correctly for most simple scopes/graphs.

For reference the demo app uses three scopes; Singleton, Retained (fragments), and an Activity scope. It uses both type-annotated scoping
and provides scoping. It keeps component instances in the activity and obtains the singleton scope via an extension function. In general
this should cover most use cases - if you encounter any problems please create an issue.

#### Limitations

DevFun uses a number of methods iteratively to introspect the generated components/modules, however depending on scoping, visibility, and
instantiation of a type it can be difficult to determine the source/scope in initial (but faster) introspection methods.

When all else fails DevFun will use a form of heavy reflection to introspect the generated code - types with a custom scope and no
constructor arguments are not necessarily obtainable from Dagger (depends on the version) by any other means. To help with this ensure your
scope is `@Retention(RUNTIME)` so that DevFun wont unintentionally create a new instance when it can't find it right away.

Due to the way Dagger generates/injects it is not possible to obtain the instance of non-scoped types from the generated component/module
as its instance is created/injected once (effectively inlined) at the inject site. It is intended to allow finding instances based on the
context of the dev. function in the future (i.e. if the dev. function is in a fragment then check for the injected instance in the fragment
etc.) - if this is desirable sooner make a comment in the issue [#26](https://github.com/NextFaze/dev-fun/issues/26).

#### Instance and Component Resolution

Unless you specify [Dagger2Component](../com.nextfaze.devfun.reference/-dagger2-component/index.md) annotations, DevFun will use a heavy-reflection based provider. Where possible DevFun will cache
the locations of where it found various types - this is somewhat loose in that the provider cache still attempts to be aware of scoping.

##### Reflection Based

By default simply including the module will use the reflection-based component locator.

It will attempt to locate your component objects in your application class and/or your activity classes and use aforementioned utility
functions.

If you place one or more [Dagger2Component](../com.nextfaze.devfun.reference/-dagger2-component/index.md) annotations (see below), then the reflective locator wont be used.

##### Annotation Based

For more control, or if the above method doesn't (such as if you use top-level extension functions to retrieve your components, or you put
them in weird places, or for whatever reason), then you can annotate the functions/getters with [Dagger2Component](../com.nextfaze.devfun.reference/-dagger2-component/index.md).
The scope/broadness/priority can be set on the annotation either via [Dagger2Component.scope](../com.nextfaze.devfun.reference/-dagger2-component/scope.md) or [Dagger2Component.priority](../com.nextfaze.devfun.reference/-dagger2-component/priority.md).
If unset then the scope will be assumed based on the context of its location (i.e. in Application class &gt; probably the top level component,
if static then first argument assumed to be the receiver, etc).

Note: For properties you can annotated to property itself (`@Dagger2Component`) or the getter explicitly (`@get:Dagger2Component`) if for
some reason on the property doesn't work (which could happen if it can't find your getter - which is done via method name string
manipulation due to KAPT limitations.

Example usage:

* Where a top-level/singleton/application component is retrieved via an extension function *(from the demo)*:

``` kotlin
@Dagger2Component
val Context.applicationComponent: ApplicationComponent?
    get() = (applicationContext as DaggerApplication).applicationComponent
```

* Where a component is kept in the activity *(from the demo)*:

``` kotlin
@get:Dagger2Component // if we want to specify getter explicitly
lateinit var activityComponent: ActivityComponent
    private set
```

* Where a differently scoped component is also kept in the activity, we can set the scope manually ([Dagger2Scope](../com.nextfaze.devfun.reference/-dagger2-scope/index.md)) *(from the demo)*:

``` kotlin
@get:Dagger2Component(Dagger2Scope.RETAINED_FRAGMENT)
lateinit var retainedComponent: RetainedComponent
    private set
```

##### Custom Instance Provider

Since the reflection locator and annotation based still make assumptions and are bit inefficient because of it, sometimes you may need to
implement your own instance provider.

* Disable the automatic locator: set [useAutomaticDagger2Injector](../com.nextfaze.devfun.inject.dagger2/use-automatic-dagger2-injector.md) to `false` (can be done at any time).
* Add your own provider using `devFun += MyProvider` (see [InstanceProvider](../com.nextfaze.devfun.inject/-instance-provider/index.md) for more details).
* Utility function [tryGetInstanceFromComponent](../com.nextfaze.devfun.inject.dagger2/try-get-instance-from-component.md) to help (though again it relies heavily on reflection and don't consider scoping very well).

See demo for example implementation: [DemoInstanceProvider](https://github.com/NextFaze/dev-fun/tree/master/demo/src/debug/java/com/nextfaze/devfun/demo/devfun/DevFun.kt#L52)

## Util Modules

Modules with frequently used or just handy functions (e.g. show Glide memory use).

Developers love reusable utility functions, we all have them, and we usually copy-paste them into new projects.
Adding them to modules and leveraging dependency injection allows for non-static, easily invokable code reuse.

*Still playing with this concept and naming conventions etc.*

### Glide

Module [GlideUtils](../com.nextfaze.devfun.utils.glide/-glide-utils/index.md) provides some utility functions when using Glide.

``` kotlin
debugImplementation("com.nextfaze.devfun:devfun-util-glide:2.1.0")
```

Features:

* Clear memory cache
* Clear disk cache
* Log current memory/disk cache usage

### Leak Canary

Module [LeakCanaryUtils](../com.nextfaze.devfun.utils.leakcanary/-leak-canary-utils/index.md) provides some utility functions when using Leak Canary.

``` kotlin
debugImplementation("com.nextfaze.devfun:devfun-util-leakcanary:2.1.0")
```

Features:

* Launch `DisplayLeakActivity`

<img src="https://github.com/NextFaze/dev-fun/raw/gh-pages/assets/images/color-picker.png" alt="Invocation UI with custom color picker view" width="35%" align="right"/>

## Invoke Modules

Modules to facilitate function invocation.

### Color Picker View

Adds a parameter annotation [ColorPicker](../com.nextfaze.devfun.invoke.view/-color-picker/index.md) that lets the invocation UI render a color picker view for the associated argument.

*Note: Only needed if you don't include `devfun-menu` (as it uses/includes the color picker transitively).*

``` kotlin
debugImplementation("com.nextfaze.devfun-invoke-view-colorpicker:2.1.0")
```

## Experimental Modules

These modules are mostly for use experimenting with various use-cases.

They generally work, but are likely to be buggy and have various limitations and nuances.
Having said that, it would be nice to expand upon them and make them nicer/more feature reach in the future.

Some future possibilities:

* HttpD: Use ktor instead of Nano
* HttpD Simple Index: Provide a themed react page or something pretty/nice
* Add a Kotlin REPL module

### HttpD

Module [DevHttpD](../com.nextfaze.devfun.httpd/-dev-http-d/index.md) adds a local HTTP server (uses [NanoHttpD](https://github.com/NanoHttpd/nanohttpd)).

Provides a single `POST` method `invoke` with one parameter `hashCode` (expecting [FunctionItem.hashCode](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/hash-code.html))

``` kotlin
debugImplementation("com.nextfaze.devfun:httpd:2.1.0")
```

Use with HttpD Front-end.
*Current port and access instructions are logged on start.*

Default port is `23075`.
If this is in use another is deterministically generated from your package (this might become the default).

If using AVD, forward port:

``` bash
adb forward tcp:23075 tcp:23075
```

Then access via IP: [http://127.0.0.1:23075/](http://127.0.0.1:23075/)

#### Custom Port

Can be set via resources:

``` xml
<integer name="df_httpd_default_port">12345</integer>
```

Or before initialization [devDefaultPort](../com.nextfaze.devfun.httpd/dev-default-port.md) (i.e. if not using auto-init content provider):

``` kotlin
devDefaultPort = 12345 // top level value located in com.nextfaze.devfun.httpd
```

### Http Front-end

Module [HttpFrontEnd](../com.nextfaze.devfun.httpd.frontend/-http-front-end/index.md) generates an admin interface using SB Admin 2 (similar to [DevMenu](../com.nextfaze.devfun.menu/-dev-menu/index.md)), allowing function invocation from a browser.

**Depends on [DevHttpD](../com.nextfaze.devfun.httpd/-dev-http-d/index.md).**

``` kotlin
debugImplementation("com.nextfaze.devfun:httpd-frontend:2.1.0")
```

Page is rather simple at the moment, but in the future it's somewhat intended (as a learning exercise) to create a React front end using
Kotlin or something.

![HTTP Server](https://github.com/NextFaze/dev-fun/raw/gh-pages/assets/images/httpd-auth-context.png)

### Stetho

Module [DevStetho](../com.nextfaze.devfun.stetho/-dev-stetho/index.md) allows generated methods to be invoked from Chrome's Dev Tools JavaScript console.

``` kotlin
debugImplementation("com.nextfaze.devfun:devfun-stetho:2.1.0")
```

Opening console will show available functions.
e.g. `Context_Enable_Account_Creation()`

*Extremely experimental and limited functionality.*

![Stetho Integration](https://github.com/NextFaze/dev-fun/raw/gh-pages/assets/images/stetho-auth.png)

