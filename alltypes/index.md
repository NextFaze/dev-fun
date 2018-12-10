

Documentation is generated using Dokka.

### All Types

| Name | Summary |
|---|---|
| [com.nextfaze.devfun.internal.android.AbstractActivityLifecycleCallbacks](../com.nextfaze.devfun.internal.android/-abstract-activity-lifecycle-callbacks/index.md) |  |
| [com.nextfaze.devfun.core.AbstractDevFunModule](../com.nextfaze.devfun.core/-abstract-dev-fun-module/index.md) | Implementation of [DevFunModule](../com.nextfaze.devfun.core/-dev-fun-module/index.md) providing various convenience functions. |
| [com.nextfaze.devfun.httpd.AbstractUriHandler](../com.nextfaze.devfun.httpd/-abstract-uri-handler/index.md) |  |
| [android.app.Application.ActivityLifecycleCallbacks](../com.nextfaze.devfun.internal.android/android.app.-application.-activity-lifecycle-callbacks/index.md) (extensions in package com.nextfaze.devfun.internal.android) |  |
| [com.nextfaze.devfun.core.ActivityProvider](../com.nextfaze.devfun.core/-activity-provider.md) | Function signature of DevFun's activity tracker/provider. |
| [com.nextfaze.devfun.core.ActivityTracker](../com.nextfaze.devfun.core/-activity-tracker/index.md) | Activity tracker that provides the currently (resumed) activity if present. |
| [com.nextfaze.devfun.inject.AndroidInstanceProvider](../com.nextfaze.devfun.inject/-android-instance-provider/index.md) |  |
| [com.nextfaze.devfun.internal.android.AndroidInstanceProviderInternal](../com.nextfaze.devfun.internal.android/-android-instance-provider-internal/index.md) |  |
| [java.lang.Appendable](../com.nextfaze.devfun.internal.string/java.lang.-appendable/index.md) (extensions in package com.nextfaze.devfun.internal.string) |  |
| [com.nextfaze.devfun.function.Args](../com.nextfaze.devfun.function/-args/index.md) | Nested annotation for declaring the arguments of a function invocation. |
| [com.nextfaze.devfun.function.ArgsProperties](../com.nextfaze.devfun.function/-args-properties/index.md) | Properties interface for @[Args](../com.nextfaze.devfun.function/-args/index.md). |
| [com.nextfaze.devfun.function.ArgumentsTransformer](../com.nextfaze.devfun.function/-arguments-transformer.md) | A function transformer that tells DevFun how to generate functions for annotation-defined arguments. |
| [com.nextfaze.devfun.overlay.AttachListener](../com.nextfaze.devfun.overlay/-attach-listener.md) |  |
| [wiki.Background](../wiki/-background.md) | Background and History |
| [com.nextfaze.devfun.internal.android.BaseDialogFragment](../com.nextfaze.devfun.internal.android/-base-dialog-fragment/index.md) |  |
| [com.nextfaze.devfun.inject.CacheLevel](../com.nextfaze.devfun.inject/-cache-level/index.md) | Controls how aggressively the [CompositeInstanceProvider](../com.nextfaze.devfun.inject/-composite-instance-provider.md) caches the sources of class instances. |
| [com.nextfaze.devfun.inject.CapturingInstanceProvider](../com.nextfaze.devfun.inject/-capturing-instance-provider/index.md) | An instance provider that requests an instance of a class from a captured lambda. |
| [com.nextfaze.devfun.category.CategoryDefinition](../com.nextfaze.devfun.category/-category-definition/index.md) | Classes annotated with [DeveloperCategory](../com.nextfaze.devfun.category/-developer-category/index.md) will be defined using this interface at compile time. |
| [com.nextfaze.devfun.category.CategoryItem](../com.nextfaze.devfun.category/-category-item/index.md) | Items are derived from [CategoryDefinition](../com.nextfaze.devfun.category/-category-definition/index.md) at run-time during [FunctionDefinition](../com.nextfaze.devfun.function/-function-definition/index.md) processing. |
| [java.lang.Class](../com.nextfaze.devfun.internal.android/java.lang.-class/index.md) (extensions in package com.nextfaze.devfun.internal.android) |  |
| [com.nextfaze.devfun.inject.ClassInstanceNotFoundException](../com.nextfaze.devfun.inject/-class-instance-not-found-exception/index.md) | Exception thrown when attempting to provide a type that was not found from any [InstanceProvider](../com.nextfaze.devfun.inject/-instance-provider/index.md). |
| [com.nextfaze.devfun.overlay.ClickListener](../com.nextfaze.devfun.overlay/-click-listener.md) |  |
| [com.nextfaze.devfun.menu.controllers.CogOverlay](../com.nextfaze.devfun.menu.controllers/-cog-overlay/index.md) | Controls the floating cog overlay. |
| [kotlin.collections.Collection](../com.nextfaze.devfun.invoke.view.types/kotlin.collections.-collection/index.md) (extensions in package com.nextfaze.devfun.invoke.view.types) |  |
| [com.nextfaze.devfun.invoke.view.ColorPicker](../com.nextfaze.devfun.invoke.view/-color-picker/index.md) | Annotated `Int` value parameters will render a color picker view rather than an input/edit for use with invoke UI. |
| [wiki.Components](../wiki/-components.md) | DevFun is designed to be modular, in terms of both its dependencies (limiting impact to main source tree) and its plugin-like architecture. ![Component Dependencies](https://github.com/NextFaze/dev-fun/raw/gh-pages/assets/uml/components.png) |
| [com.nextfaze.devfun.core.Composite](../com.nextfaze.devfun.core/-composite/index.md) | Use by providers to facilitate user provided types [T](../com.nextfaze.devfun.core/-composite/index.md#T) to the composting provider. |
| [com.nextfaze.devfun.inject.CompositeInstanceProvider](../com.nextfaze.devfun.inject/-composite-instance-provider.md) | Instance provider that delegates to other providers. |
| [com.nextfaze.devfun.invoke.CompositeParameterViewFactoryProvider](../com.nextfaze.devfun.invoke/-composite-parameter-view-factory-provider.md) | A [ParameterViewFactoryProvider](../com.nextfaze.devfun.invoke/-parameter-view-factory-provider/index.md) that delegates to other providers. |
| [com.nextfaze.devfun.view.CompositeViewFactoryProvider](../com.nextfaze.devfun.view/-composite-view-factory-provider.md) | A [ViewFactoryProvider](../com.nextfaze.devfun.view/-view-factory-provider/index.md) that delegates to other providers. |
| [com.nextfaze.devfun.inject.Constructable](../com.nextfaze.devfun.inject/-constructable/index.md) | Tag to allow classes to be instantiated when no other [InstanceProvider](../com.nextfaze.devfun.inject/-instance-provider/index.md) was able to provide the class. |
| [com.nextfaze.devfun.inject.ConstructableException](../com.nextfaze.devfun.inject/-constructable-exception/index.md) | Thrown when [ConstructingInstanceProvider](../com.nextfaze.devfun.inject/-constructing-instance-provider/index.md) fails to create a new instance of a class. |
| [com.nextfaze.devfun.inject.ConstructingInstanceProvider](../com.nextfaze.devfun.inject/-constructing-instance-provider/index.md) | Provides objects via instance construction. Type must be annotated with [Constructable](../com.nextfaze.devfun.inject/-constructable/index.md). |
| [android.content.Context](../com.nextfaze.devfun.internal.android/android.content.-context/index.md) (extensions in package com.nextfaze.devfun.internal.android) |  |
| [com.nextfaze.devfun.category.ContextCategory](../com.nextfaze.devfun.category/-context-category/index.md) | [DeveloperCategory](../com.nextfaze.devfun.category/-developer-category/index.md) annotation used to declare the "Context" category. |
| [com.nextfaze.devfun.reference.Dagger2Component](../com.nextfaze.devfun.reference/-dagger2-component/index.md) | Annotated functions (`fun`, properties, or property getters (`@get:Dagger2Component`)) will be checked/used as Dagger 2 components. |
| [com.nextfaze.devfun.reference.Dagger2ComponentProperties](../com.nextfaze.devfun.reference/-dagger2-component-properties/index.md) | Properties interface for @[Dagger2Component](../com.nextfaze.devfun.reference/-dagger2-component/index.md). |
| [com.nextfaze.devfun.inject.dagger2.Dagger2InstanceProvider](../com.nextfaze.devfun.inject.dagger2/-dagger2-instance-provider/index.md) |  |
| [com.nextfaze.devfun.reference.Dagger2Scope](../com.nextfaze.devfun.reference/-dagger2-scope/index.md) | Some range of scopes for use with [Dagger2Component](../com.nextfaze.devfun.reference/-dagger2-component/index.md). Priority is based on their ordinal value (higher = broader scope). |
| [com.nextfaze.devfun.compiler.DaggerProcessor](../com.nextfaze.devfun.compiler/-dagger-processor/index.md) | Base [AbstractProcessor](http://docs.oracle.com/javase/6/docs/api/javax/annotation/processing/AbstractProcessor.html) class with Dagger support. |
| [com.nextfaze.devfun.DebugException](../com.nextfaze.devfun/-debug-exception/index.md) | This will not be caught by the standard DevFun error handler. |
| [wiki.Dependency Injection](../wiki/-dependency -injection.md) | DevFun supports a rudimentary form of dependency injection using an [InstanceProvider](../com.nextfaze.devfun.inject/-instance-provider/index.md) (a [CompositeInstanceProvider](../com.nextfaze.devfun.inject/-composite-instance-provider.md) at [DevFun.instanceProviders](../com.nextfaze.devfun.core/-dev-fun/instance-providers.md)). |
| [com.nextfaze.devfun.compiler.DevAnnotationProcessor](../com.nextfaze.devfun.compiler/-dev-annotation-processor/index.md) | Annotation processor for [DeveloperAnnotation](../com.nextfaze.devfun/-developer-annotation/index.md) to generate properties interfaces. |
| [com.nextfaze.devfun.core.DevFun](../com.nextfaze.devfun.core/-dev-fun/index.md) | Primary entry point and initializer of DevFun and associated libraries. |
| [com.nextfaze.devfun.gradle.plugin.DevFunExtension](../com.nextfaze.devfun.gradle.plugin/-dev-fun-extension/index.md) | Gradle DSL for configuring DevFun. |
| [com.nextfaze.devfun.generated.DevFunGenerated](../com.nextfaze.devfun.generated/-dev-fun-generated/index.md) | Generated classes will implement this, which will be loaded using Java's [ServiceLoader](https://developer.android.com/reference/java/util/ServiceLoader.html). |
| [com.nextfaze.devfun.gradle.plugin.DevFunGradlePlugin](../com.nextfaze.devfun.gradle.plugin/-dev-fun-gradle-plugin/index.md) | The DevFun Gradle plugin. Allows use of the script configuration DSL. |
| [com.nextfaze.devfun.core.DevFunInitializerProvider](../com.nextfaze.devfun.core/-dev-fun-initializer-provider/index.md) | Used to automatically initialize [DevFun](../com.nextfaze.devfun.core/-dev-fun/index.md) without user input. |
| [com.nextfaze.devfun.core.DevFunModule](../com.nextfaze.devfun.core/-dev-fun-module/index.md) | Modules that extend/use the functionality of [DevFun](../com.nextfaze.devfun.core/-dev-fun/index.md). |
| [com.nextfaze.devfun.compiler.DevFunProcessor](../com.nextfaze.devfun.compiler/-dev-fun-processor/index.md) | Annotation processor for [DeveloperAnnotation](../com.nextfaze.devfun/-developer-annotation/index.md) annotated annotations. |
| [com.nextfaze.devfun.httpd.DevHttpD](../com.nextfaze.devfun.httpd/-dev-http-d/index.md) |  |
| [com.nextfaze.devfun.menu.DevMenu](../com.nextfaze.devfun.menu/-dev-menu/index.md) |  |
| [com.nextfaze.devfun.stetho.DevStetho](../com.nextfaze.devfun.stetho/-dev-stetho/index.md) |  |
| [com.nextfaze.devfun.DeveloperAnnotation](../com.nextfaze.devfun/-developer-annotation/index.md) | Annotation used to by DevFun to "tag" references to other DevFun/developer -related annotations. |
| [com.nextfaze.devfun.function.DeveloperArguments](../com.nextfaze.devfun.function/-developer-arguments/index.md) | An alternative to [DeveloperFunction](../com.nextfaze.devfun.function/-developer-function/index.md) that allows you to provide arguments for multiple invocations. |
| [com.nextfaze.devfun.function.DeveloperArgumentsProperties](../com.nextfaze.devfun.function/-developer-arguments-properties/index.md) | Properties interface for @[DeveloperArguments](../com.nextfaze.devfun.function/-developer-arguments/index.md). |
| [com.nextfaze.devfun.category.DeveloperCategory](../com.nextfaze.devfun.category/-developer-category/index.md) | This annotation is optional, and is used to change the category's name/order or the group of the functions defined in this class. |
| [com.nextfaze.devfun.category.DeveloperCategoryProperties](../com.nextfaze.devfun.category/-developer-category-properties/index.md) | Properties interface for @[DeveloperCategory](../com.nextfaze.devfun.category/-developer-category/index.md). |
| [com.nextfaze.devfun.function.DeveloperFunction](../com.nextfaze.devfun.function/-developer-function/index.md) | Functions/methods annotated with this will be shown on the Developer Menu (and other modules). |
| [com.nextfaze.devfun.function.DeveloperFunctionProperties](../com.nextfaze.devfun.function/-developer-function-properties/index.md) | Properties interface for @[DeveloperFunction](../com.nextfaze.devfun.function/-developer-function/index.md). |
| [com.nextfaze.devfun.reference.DeveloperLogger](../com.nextfaze.devfun.reference/-developer-logger/index.md) | Annotated references will be rendered as an overlay. |
| [com.nextfaze.devfun.reference.DeveloperLoggerProperties](../com.nextfaze.devfun.reference/-developer-logger-properties/index.md) | Properties interface for @[DeveloperLogger](../com.nextfaze.devfun.reference/-developer-logger/index.md). |
| [com.nextfaze.devfun.menu.DeveloperMenu](../com.nextfaze.devfun.menu/-developer-menu/index.md) |  |
| [com.nextfaze.devfun.function.DeveloperProperty](../com.nextfaze.devfun.function/-developer-property/index.md) | An annotation that, when used on Kotlin properties, allows DevFun to provide the means of getting/setting properties on the fly. |
| [com.nextfaze.devfun.function.DeveloperPropertyProperties](../com.nextfaze.devfun.function/-developer-property-properties/index.md) | Properties interface for @[DeveloperProperty](../com.nextfaze.devfun.function/-developer-property/index.md). |
| [com.nextfaze.devfun.reference.DeveloperReference](../com.nextfaze.devfun.reference/-developer-reference/index.md) | Annotated elements will be recorded by DevFun for later retrieval via `devFun.developerReferences<DeveloperReference>()`. |
| [com.nextfaze.devfun.reference.DeveloperReferenceProperties](../com.nextfaze.devfun.reference/-developer-reference-properties.md) | Properties interface for @[DeveloperReference](../com.nextfaze.devfun.reference/-developer-reference/index.md). |
| [androidx.fragment.app.DialogFragment](../com.nextfaze.devfun.internal.android/androidx.fragment.app.-dialog-fragment/index.md) (extensions in package com.nextfaze.devfun.internal.android) |  |
| [com.nextfaze.devfun.overlay.Dock](../com.nextfaze.devfun.overlay/-dock/index.md) |  |
| [com.nextfaze.devfun.error.ErrorDetails](../com.nextfaze.devfun.error/-error-details/index.md) | Details/information of an error. |
| [com.nextfaze.devfun.error.ErrorHandler](../com.nextfaze.devfun.error/-error-handler/index.md) | Handles errors that occur during/throughout DevFun. |
| [com.nextfaze.devfun.invoke.view.simple.ErrorParameterView](../com.nextfaze.devfun.invoke.view.simple/-error-parameter-view.md) | A simple view that should tell the user if a view could not be injected/rendered. |
| [com.nextfaze.devfun.internal.exception.ExceptionCategoryDefinition](../com.nextfaze.devfun.internal.exception/-exception-category-definition/index.md) |  |
| [com.nextfaze.devfun.internal.exception.ExceptionCategoryItem](../com.nextfaze.devfun.internal.exception/-exception-category-item/index.md) |  |
| [com.nextfaze.devfun.internal.exception.ExceptionFunctionDefinition](../com.nextfaze.devfun.internal.exception/-exception-function-definition/index.md) |  |
| [com.nextfaze.devfun.internal.exception.ExceptionFunctionItem](../com.nextfaze.devfun.internal.exception/-exception-function-item/index.md) |  |
| [com.nextfaze.devfun.internal.exception.ExceptionInvokeResult](../com.nextfaze.devfun.internal.exception/-exception-invoke-result/index.md) |  |
| [com.nextfaze.devfun.reference.FieldReference](../com.nextfaze.devfun.reference/-field-reference/index.md) | A reference to a field generated by [DeveloperReference](../com.nextfaze.devfun.reference/-developer-reference/index.md). |
| [com.nextfaze.devfun.internal.FocusChangeListener](../com.nextfaze.devfun.internal/-focus-change-listener.md) | Function signature of a listener for [Window](https://developer.android.com/reference/android/view/Window.html) focus changes. |
| [com.nextfaze.devfun.core.ForegroundChangeListener](../com.nextfaze.devfun.core/-foreground-change-listener.md) | Function signature of callbacks for foreground status changes. |
| [com.nextfaze.devfun.core.ForegroundTracker](../com.nextfaze.devfun.core/-foreground-tracker/index.md) | Application foreground state tracker. |
| [androidx.fragment.app.Fragment](../com.nextfaze.devfun.internal.android/androidx.fragment.app.-fragment/index.md) (extensions in package com.nextfaze.devfun.internal.android) |  |
| [androidx.fragment.app.FragmentActivity](../com.nextfaze.devfun.internal.android/androidx.fragment.app.-fragment-activity/index.md) (extensions in package com.nextfaze.devfun.internal.android) |  |
| [androidx.fragment.app.FragmentManager](../com.nextfaze.devfun.internal.android/androidx.fragment.app.-fragment-manager/index.md) (extensions in package com.nextfaze.devfun.internal.android) |  |
| [com.nextfaze.devfun.invoke.view.From](../com.nextfaze.devfun.invoke.view/-from/index.md) | Annotate parameters with this specifying a [ValueSource](../com.nextfaze.devfun.invoke.view/-value-source/index.md) class to initialize invoke views with an initial value. |
| [com.nextfaze.devfun.function.FunctionArgs](../com.nextfaze.devfun.function/-function-args.md) | Definition for user-supplied arguments (usually supplied from a [FunctionTransformer](../com.nextfaze.devfun.function/-function-transformer/index.md)). |
| [com.nextfaze.devfun.function.FunctionDefinition](../com.nextfaze.devfun.function/-function-definition/index.md) | Functions/methods annotated with [DeveloperFunction](../com.nextfaze.devfun.function/-developer-function/index.md) will be defined using this interface at compile time. |
| [com.nextfaze.devfun.function.FunctionInvoke](../com.nextfaze.devfun.function/-function-invoke.md) | Definition of generated function to call that invokes the function definition. |
| [com.nextfaze.devfun.function.FunctionItem](../com.nextfaze.devfun.function/-function-item/index.md) | Items are converted from [FunctionDefinition](../com.nextfaze.devfun.function/-function-definition/index.md) at run-time via [FunctionTransformer](../com.nextfaze.devfun.function/-function-transformer/index.md). |
| [com.nextfaze.devfun.function.FunctionTransformer](../com.nextfaze.devfun.function/-function-transformer/index.md) | Function transformers filter and/or convert a [FunctionDefinition](../com.nextfaze.devfun.function/-function-definition/index.md) to [FunctionItem](../com.nextfaze.devfun.function/-function-item/index.md). |
| [com.nextfaze.devfun.utils.glide.GlideUtils](../com.nextfaze.devfun.utils.glide/-glide-utils/index.md) | Utility functions for [Glide](https://github.com/bumptech/glide). |
| [com.nextfaze.devfun.httpd.HttpDRouter](../com.nextfaze.devfun.httpd/-http-d-router/index.md) |  |
| [com.nextfaze.devfun.httpd.frontend.HttpFrontEnd](../com.nextfaze.devfun.httpd.frontend/-http-front-end/index.md) |  |
| [com.nextfaze.devfun.inject.dagger2.InjectFromDagger2](../com.nextfaze.devfun.inject.dagger2/-inject-from-dagger2/index.md) | This module adds rudimentary support for searching Dagger 2.x component graphs for object instances. |
| [com.nextfaze.devfun.invoke.view.simple.InjectedParameterView](../com.nextfaze.devfun.invoke.view.simple/-injected-parameter-view.md) | A simple view that should tell the user if a parameter is being injected. |
| [com.nextfaze.devfun.compiler.Injector](../com.nextfaze.devfun.compiler/-injector/index.md) | Dagger injector for [DaggerProcessor](../com.nextfaze.devfun.compiler/-dagger-processor/index.md). |
| [com.nextfaze.devfun.inject.InstanceProvider](../com.nextfaze.devfun.inject/-instance-provider/index.md) | Provides object instances for one or more types. |
| [com.nextfaze.devfun.invoke.view.InvokeParameterView](../com.nextfaze.devfun.invoke.view/-invoke-parameter-view/index.md) | Parameter views rendered for the Invoke UI will be wrapped with this type (to provide a label etc.). |
| [com.nextfaze.devfun.function.InvokeResult](../com.nextfaze.devfun.function/-invoke-result/index.md) | Function invocations will be wrapped by this. |
| [com.nextfaze.devfun.invoke.Invoker](../com.nextfaze.devfun.invoke/-invoker/index.md) | Used to invoke a [FunctionItem](../com.nextfaze.devfun.function/-function-item/index.md) or [UiFunction](../com.nextfaze.devfun.invoke/-ui-function/index.md) and automatically handles parameter injection and errors. |
| [kotlin.reflect.KClass](../com.nextfaze.devfun.inject/kotlin.reflect.-k-class/index.md) (extensions in package com.nextfaze.devfun.inject) |  |
| [kotlin.reflect.KClass](../com.nextfaze.devfun.internal.android/kotlin.reflect.-k-class/index.md) (extensions in package com.nextfaze.devfun.internal.android) |  |
| [kotlin.reflect.KClass](../com.nextfaze.devfun.internal/kotlin.reflect.-k-class/index.md) (extensions in package com.nextfaze.devfun.internal) |  |
| [com.nextfaze.devfun.internal.pref.KNullablePreference](../com.nextfaze.devfun.internal.pref/-k-nullable-preference.md) |  |
| [com.nextfaze.devfun.inject.KObjectInstanceProvider](../com.nextfaze.devfun.inject/-k-object-instance-provider/index.md) | Handles Kotlin `object` and `companion object` types. |
| [com.nextfaze.devfun.internal.pref.KPreference](../com.nextfaze.devfun.internal.pref/-k-preference/index.md) |  |
| [com.nextfaze.devfun.internal.pref.KSharedPreferences](../com.nextfaze.devfun.internal.pref/-k-shared-preferences/index.md) |  |
| [com.nextfaze.devfun.internal.KeyEventListener](../com.nextfaze.devfun.internal/-key-event-listener.md) | Function signature of a listener for [Window](https://developer.android.com/reference/android/view/Window.html) key events. |
| [com.nextfaze.devfun.menu.controllers.KeySequence](../com.nextfaze.devfun.menu.controllers/-key-sequence/index.md) | Allows toggling the Developer Menu using button/key sequences. |
| [com.nextfaze.devfun.gradle.plugin.KotlinGradlePlugin](../com.nextfaze.devfun.gradle.plugin/-kotlin-gradle-plugin/index.md) | The DevFun Kotlin Gradle plugin. Configures the KAPT options. |
| [com.nextfaze.devfun.utils.leakcanary.LeakCanaryUtils](../com.nextfaze.devfun.utils.leakcanary/-leak-canary-utils/index.md) | Utility functions for [Leak Canary](https://github.com/square/leakcanary). |
| [wiki.Lifecycle](../wiki/-lifecycle.md) | An overview of the lifecycle of the KAPT generation to runtime transformation and function invocation process. |
| [org.slf4j.Logger](../com.nextfaze.devfun.internal.log/org.slf4j.-logger/index.md) (extensions in package com.nextfaze.devfun.internal.log) |  |
| [java.lang.reflect.Member](../com.nextfaze.devfun.internal.reflect/java.lang.reflect.-member/index.md) (extensions in package com.nextfaze.devfun.internal.reflect) |  |
| [com.nextfaze.devfun.menu.MenuController](../com.nextfaze.devfun.menu/-menu-controller/index.md) |  |
| [com.nextfaze.devfun.menu.MenuHeader](../com.nextfaze.devfun.menu/-menu-header.md) | The view type/key used by DevMenu to find/inflate the menu header view. |
| [java.lang.reflect.Method](../com.nextfaze.devfun.internal/java.lang.reflect.-method/index.md) (extensions in package com.nextfaze.devfun.internal) |  |
| [java.lang.reflect.Method](../com.nextfaze.devfun.invoke/java.lang.reflect.-method/index.md) (extensions in package com.nextfaze.devfun.invoke) |  |
| [com.nextfaze.devfun.reference.MethodReference](../com.nextfaze.devfun.reference/-method-reference/index.md) | A reference to a method generated by [DeveloperReference](../com.nextfaze.devfun.reference/-developer-reference/index.md). |
| [com.nextfaze.devfun.internal.android.OnActivityCreated](../com.nextfaze.devfun.internal.android/-on-activity-created.md) |  |
| [com.nextfaze.devfun.internal.android.OnActivityDestroyed](../com.nextfaze.devfun.internal.android/-on-activity-destroyed.md) |  |
| [com.nextfaze.devfun.internal.android.OnActivityPaused](../com.nextfaze.devfun.internal.android/-on-activity-paused.md) |  |
| [com.nextfaze.devfun.internal.android.OnActivityResumed](../com.nextfaze.devfun.internal.android/-on-activity-resumed.md) |  |
| [com.nextfaze.devfun.internal.android.OnActivitySave](../com.nextfaze.devfun.internal.android/-on-activity-save.md) |  |
| [com.nextfaze.devfun.internal.android.OnActivityStarted](../com.nextfaze.devfun.internal.android/-on-activity-started.md) |  |
| [com.nextfaze.devfun.internal.android.OnActivityStopped](../com.nextfaze.devfun.internal.android/-on-activity-stopped.md) |  |
| [com.nextfaze.devfun.internal.pref.OnChange](../com.nextfaze.devfun.internal.pref/-on-change.md) |  |
| [com.nextfaze.devfun.invoke.OnClick](../com.nextfaze.devfun.invoke/-on-click.md) |  |
| [com.nextfaze.devfun.overlay.logger.OnClick](../com.nextfaze.devfun.overlay.logger/-on-click.md) |  |
| [com.nextfaze.devfun.core.OnInitialized](../com.nextfaze.devfun.core/-on-initialized.md) | Callback signature if/when [DevFun](../com.nextfaze.devfun.core/-dev-fun/index.md) has been initialized. |
| [com.nextfaze.devfun.overlay.logger.OverlayLogger](../com.nextfaze.devfun.overlay.logger/-overlay-logger/index.md) | An overlay logger is a floating semi-transparent `TextView` that will display the `.toString()` of a function, property, or class. |
| [com.nextfaze.devfun.overlay.logger.OverlayLogging](../com.nextfaze.devfun.overlay.logger/-overlay-logging/index.md) | Handles the creation, maintenance, and permissions of [OverlayLogger](../com.nextfaze.devfun.overlay.logger/-overlay-logger/index.md) instances. |
| [com.nextfaze.devfun.overlay.OverlayManager](../com.nextfaze.devfun.overlay/-overlay-manager/index.md) | Handles creation, destruction, and visibility of overlays. |
| [com.nextfaze.devfun.overlay.OverlayPermissionListener](../com.nextfaze.devfun.overlay/-overlay-permission-listener.md) |  |
| [com.nextfaze.devfun.overlay.OverlayPermissions](../com.nextfaze.devfun.overlay/-overlay-permissions/index.md) | Handles overlay permissions. |
| [com.nextfaze.devfun.overlay.OverlayReason](../com.nextfaze.devfun.overlay/-overlay-reason.md) |  |
| [com.nextfaze.devfun.overlay.OverlayWindow](../com.nextfaze.devfun.overlay/-overlay-window/index.md) | Overlay windows are used by DevFun to display the loggers [DeveloperLogger](../com.nextfaze.devfun.reference/-developer-logger/index.md) and DevMenu cog. |
| [com.nextfaze.devfun.stetho.Page](../com.nextfaze.devfun.stetho/-page/index.md) |  |
| [com.nextfaze.devfun.invoke.Parameter](../com.nextfaze.devfun.invoke/-parameter/index.md) | Effectively just a wrapper for [KParameter](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-parameter/index.html) to allow libraries to use it without declaring a dependency on the kotlin-reflect lib. |
| [com.nextfaze.devfun.invoke.ParameterViewFactoryProvider](../com.nextfaze.devfun.invoke/-parameter-view-factory-provider/index.md) | A factory that creates views based on parameter attributes to be used when invoking a function with non-injectable parameter types. |
| [com.nextfaze.devfun.function.PropertyTransformer](../com.nextfaze.devfun.function/-property-transformer.md) | A function transformer that tells DevFun how to render Kotlin properties. |
| [com.nextfaze.devfun.invoke.view.Ranged](../com.nextfaze.devfun.invoke.view/-ranged/index.md) | Used to restrict the range of a [Number](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-number/index.html) for user input. Using this will render a slider rather than a text view. |
| [com.nextfaze.devfun.reference.ReferenceDefinition](../com.nextfaze.devfun.reference/-reference-definition/index.md) | Defines references to annotations that are annotated [DeveloperReference](../com.nextfaze.devfun.reference/-developer-reference/index.md). |
| [com.nextfaze.devfun.internal.ReflectedMethod](../com.nextfaze.devfun.internal/-reflected-method/index.md) |  |
| [com.nextfaze.devfun.internal.ReflectedProperty](../com.nextfaze.devfun.internal/-reflected-property/index.md) |  |
| [wiki.Setup and Initialization](../wiki/-setup and -initialization.md) | Compiler configuration and initialization process. |
| [com.nextfaze.devfun.error.SimpleError](../com.nextfaze.devfun.error/-simple-error/index.md) | Convenience class that implements [ErrorDetails](../com.nextfaze.devfun.error/-error-details/index.md) and automatically time stamps it. |
| [com.nextfaze.devfun.function.SimpleFunctionItem](../com.nextfaze.devfun.function/-simple-function-item/index.md) | Convenience class for [FunctionItem](../com.nextfaze.devfun.function/-function-item/index.md) to extend from, providing standard [equals](../com.nextfaze.devfun.function/-simple-function-item/equals.md) and [hashCode](../com.nextfaze.devfun.function/-simple-function-item/hash-code.md) implementations. |
| [com.nextfaze.devfun.invoke.SimpleInvoke](../com.nextfaze.devfun.invoke/-simple-invoke.md) |  |
| [com.nextfaze.devfun.function.SingleFunctionTransformer](../com.nextfaze.devfun.function/-single-function-transformer/index.md) | The default transformer. Effectively just wraps the [FunctionDefinition](../com.nextfaze.devfun.function/-function-definition/index.md) to a [FunctionItem](../com.nextfaze.devfun.function/-function-item/index.md) (1:1). |
| [com.nextfaze.devfun.internal.string.Span](../com.nextfaze.devfun.internal.string/-span.md) |  |
| [android.text.SpannableStringBuilder](../com.nextfaze.devfun.internal.string/android.text.-spannable-string-builder/index.md) (extensions in package com.nextfaze.devfun.internal.string) |  |
| [kotlin.String](../com.nextfaze.devfun.internal/kotlin.-string/index.md) (extensions in package com.nextfaze.devfun.internal) |  |
| [com.nextfaze.devfun.internal.prop.ThreadLocalDelegate](../com.nextfaze.devfun.internal.prop/-thread-local-delegate/index.md) |  |
| [kotlin.Throwable](../com.nextfaze.devfun.internal.exception/kotlin.-throwable/index.md) (extensions in package com.nextfaze.devfun.internal.exception) |  |
| [com.nextfaze.devfun.inject.ThrowingInstanceProvider](../com.nextfaze.devfun.inject/-throwing-instance-provider/index.md) | Same as [InstanceProvider](../com.nextfaze.devfun.inject/-instance-provider/index.md), but throws [ClassInstanceNotFoundException](../com.nextfaze.devfun.inject/-class-instance-not-found-exception/index.md) instead of returning `null`. |
| [wiki.Transformers](../wiki/-transformers.md) | TODO |
| [com.nextfaze.devfun.reference.TypeReference](../com.nextfaze.devfun.reference/-type-reference/index.md) | A reference to a type generated by [DeveloperReference](../com.nextfaze.devfun.reference/-developer-reference/index.md). |
| [com.nextfaze.devfun.invoke.UiButton](../com.nextfaze.devfun.invoke/-ui-button/index.md) | Describes a dialog button for use with [UiFunction](../com.nextfaze.devfun.invoke/-ui-function/index.md) to be rendered via the DevFun Invocation UI. |
| [com.nextfaze.devfun.invoke.UiField](../com.nextfaze.devfun.invoke/-ui-field/index.md) | Utility interface to easily generate an invoke UI/dialog. *(experimental)* |
| [com.nextfaze.devfun.invoke.UiFunction](../com.nextfaze.devfun.invoke/-ui-function/index.md) | Describes a function to be executed via the DevFun Invocation UI. |
| [com.nextfaze.devfun.overlay.logger.UpdateCallback](../com.nextfaze.devfun.overlay.logger/-update-callback.md) |  |
| [com.nextfaze.devfun.invoke.view.ValueSource](../com.nextfaze.devfun.invoke.view/-value-source/index.md) | Used in conjunction with [From](../com.nextfaze.devfun.invoke.view/-from/index.md) or [Values](../com.nextfaze.devfun.invoke.view/-values/index.md) to provide the Invoke UI with an initial value or set of values. |
| [com.nextfaze.devfun.invoke.view.Values](../com.nextfaze.devfun.invoke.view/-values/index.md) | Annotate parameters with this specifying an [Iterable](../com.nextfaze.devfun.invoke.view/-value-source/index.md) class to initialize invoke views with a list of values. |
| [android.view.View](../com.nextfaze.devfun.internal.android/android.view.-view/index.md) (extensions in package com.nextfaze.devfun.internal.android) |  |
| [com.nextfaze.devfun.view.ViewFactory](../com.nextfaze.devfun.view/-view-factory/index.md) | Used by [DevFun](../com.nextfaze.devfun.core/-dev-fun/index.md) to inflate views when needed. |
| [com.nextfaze.devfun.view.ViewFactoryProvider](../com.nextfaze.devfun.view/-view-factory-provider/index.md) | Provides [ViewFactory](../com.nextfaze.devfun.view/-view-factory/index.md) instances for some class type/key. |
| [android.view.ViewGroup](../com.nextfaze.devfun.internal.android/android.view.-view-group/index.md) (extensions in package com.nextfaze.devfun.internal.android) |  |
| [com.nextfaze.devfun.overlay.VisibilityListener](../com.nextfaze.devfun.overlay/-visibility-listener.md) |  |
| [com.nextfaze.devfun.overlay.VisibilityPredicate](../com.nextfaze.devfun.overlay/-visibility-predicate.md) |  |
| [com.nextfaze.devfun.overlay.VisibilityScope](../com.nextfaze.devfun.overlay/-visibility-scope/index.md) | Determines when an overlay can be visible. |
| [com.nextfaze.devfun.internal.prop.WeakProperty](../com.nextfaze.devfun.internal.prop/-weak-property/index.md) |  |
| [com.nextfaze.devfun.internal.WindowCallbacks](../com.nextfaze.devfun.internal/-window-callbacks/index.md) | Handles wrapping and posting [Window](https://developer.android.com/reference/android/view/Window.html) events throughout an app's life. |
| [com.nextfaze.devfun.invoke.WithInitialValue](../com.nextfaze.devfun.invoke/-with-initial-value/index.md) | [Parameter](../com.nextfaze.devfun.invoke/-parameter/index.md) objects that implement this will use the value provided by [WithInitialValue.value](../com.nextfaze.devfun.invoke/-with-initial-value/value.md) rather than checking for an @[From](../com.nextfaze.devfun.invoke.view/-from/index.md) annotation. |
| [com.nextfaze.devfun.invoke.WithKParameter](../com.nextfaze.devfun.invoke/-with-k-parameter/index.md) | [Parameter](../com.nextfaze.devfun.invoke/-parameter/index.md) objects that implement this will have their values backed by a native function parameter description. |
| [com.nextfaze.devfun.invoke.view.WithLabel](../com.nextfaze.devfun.invoke.view/-with-label/index.md) | View used with the Invoke UI will automatically be wrapped and be given a label unless they provide their own with this interface. |
| [com.nextfaze.devfun.invoke.view.types.WithMask](../com.nextfaze.devfun.invoke.view.types/-with-mask/index.md) | This is an experimental feature. TODO? Could inline classes be a cleaner way of doing this given most of Android's flags are Int/Long values? |
| [com.nextfaze.devfun.invoke.WithNullability](../com.nextfaze.devfun.invoke/-with-nullability/index.md) | [Parameter](../com.nextfaze.devfun.invoke/-parameter/index.md) objects that implement this will can be nullable. |
| [com.nextfaze.devfun.reference.WithProperties](../com.nextfaze.devfun.reference/-with-properties/index.md) | Declares a definition that has [properties](../com.nextfaze.devfun.reference/-with-properties/properties.md) associated with it. The type will be `AnnotationNameProperties`. |
| [com.nextfaze.devfun.internal.WithSubGroup](../com.nextfaze.devfun.internal/-with-sub-group/index.md) | When implemented by a [FunctionItem](../com.nextfaze.devfun.function/-function-item/index.md) the DevMenu will render a sub-group for it. *Not explicitly intended for public use - primarilyhere as a "fix/workaround" for #19 (where Context overrides the user defined group)* |
| [com.nextfaze.devfun.invoke.view.WithValue](../com.nextfaze.devfun.invoke.view/-with-value/index.md) | Views used with the Invoke UI should implement this to ensure the correct value is obtainable upon invocation. |
