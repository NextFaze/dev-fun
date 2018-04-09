package wiki

import com.nextfaze.devfun.annotations.DeveloperCategory
import com.nextfaze.devfun.annotations.DeveloperFunction
import com.nextfaze.devfun.compiler.DevFunProcessor
import com.nextfaze.devfun.core.DevFun
import com.nextfaze.devfun.core.FunctionItem
import com.nextfaze.devfun.core.call
import com.nextfaze.devfun.core.devFun
import com.nextfaze.devfun.httpd.DevHttpD
import com.nextfaze.devfun.httpd.devDefaultPort
import com.nextfaze.devfun.httpd.frontend.HttpFrontEnd
import com.nextfaze.devfun.inject.CompositeInstanceProvider
import com.nextfaze.devfun.inject.InstanceProvider
import com.nextfaze.devfun.inject.dagger2.InjectFromDagger2
import com.nextfaze.devfun.inject.dagger2.useAutomaticDagger2Injector
import com.nextfaze.devfun.menu.DevMenu
import com.nextfaze.devfun.menu.MenuController
import com.nextfaze.devfun.menu.controllers.CogOverlay
import com.nextfaze.devfun.menu.controllers.KeySequence
import com.nextfaze.devfun.stetho.DevStetho
import com.nextfaze.devfun.utils.glide.GlideUtils
import com.nextfaze.devfun.utils.leakcanary.LeakCanaryUtils
import java.util.ServiceLoader

