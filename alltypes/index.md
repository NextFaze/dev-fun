

Documentation is generated using Dokka.

### All Types

| Name | Summary |
|---|---|
| [com.nextfaze.devfun.internal.AbstractActivityLifecycleCallbacks](../com.nextfaze.devfun.internal/-abstract-activity-lifecycle-callbacks/index.md) |  |
| [com.nextfaze.devfun.core.AbstractDevFunModule](../com.nextfaze.devfun.core/-abstract-dev-fun-module/index.md) | Implementation of [DevFunModule](../com.nextfaze.devfun.core/-dev-fun-module/index.md) providing various convenience functions. |
| [com.nextfaze.devfun.httpd.AbstractUriHandler](../com.nextfaze.devfun.httpd/-abstract-uri-handler/index.md) |  |
| [android.app.Application.ActivityLifecycleCallbacks](../com.nextfaze.devfun.internal/android.app.-application.-activity-lifecycle-callbacks/index.md) (extensions in package com.nextfaze.devfun.internal) |  |
| [com.nextfaze.devfun.internal.ActivityProvider](../com.nextfaze.devfun.internal/-activity-provider.md) |  |
| [com.nextfaze.devfun.internal.ActivityTracker](../com.nextfaze.devfun.internal/-activity-tracker/index.md) |  |
| [wiki.Background](../wiki/-background.md) | Background and History |
| [com.nextfaze.devfun.inject.CapturingInstanceProvider](../com.nextfaze.devfun.inject/-capturing-instance-provider/index.md) | An instance provider that requests an instance of a class from a captured lambda. |
| [com.nextfaze.devfun.core.CategoryDefinition](../com.nextfaze.devfun.core/-category-definition/index.md) | Classes annotated with [DeveloperCategory](../com.nextfaze.devfun.annotations/-developer-category/index.md) will be defined using this interface at compile time. |
| [com.nextfaze.devfun.core.CategoryItem](../com.nextfaze.devfun.core/-category-item/index.md) | Items are derived from [CategoryDefinition](../com.nextfaze.devfun.core/-category-definition/index.md) at run-time during [FunctionDefinition](../com.nextfaze.devfun.core/-function-definition/index.md) processing. |
| [com.nextfaze.devfun.inject.ClassInstanceNotFoundException](../com.nextfaze.devfun.inject/-class-instance-not-found-exception/index.md) | Exception thrown when attempting to provide a type that was not found from any [InstanceProvider](../com.nextfaze.devfun.inject/-instance-provider/index.md). |
| [com.nextfaze.devfun.menu.controllers.CogOverlay](../com.nextfaze.devfun.menu.controllers/-cog-overlay/index.md) | Controls the floating cog overlay. |
| [wiki.Components](../wiki/-components.md) | DevFun is designed to be modular, in terms of both its dependencies (limiting impact to main source tree) and its plugin-like architecture. ![Component Dependencies](https://github.com/NextFaze/dev-fun/raw/gh-pages/assets/uml/components.png) |
| [com.nextfaze.devfun.inject.CompositeInstanceProvider](../com.nextfaze.devfun.inject/-composite-instance-provider/index.md) | Instance provider that delegates to other providers. |
| [com.nextfaze.devfun.inject.Constructable](../com.nextfaze.devfun.inject/-constructable/index.md) | Tag to allow classes to be instantiated when no other [InstanceProvider](../com.nextfaze.devfun.inject/-instance-provider/index.md) was able to provide the class. |
| [com.nextfaze.devfun.inject.ConstructingInstanceProvider](../com.nextfaze.devfun.inject/-constructing-instance-provider/index.md) | Provides objects via instance construction. Type must be annotated with [Constructable](../com.nextfaze.devfun.inject/-constructable/index.md). |
| [android.content.Context](../com.nextfaze.devfun.internal/android.content.-context/index.md) (extensions in package com.nextfaze.devfun.internal) |  |
| [com.nextfaze.devfun.core.DebugException](../com.nextfaze.devfun.core/-debug-exception/index.md) | This will not be caught by the generated [FunctionInvoke](../com.nextfaze.devfun.core/-function-invoke.md) call. |
| [wiki.Dependency Injection](../wiki/-dependency -injection.md) | DevFun supports a rudimentary form of dependency injection using an [InstanceProvider](../com.nextfaze.devfun.inject/-instance-provider/index.md) (a [CompositeInstanceProvider](../com.nextfaze.devfun.inject/-composite-instance-provider/index.md) at [DevFun.instanceProviders](../com.nextfaze.devfun.core/-dev-fun/instance-providers.md)). |
| [com.nextfaze.devfun.core.DevFun](../com.nextfaze.devfun.core/-dev-fun/index.md) | Primary entry point and initializer of DevFun and associated libraries. |
| [com.nextfaze.devfun.generated.DevFunGenerated](../com.nextfaze.devfun.generated/-dev-fun-generated/index.md) | Generated classes will implement this, which will be loaded using Java's [ServiceLoader](https://developer.android.com/reference/java/util/ServiceLoader.html). |
| [com.nextfaze.devfun.core.DevFunInitializerProvider](../com.nextfaze.devfun.core/-dev-fun-initializer-provider/index.md) | Used to automatically initialize [DevFun](../com.nextfaze.devfun.core/-dev-fun/index.md) without user input. |
| [com.nextfaze.devfun.core.DevFunModule](../com.nextfaze.devfun.core/-dev-fun-module/index.md) | Modules that extend/use the functionality of [DevFun](../com.nextfaze.devfun.core/-dev-fun/index.md). |
| [com.nextfaze.devfun.compiler.DevFunProcessor](../com.nextfaze.devfun.compiler/-dev-fun-processor/index.md) | Annotation processor for [DeveloperFunction](../com.nextfaze.devfun.annotations/-developer-function/index.md) and [DeveloperCategory](../com.nextfaze.devfun.annotations/-developer-category/index.md). |
| [com.nextfaze.devfun.httpd.DevHttpD](../com.nextfaze.devfun.httpd/-dev-http-d/index.md) |  |
| [com.nextfaze.devfun.menu.DevMenu](../com.nextfaze.devfun.menu/-dev-menu/index.md) |  |
| [com.nextfaze.devfun.stetho.DevStetho](../com.nextfaze.devfun.stetho/-dev-stetho/index.md) |  |
| [com.nextfaze.devfun.annotations.DeveloperCategory](../com.nextfaze.devfun.annotations/-developer-category/index.md) | This annotation is optional, and is used to change the category's name/order or the group of the functions defined in this class. |
| [com.nextfaze.devfun.annotations.DeveloperFunction](../com.nextfaze.devfun.annotations/-developer-function/index.md) | Functions/methods annotated with this will be shown on the Developer Menu (and other modules). |
| [com.nextfaze.devfun.menu.DeveloperMenu](../com.nextfaze.devfun.menu/-developer-menu/index.md) |  |
| [com.nextfaze.devfun.core.FunctionArgs](../com.nextfaze.devfun.core/-function-args.md) |  |
| [com.nextfaze.devfun.core.FunctionDefinition](../com.nextfaze.devfun.core/-function-definition/index.md) | Functions/methods annotated with [DeveloperFunction](../com.nextfaze.devfun.annotations/-developer-function/index.md) will be defined using this interface at compile time. |
| [com.nextfaze.devfun.core.FunctionInvoke](../com.nextfaze.devfun.core/-function-invoke.md) |  |
| [com.nextfaze.devfun.core.FunctionItem](../com.nextfaze.devfun.core/-function-item/index.md) | Items are converted from [FunctionDefinition](../com.nextfaze.devfun.core/-function-definition/index.md) at run-time via [FunctionTransformer](../com.nextfaze.devfun.core/-function-transformer/index.md). |
| [com.nextfaze.devfun.core.FunctionTransformer](../com.nextfaze.devfun.core/-function-transformer/index.md) | Function transformers filter and/or convert a [FunctionDefinition](../com.nextfaze.devfun.core/-function-definition/index.md) to [FunctionItem](../com.nextfaze.devfun.core/-function-item/index.md). |
| [com.nextfaze.devfun.utils.glide.GlideUtils](../com.nextfaze.devfun.utils.glide/-glide-utils/index.md) | Utility functions for [Glide](https://github.com/bumptech/glide). |
| [com.nextfaze.devfun.httpd.HttpDRouter](../com.nextfaze.devfun.httpd/-http-d-router/index.md) |  |
| [com.nextfaze.devfun.httpd.frontend.HttpFrontEnd](../com.nextfaze.devfun.httpd.frontend/-http-front-end/index.md) |  |
| [com.nextfaze.devfun.inject.dagger2.InjectFromDagger2](../com.nextfaze.devfun.inject.dagger2/-inject-from-dagger2/index.md) | This module adds rudimentary support for searching Dagger 2.x component graphs for object instances. |
| [com.nextfaze.devfun.inject.InstanceProvider](../com.nextfaze.devfun.inject/-instance-provider/index.md) | Provides object instances for one or more types. |
| [com.nextfaze.devfun.core.InvokeResult](../com.nextfaze.devfun.core/-invoke-result/index.md) | Function invocations will be wrapped by this. |
| [kotlin.reflect.KClass](../com.nextfaze.devfun.internal/kotlin.reflect.-k-class/index.md) (extensions in package com.nextfaze.devfun.internal) |  |
| [com.nextfaze.devfun.inject.KObjectInstanceProvider](../com.nextfaze.devfun.inject/-k-object-instance-provider/index.md) | Handles Kotlin `object` types. |
| [com.nextfaze.devfun.menu.controllers.KeySequence](../com.nextfaze.devfun.menu.controllers/-key-sequence/index.md) |  |
| [com.nextfaze.devfun.utils.leakcanary.LeakCanaryUtils](../com.nextfaze.devfun.utils.leakcanary/-leak-canary-utils/index.md) | Utility functions for [Leak Canary](https://github.com/square/leakcanary). |
| [wiki.Lifecycle](../wiki/-lifecycle.md) | An overview of the lifecycle of the KAPT generation to runtime transformation and function invocation process. |
| [org.slf4j.Logger](../com.nextfaze.devfun.internal/org.slf4j.-logger/index.md) (extensions in package com.nextfaze.devfun.internal) |  |
| [com.nextfaze.devfun.menu.MenuController](../com.nextfaze.devfun.menu/-menu-controller/index.md) |  |
| [com.nextfaze.devfun.core.OnInitialized](../com.nextfaze.devfun.core/-on-initialized.md) | Callback signature if/when [DevFun](../com.nextfaze.devfun.core/-dev-fun/index.md) has been initialized. |
| [com.nextfaze.devfun.stetho.Page](../com.nextfaze.devfun.stetho/-page/index.md) |  |
| [com.nextfaze.devfun.inject.RequiringInstanceProvider](../com.nextfaze.devfun.inject/-requiring-instance-provider/index.md) | Same as [InstanceProvider](../com.nextfaze.devfun.inject/-instance-provider/index.md), but throws [ClassInstanceNotFoundException](../com.nextfaze.devfun.inject/-class-instance-not-found-exception/index.md) instead of returning `null`. |
| [wiki.Setup and Initialization](../wiki/-setup and -initialization.md) | Compiler configuration and initialization process. |
| [com.nextfaze.devfun.core.SimpleFunctionItem](../com.nextfaze.devfun.core/-simple-function-item/index.md) | Convenience class for [FunctionItem](../com.nextfaze.devfun.core/-function-item/index.md) to extend from, providing standard [equals](../com.nextfaze.devfun.core/-simple-function-item/equals.md) and [hashCode](../com.nextfaze.devfun.core/-simple-function-item/hash-code.md) implementations. |
| [com.nextfaze.devfun.core.SingleFunctionTransformer](../com.nextfaze.devfun.core/-single-function-transformer/index.md) | The default transformer. Effectively just wraps the [FunctionDefinition](../com.nextfaze.devfun.core/-function-definition/index.md) to a [FunctionItem](../com.nextfaze.devfun.core/-function-item/index.md) (1:1). |
| [kotlin.String](../com.nextfaze.devfun.internal/kotlin.-string/index.md) (extensions in package com.nextfaze.devfun.internal) |  |
| [wiki.Transformers](../wiki/-transformers.md) | TODO |
