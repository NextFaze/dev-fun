

Documentation is generated using Dokka.

### All Types

| Name | Summary |
|---|---|
| [com.nextfaze.devfun.internal.AbstractActivityLifecycleCallbacks](../com.nextfaze.devfun.internal/-abstract-activity-lifecycle-callbacks/index.md) |  |
| [com.nextfaze.devfun.core.AbstractDevFunModule](../com.nextfaze.devfun.core/-abstract-dev-fun-module/index.md) | Implementation of [DevFunModule](../com.nextfaze.devfun.core/-dev-fun-module/index.md) providing various convenience functions. |
| [com.nextfaze.devfun.httpd.AbstractUriHandler](../com.nextfaze.devfun.httpd/-abstract-uri-handler/index.md) |  |
| [android.app.Application.ActivityLifecycleCallbacks](../com.nextfaze.devfun.internal/android.app.-application.-activity-lifecycle-callbacks/index.md) (extensions in package com.nextfaze.devfun.internal) |  |
| [com.nextfaze.devfun.core.ActivityProvider](../com.nextfaze.devfun.core/-activity-provider.md) | Function signature of DevFun's activity tracker/provider. |
| [java.lang.Appendable](../com.nextfaze.devfun.internal/java.lang.-appendable/index.md) (extensions in package com.nextfaze.devfun.internal) |  |
| [wiki.Background](../wiki/-background.md) | Background and History |
| [com.nextfaze.devfun.inject.CapturingInstanceProvider](../com.nextfaze.devfun.inject/-capturing-instance-provider/index.md) | An instance provider that requests an instance of a class from a captured lambda. |
| [com.nextfaze.devfun.core.CategoryDefinition](../com.nextfaze.devfun.core/-category-definition/index.md) | Classes annotated with [DeveloperCategory](../com.nextfaze.devfun.annotations/-developer-category/index.md) will be defined using this interface at compile time. |
| [com.nextfaze.devfun.core.CategoryItem](../com.nextfaze.devfun.core/-category-item/index.md) | Items are derived from [CategoryDefinition](../com.nextfaze.devfun.core/-category-definition/index.md) at run-time during [FunctionDefinition](../com.nextfaze.devfun.core/-function-definition/index.md) processing. |
| [com.nextfaze.devfun.inject.ClassInstanceNotFoundException](../com.nextfaze.devfun.inject/-class-instance-not-found-exception/index.md) | Exception thrown when attempting to provide a type that was not found from any [InstanceProvider](../com.nextfaze.devfun.inject/-instance-provider/index.md). |
| [com.nextfaze.devfun.menu.controllers.CogOverlay](../com.nextfaze.devfun.menu.controllers/-cog-overlay/index.md) | Controls the floating cog overlay. |
| [com.nextfaze.devfun.invoke.view.ColorPicker](../com.nextfaze.devfun.invoke.view/-color-picker/index.md) | Annotated `Int` value parameters will render a color picker view rather than an input/edit for use with invoke UI. |
| [wiki.Components](../wiki/-components.md) | DevFun is designed to be modular, in terms of both its dependencies (limiting impact to main source tree) and its plugin-like architecture. ![Component Dependencies](https://github.com/NextFaze/dev-fun/raw/gh-pages/assets/uml/components.png) |
| [com.nextfaze.devfun.core.Composite](../com.nextfaze.devfun.core/-composite/index.md) | Use by providers to facilitate user provided types [T](../com.nextfaze.devfun.core/-composite/index.md#T) to the composting provider. |
| [com.nextfaze.devfun.inject.CompositeInstanceProvider](../com.nextfaze.devfun.inject/-composite-instance-provider.md) | Instance provider that delegates to other providers. |
| [com.nextfaze.devfun.invoke.CompositeParameterViewFactoryProvider](../com.nextfaze.devfun.invoke/-composite-parameter-view-factory-provider.md) | A [ParameterViewFactoryProvider](../com.nextfaze.devfun.invoke/-parameter-view-factory-provider/index.md) that delegates to other providers. |
| [com.nextfaze.devfun.view.CompositeViewFactoryProvider](../com.nextfaze.devfun.view/-composite-view-factory-provider.md) | A [ViewFactoryProvider](../com.nextfaze.devfun.view/-view-factory-provider/index.md) that delegates to other providers. |
| [com.nextfaze.devfun.inject.Constructable](../com.nextfaze.devfun.inject/-constructable/index.md) | Tag to allow classes to be instantiated when no other [InstanceProvider](../com.nextfaze.devfun.inject/-instance-provider/index.md) was able to provide the class. |
| [com.nextfaze.devfun.inject.ConstructingInstanceProvider](../com.nextfaze.devfun.inject/-constructing-instance-provider/index.md) | Provides objects via instance construction. Type must be annotated with [Constructable](../com.nextfaze.devfun.inject/-constructable/index.md). |
| [android.content.Context](../com.nextfaze.devfun.internal/android.content.-context/index.md) (extensions in package com.nextfaze.devfun.internal) |  |
| [com.nextfaze.devfun.annotations.Dagger2Component](../com.nextfaze.devfun.annotations/-dagger2-component/index.md) | Annotated functions (`fun` or property getters with `@get:Dagger2Component`) will be checked/used as Dagger 2 components. |
| [com.nextfaze.devfun.annotations.Dagger2Scope](../com.nextfaze.devfun.annotations/-dagger2-scope/index.md) | Some range of scopes for use with [Dagger2Component](../com.nextfaze.devfun.annotations/-dagger2-component/index.md). Priority is based on their ordinal value (higher = broader scope). |
| [com.nextfaze.devfun.core.DebugException](../com.nextfaze.devfun.core/-debug-exception/index.md) | This will not be caught by the standard DevFun Invoker. |
| [wiki.Dependency Injection](../wiki/-dependency -injection.md) | DevFun supports a rudimentary form of dependency injection using an [InstanceProvider](../com.nextfaze.devfun.inject/-instance-provider/index.md) (a [CompositeInstanceProvider](../com.nextfaze.devfun.inject/-composite-instance-provider.md) at [DevFun.instanceProviders](../com.nextfaze.devfun.core/-dev-fun/instance-providers.md)). |
| [com.nextfaze.devfun.core.DevFun](../com.nextfaze.devfun.core/-dev-fun/index.md) | Primary entry point and initializer of DevFun and associated libraries. |
| [com.nextfaze.devfun.gradle.plugin.DevFunExtension](../com.nextfaze.devfun.gradle.plugin/-dev-fun-extension/index.md) | Gradle DSL for configuring DevFun. |
| [com.nextfaze.devfun.generated.DevFunGenerated](../com.nextfaze.devfun.generated/-dev-fun-generated/index.md) | Generated classes will implement this, which will be loaded using Java's [ServiceLoader](https://developer.android.com/reference/java/util/ServiceLoader.html). |
| [com.nextfaze.devfun.gradle.plugin.DevFunGradlePlugin](../com.nextfaze.devfun.gradle.plugin/-dev-fun-gradle-plugin/index.md) | The DevFun Gradle plugin. Allows use of the script configuration DSL. |
| [com.nextfaze.devfun.core.DevFunInitializerProvider](../com.nextfaze.devfun.core/-dev-fun-initializer-provider/index.md) | Used to automatically initialize [DevFun](../com.nextfaze.devfun.core/-dev-fun/index.md) without user input. |
| [com.nextfaze.devfun.gradle.plugin.DevFunKotlinGradlePlugin](../com.nextfaze.devfun.gradle.plugin/-dev-fun-kotlin-gradle-plugin/index.md) | The DevFun Kotlin Gradle plugin. Configures the KAPT options. |
| [com.nextfaze.devfun.core.DevFunModule](../com.nextfaze.devfun.core/-dev-fun-module/index.md) | Modules that extend/use the functionality of [DevFun](../com.nextfaze.devfun.core/-dev-fun/index.md). |
| [com.nextfaze.devfun.compiler.DevFunProcessor](../com.nextfaze.devfun.compiler/-dev-fun-processor/index.md) | Annotation processor for [DeveloperFunction](../com.nextfaze.devfun.annotations/-developer-function/index.md) and [DeveloperCategory](../com.nextfaze.devfun.annotations/-developer-category/index.md). |
| [com.nextfaze.devfun.httpd.DevHttpD](../com.nextfaze.devfun.httpd/-dev-http-d/index.md) |  |
| [com.nextfaze.devfun.menu.DevMenu](../com.nextfaze.devfun.menu/-dev-menu/index.md) |  |
| [com.nextfaze.devfun.stetho.DevStetho](../com.nextfaze.devfun.stetho/-dev-stetho/index.md) |  |
| [com.nextfaze.devfun.annotations.DeveloperAnnotation](../com.nextfaze.devfun.annotations/-developer-annotation/index.md) | Meta annotation used to by DevFun to "tag" references to some other annotations. |
| [com.nextfaze.devfun.annotations.DeveloperCategory](../com.nextfaze.devfun.annotations/-developer-category/index.md) | This annotation is optional, and is used to change the category's name/order or the group of the functions defined in this class. |
| [com.nextfaze.devfun.annotations.DeveloperFunction](../com.nextfaze.devfun.annotations/-developer-function/index.md) | Functions/methods annotated with this will be shown on the Developer Menu (and other modules). |
| [com.nextfaze.devfun.menu.DeveloperMenu](../com.nextfaze.devfun.menu/-developer-menu/index.md) |  |
| [com.nextfaze.devfun.core.DeveloperReference](../com.nextfaze.devfun.core/-developer-reference/index.md) | Defines references to annotations that are annotated by meta annotation [DeveloperAnnotation](../com.nextfaze.devfun.annotations/-developer-annotation/index.md). |
| [com.nextfaze.devfun.error.ErrorDetails](../com.nextfaze.devfun.error/-error-details/index.md) | Details/information of an error. |
| [com.nextfaze.devfun.error.ErrorHandler](../com.nextfaze.devfun.error/-error-handler/index.md) | Handles errors that occur during/throughout DevFun. |
| [com.nextfaze.devfun.invoke.view.simple.ErrorParameterView](../com.nextfaze.devfun.invoke.view.simple/-error-parameter-view.md) | A simple view that should tell the user if a view could not be injected/rendered. |
| [com.nextfaze.devfun.invoke.view.From](../com.nextfaze.devfun.invoke.view/-from/index.md) | Annotate parameters with this specifying a [ValueSource](../com.nextfaze.devfun.invoke.view/-value-source/index.md) class to initialize invoke views with an initial value. |
| [com.nextfaze.devfun.core.FunctionArgs](../com.nextfaze.devfun.core/-function-args.md) | Definition for user-supplied arguments (usually supplied from a [FunctionTransformer](../com.nextfaze.devfun.core/-function-transformer/index.md)). |
| [com.nextfaze.devfun.core.FunctionDefinition](../com.nextfaze.devfun.core/-function-definition/index.md) | Functions/methods annotated with [DeveloperFunction](../com.nextfaze.devfun.annotations/-developer-function/index.md) will be defined using this interface at compile time. |
| [com.nextfaze.devfun.core.FunctionInvoke](../com.nextfaze.devfun.core/-function-invoke.md) | Definition of generated function to call that invokes the function definition. |
| [com.nextfaze.devfun.core.FunctionItem](../com.nextfaze.devfun.core/-function-item/index.md) | Items are converted from [FunctionDefinition](../com.nextfaze.devfun.core/-function-definition/index.md) at run-time via [FunctionTransformer](../com.nextfaze.devfun.core/-function-transformer/index.md). |
| [com.nextfaze.devfun.core.FunctionTransformer](../com.nextfaze.devfun.core/-function-transformer/index.md) | Function transformers filter and/or convert a [FunctionDefinition](../com.nextfaze.devfun.core/-function-definition/index.md) to [FunctionItem](../com.nextfaze.devfun.core/-function-item/index.md). |
| [com.nextfaze.devfun.utils.glide.GlideUtils](../com.nextfaze.devfun.utils.glide/-glide-utils/index.md) | Utility functions for [Glide](https://github.com/bumptech/glide). |
| [com.nextfaze.devfun.httpd.HttpDRouter](../com.nextfaze.devfun.httpd/-http-d-router/index.md) |  |
| [com.nextfaze.devfun.httpd.frontend.HttpFrontEnd](../com.nextfaze.devfun.httpd.frontend/-http-front-end/index.md) |  |
| [com.nextfaze.devfun.inject.dagger2.InjectFromDagger2](../com.nextfaze.devfun.inject.dagger2/-inject-from-dagger2/index.md) | This module adds rudimentary support for searching Dagger 2.x component graphs for object instances. |
| [com.nextfaze.devfun.invoke.view.simple.InjectedParameterView](../com.nextfaze.devfun.invoke.view.simple/-injected-parameter-view.md) | A simple view that should tell the user if a parameter is being injected. |
| [com.nextfaze.devfun.inject.InstanceProvider](../com.nextfaze.devfun.inject/-instance-provider/index.md) | Provides object instances for one or more types. |
| [com.nextfaze.devfun.invoke.view.InvokeParameterView](../com.nextfaze.devfun.invoke.view/-invoke-parameter-view/index.md) | Parameter views rendered for the Invoke UI will be wrapped with this type (to provide a label etc.). |
| [com.nextfaze.devfun.core.InvokeResult](../com.nextfaze.devfun.core/-invoke-result/index.md) | Function invocations will be wrapped by this. |
| [com.nextfaze.devfun.invoke.Invoker](../com.nextfaze.devfun.invoke/-invoker/index.md) | Used to invoke a [FunctionItem](../com.nextfaze.devfun.core/-function-item/index.md) and automatically handles parameter injection and errors. |
| [kotlin.reflect.KClass](../com.nextfaze.devfun.internal/kotlin.reflect.-k-class/index.md) (extensions in package com.nextfaze.devfun.internal) |  |
| [com.nextfaze.devfun.inject.KObjectInstanceProvider](../com.nextfaze.devfun.inject/-k-object-instance-provider/index.md) | Handles Kotlin `object` types. |
| [com.nextfaze.devfun.menu.controllers.KeySequence](../com.nextfaze.devfun.menu.controllers/-key-sequence/index.md) | Allows toggling the Developer Menu using button/key sequences. |
| [com.nextfaze.devfun.utils.leakcanary.LeakCanaryUtils](../com.nextfaze.devfun.utils.leakcanary/-leak-canary-utils/index.md) | Utility functions for [Leak Canary](https://github.com/square/leakcanary). |
| [wiki.Lifecycle](../wiki/-lifecycle.md) | An overview of the lifecycle of the KAPT generation to runtime transformation and function invocation process. |
| [org.slf4j.Logger](../com.nextfaze.devfun.internal/org.slf4j.-logger/index.md) (extensions in package com.nextfaze.devfun.internal) |  |
| [java.lang.reflect.Member](../com.nextfaze.devfun.internal/java.lang.reflect.-member/index.md) (extensions in package com.nextfaze.devfun.internal) |  |
| [com.nextfaze.devfun.menu.MenuController](../com.nextfaze.devfun.menu/-menu-controller/index.md) |  |
| [com.nextfaze.devfun.menu.MenuHeader](../com.nextfaze.devfun.menu/-menu-header.md) | The view type/key used by DevMenu to find/inflate the menu header view. |
| [java.lang.reflect.Method](../com.nextfaze.devfun.invoke/java.lang.reflect.-method/index.md) (extensions in package com.nextfaze.devfun.invoke) |  |
| [com.nextfaze.devfun.internal.OnActivityCreated](../com.nextfaze.devfun.internal/-on-activity-created.md) |  |
| [com.nextfaze.devfun.internal.OnActivityDestroyed](../com.nextfaze.devfun.internal/-on-activity-destroyed.md) |  |
| [com.nextfaze.devfun.internal.OnActivityPaused](../com.nextfaze.devfun.internal/-on-activity-paused.md) |  |
| [com.nextfaze.devfun.internal.OnActivityResumed](../com.nextfaze.devfun.internal/-on-activity-resumed.md) |  |
| [com.nextfaze.devfun.internal.OnActivitySave](../com.nextfaze.devfun.internal/-on-activity-save.md) |  |
| [com.nextfaze.devfun.internal.OnActivityStarted](../com.nextfaze.devfun.internal/-on-activity-started.md) |  |
| [com.nextfaze.devfun.internal.OnActivityStopped](../com.nextfaze.devfun.internal/-on-activity-stopped.md) |  |
| [com.nextfaze.devfun.core.OnInitialized](../com.nextfaze.devfun.core/-on-initialized.md) | Callback signature if/when [DevFun](../com.nextfaze.devfun.core/-dev-fun/index.md) has been initialized. |
| [com.nextfaze.devfun.stetho.Page](../com.nextfaze.devfun.stetho/-page/index.md) |  |
| [com.nextfaze.devfun.invoke.Parameter](../com.nextfaze.devfun.invoke/-parameter/index.md) | Effectively just a wrapper for [KParameter](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-parameter/index.html) to allow libraries to use it without declaring a dependency on the kotlin-reflect lib. |
| [com.nextfaze.devfun.invoke.ParameterViewFactoryProvider](../com.nextfaze.devfun.invoke/-parameter-view-factory-provider/index.md) | A factory that creates views based on parameter attributes to be used when invoking a function with non-injectable parameter types. |
| [com.nextfaze.devfun.invoke.view.Ranged](../com.nextfaze.devfun.invoke.view/-ranged/index.md) | Used to restrict the range of a [Number](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-number/index.html) for user input. Using this will render a slider rather than a text view. |
| [com.nextfaze.devfun.inject.RequiringInstanceProvider](../com.nextfaze.devfun.inject/-requiring-instance-provider/index.md) | Same as [InstanceProvider](../com.nextfaze.devfun.inject/-instance-provider/index.md), but throws [ClassInstanceNotFoundException](../com.nextfaze.devfun.inject/-class-instance-not-found-exception/index.md) instead of returning `null`. |
| [wiki.Setup and Initialization](../wiki/-setup and -initialization.md) | Compiler configuration and initialization process. |
| [com.nextfaze.devfun.error.SimpleError](../com.nextfaze.devfun.error/-simple-error/index.md) | Convenience class that implements [ErrorDetails](../com.nextfaze.devfun.error/-error-details/index.md) and automatically time stamps it. |
| [com.nextfaze.devfun.core.SimpleFunctionItem](../com.nextfaze.devfun.core/-simple-function-item/index.md) | Convenience class for [FunctionItem](../com.nextfaze.devfun.core/-function-item/index.md) to extend from, providing standard [equals](../com.nextfaze.devfun.core/-simple-function-item/equals.md) and [hashCode](../com.nextfaze.devfun.core/-simple-function-item/hash-code.md) implementations. |
| [com.nextfaze.devfun.core.SingleFunctionTransformer](../com.nextfaze.devfun.core/-single-function-transformer/index.md) | The default transformer. Effectively just wraps the [FunctionDefinition](../com.nextfaze.devfun.core/-function-definition/index.md) to a [FunctionItem](../com.nextfaze.devfun.core/-function-item/index.md) (1:1). |
| [android.text.SpannableStringBuilder](../com.nextfaze.devfun.internal/android.text.-spannable-string-builder/index.md) (extensions in package com.nextfaze.devfun.internal) |  |
| [kotlin.String](../com.nextfaze.devfun.internal/kotlin.-string/index.md) (extensions in package com.nextfaze.devfun.internal) |  |
| [wiki.Transformers](../wiki/-transformers.md) | TODO |
| [com.nextfaze.devfun.invoke.view.ValueSource](../com.nextfaze.devfun.invoke.view/-value-source/index.md) | Used in conjunction with [From](../com.nextfaze.devfun.invoke.view/-from/index.md) to allow the Invoke UI to provide an initial value. |
| [com.nextfaze.devfun.view.ViewFactory](../com.nextfaze.devfun.view/-view-factory/index.md) | Used by [DevFun](../com.nextfaze.devfun.core/-dev-fun/index.md) to inflate views when needed. |
| [com.nextfaze.devfun.view.ViewFactoryProvider](../com.nextfaze.devfun.view/-view-factory-provider/index.md) | Provides [ViewFactory](../com.nextfaze.devfun.view/-view-factory/index.md) instances for some class type/key. |
| [com.nextfaze.devfun.invoke.view.WithLabel](../com.nextfaze.devfun.invoke.view/-with-label/index.md) | View used with the Invoke UI will automatically be wrapped and be given a label unless they provide their own with this interface. |
| [com.nextfaze.devfun.invoke.view.WithValue](../com.nextfaze.devfun.invoke.view/-with-value/index.md) | Views used with the Invoke UI should implement this to ensure the correct value is obtainable upon invocation. |