/**
DevFun is designed to be modular, in terms of both its dependencies (limiting impact to main source tree) and its plugin-like architecture. ![Component Dependencies](https://github.com/NextFaze/dev-fun/raw/gh-pages/assets/uml/components.png)

 * <!-- START doctoc generated TOC please keep comment here to allow auto update -->
 * <!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
 * 
 * 
 * - [Main Modules](#main-modules)
 *     - [Annotations](#annotations)
 *     - [Compiler](#compiler)
 * - [Core Modules](#core-modules)
 *     - [DevFun](#devfun)
 *     - [Menu](#menu)
 * - [Inject Modules](#inject-modules)
 *     - [Dagger 2](#dagger-2)
 * - [Util Modules](#util-modules)
 *     - [Glide](#glide)
 *     - [Leak Canary](#leak-canary)
 * - [Experimental Modules](#experimental-modules)
 *     - [HttpD](#httpd)
 *         - [Custom Port](#custom-port)
 *     - [Http Front-end](#http-front-end)
 *     - [Stetho](#stetho)
 * 
 * <!-- END doctoc generated TOC please keep comment here to allow auto update -->

## Main Modules
Minimum required libraries - annotations and annotation processor.


### Annotations
Contains the annotations [DeveloperFunction] and [DeveloperCategory], and various interface definitions.

This library contains primarily interface definitions and inline functions, and will have a
negligible impact on your method count and dex sizes. Apply to your main `compile` configuration:
```gradle
compile 'com.nextfaze.devfun:devfun-annotations:0.2.0'
```


### Compiler
Annotation processor [DevFunProcessor] that handles [DeveloperFunction] and [DeveloperCategory] annotations.

This should be applied to your non-main kapt configuration 'kaptDebug' to avoid running/using it on release builds.
```gradle
kaptDebug 'com.nextfaze.devfun:devfun-compiler:0.2.0'
```

Configuration options can be applied using Android DSL:
 * ```gradle
 *  android {
 *       defaultConfig {
 *           javaCompileOptions {
 *               annotationProcessorOptions {
 *                   argument 'devfun.argument', 'value'
 *               }
 *           }
 *       }
 *  }
 * ```
Full list available at [com.nextfaze.devfun.compiler].


## Core Modules
Modules that extend the accessibility of DevFun (e.g. add menu/http server).

_Also see [Experimental Modules](#experimental-modules) below._


### DevFun
Core of [DevFun]. Loads modules and definitions.

Apply to your non-main configuration:
```gradle
debugCompile 'com.nextfaze.devfun:devfun:0.2.0'
```

Modules are loaded by [DevFun] using Java's [ServiceLoader].

[DevFun] loads, transforms, and sorts the generated function definitions, again via the [ServiceLoader] mechanism.
To inject function invocations, [InstanceProvider]s are used, which will attempt to locate (or create) object instances.
A composite instance provider [CompositeInstanceProvider] at [DevFun.instanceProviders] is used via convenience
function (extension) [FunctionItem.call] that uses the currently loaded [devFun] instance.

If using Dagger 2.x, you can use the `devfun-inject-dagger2` module for a simple reflection based provider or related helper
functions. A heavily reflective version will be used automatically, but if it fails (e.g. it expects a `Component` in
your application class), a manual implementation can be provided.
See the demo app [DemoInstanceProvider](https://github.com/NextFaze/dev-fun/tree/master/demo/src/debug/java/com/nextfaze/devfun/demo/devfun/DevFun.kt#L34) for a sample implementation.

### Menu
Adds a developer menu [DevMenu], accessible by a floating cog [CogOverlay] (long-press to drag) or device button sequence [KeySequence].
```gradle
debugCompile 'com.nextfaze.devfun:menu:0.2.0'
```

Button sequence: *(this is not configurable at the moment but is intended to be eventually)*
 * ```kotlin
 * private val DEFAULT_KEY_SEQUENCE = intArrayOf(
 *     KeyEvent.KEYCODE_VOLUME_DOWN,
 *     KeyEvent.KEYCODE_VOLUME_DOWN,
 *     KeyEvent.KEYCODE_VOLUME_UP,
 *     KeyEvent.KEYCODE_VOLUME_DOWN
 * )
 * ```

Menu controllers implement [MenuController] and can be added via `devFun.module<DevMenu>() += MyMenuController()`.



## Inject Modules
Modules to facilitate dependency injection for function invocation.


### Dagger 2
Adds module [InjectFromDagger2] which adds an [InstanceProvider] that reflectively uses Dagger 2 components.

Provides default heavy-reflection based Dagger 2 injector and convenience functions for reflectively locating object instances from Dagger 2.x `@Component` objects.
```gradle
debugCompile 'com.nextfaze.devfun:devfun-inject-dagger2:0.2.0'
```

It only really supports simple graphs by finding provides methods/fields that match (or are a super type) of the requested type (scoping is not considered etc.).

To disable and implement manually (if needed and/or for better performance):
- [useAutomaticDagger2Injector]
- [DemoInstanceProvider](https://github.com/NextFaze/dev-fun/tree/master/demo/src/debug/java/com/nextfaze/devfun/demo/devfun/DevFun.kt#L34)

_I'm looking into better ways to support this, comments/suggestions are welcome._
- Currently kapt doesn't support multi-staged processing of generated Kotlin code.
- Possibly consider generating Java `Component` interfaces for some types?



## Util Modules
Modules with frequently used or just handy functions (e.g. show Glide memory use).

Developers love reusable utility functions, we all have them, and we usually copy-paste them into new projects.
Adding them to modules and leveraging dependency injection allows for non-static, easily invokable code reuse.

_Still playing with this concept and naming conventions etc._


### Glide
Module [GlideUtils] provides some utility functions when using Glide.
```gradle
debugCompile 'com.nextfaze.devfun:devfun-util-glide:0.2.0'
```

Features:
- Clear memory cache
- Clear disk cache
- Log current memory/disk cache usage


### Leak Canary
Module [LeakCanaryUtils] provides some utility functions when using Leak Canary.
```gradle
debugCompile 'com.nextfaze.devfun:devfun-util-leakcanary:0.2.0'
```

Features:
- Launch `DisplayLeakActivity`



## Experimental Modules
These modules are mostly for use experimenting with various use-cases.

They generally work, but are likely to be buggy and have various limitations and nuances.
Having said that, it would be nice to expand upon them and make them nicer/more feature reach in the future.

Some future possibilities:
- HttpD: Use ktor instead of Nano
- HttpD Simple Index: Provide a themed react page or something pretty/nice
- Add a Kotlin REPL module


### HttpD
Module [DevHttpD] adds a local HTTP server (uses [NanoHttpD](https://github.com/NanoHttpd/nanohttpd)).

Provides a single `POST` method `invoke` with one parameter `hashCode` (expecting [FunctionItem.hashCode])
```gradle
debugCompile 'com.nextfaze.devfun:httpd:0.2.0'
```

Use with HttpD Front-end.
_Current port and access instructions are logged on start._

Default port is `23075`.
If this is in use another is deterministically generated from your package (this might become the default).

If using AVD, forward port:
```bash
adb forward tcp:23075 tcp:23075
```

Then access via IP: [http://127.0.0.1:23075/](http://127.0.0.1:23075/)

#### Custom Port
Can be set via resources:
```xml
<integer name="df_httpd_default_port">12345</integer>
```

Or before initialization [devDefaultPort] (i.e. if not using auto-init content provider):
```kotlin
devDefaultPort = 12345 // top level value located in com.nextfaze.devfun.httpd
```


### Http Front-end
Module [HttpFrontEnd] generates an admin interface using SB Admin 2 (similar to [DevMenu]), allowing function invocation from a browser.

__Depends on [DevHttpD].__
```gradle
debugCompile 'com.nextfaze.devfun:httpd-frontend:0.2.0'
```

Page is extremely simple/static at the moment:
- Need to refresh each time to see changes to context-aware items.
- No item groups/titles.
- Zero styling/feedback for errors.


### Stetho
Module [DevStetho] allows generated methods to be invoked from Chrome's Dev Tools JavaScript console.
```gradle
debugCompile 'com.nextfaze.devfun:devfun-stetho:0.2.0'
```

Opening console will show available functions.
e.g. `Context_Enable_Account_Creation()`

_Extremely experimental and limited functionality._

 */
object Components

/**
 * Here to ensure the `.call` extension function stays in the import list (Kotlin IDE bug).
 */
private val dummy = (Any() as FunctionItem).call()
