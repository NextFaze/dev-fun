# DevFun - Developer (Fun)ctions
<img src="https://github.com/NextFaze/dev-fun/raw/gh-pages/assets/gif/registration-flow.gif" align="right">
A developer targeted library to facilitate the development and testing of Android apps by enabling the invocation of arbitrary functions and code using tagging annotations.

By tagging (annotating) functions of interest, DevFun provides direct access to these functions at run-time through a
number of means - such as an automatically generated "Developer Menu".

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->


  - [Quick Start](#quick-start)
      - [Project Setup](#project-setup)
      - [Build File `build.gradle`](#build-file-buildgradle)
      - [Modules](#modules)
  - [Key Features](#key-features)
      - [Zero-configuration Setup](#zero-configuration-setup)
      - [Context Aware](#context-aware)
      - [Invocation UI](#invocation-ui)
      - [Modular](#modular)
      - [And More](#and-more)
  - [Showcase](#showcase)
      - [Developer Menu](#developer-menu)
      - [Invocation UI](#invocation-ui-1)
      - [Overlay Loggers](#overlay-loggers)
      - [Local HTTP Server (experimental)](#local-http-server-experimental)
      - [Stetho Integration (experimental)](#stetho-integration-experimental)
  - [Annotations Overview and Custom Annotations/Properties](#annotations-overview-and-custom-annotationsproperties)
    - [Functions](#functions)
    - [References](#references)
    - [Categories](#categories)
    - [Custom Annotations](#custom-annotations)
- [Dependency Injection](#dependency-injection)
  - [Dagger 2.x Support](#dagger-2x-support)
    - [Supported Versions](#supported-versions)
    - [Limitations](#limitations)
    - [Module Dependencies](#module-dependencies)
- [Troubleshooting](#troubleshooting)
  - [Java Compatibility](#java-compatibility)
  - [Documentation](#documentation)
  - [Logging](#logging)
  - [Kotlin `stdlib` Conflict](#kotlin-stdlib-conflict)
  - [Gradle Plugin and APT Options](#gradle-plugin-and-apt-options)
  - [Gradle Sync `AbstractMethodError`](#gradle-sync-abstractmethoderror)
  - [Proguard](#proguard)
  - [Getting `ClassInstanceNotFoundException`](#getting-classinstancenotfoundexception)
  - [Missing Items](#missing-items)
- [Build](#build)
- [Contributing](#contributing)
- [License](#license)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

**Reasoning**  
While developing some feature `Z`, there's nothing more annoying than having to go through `X`, to get to `Y`, to test
 your changes on `Z`. So it's not uncommon for developers to sometimes try and shortcut that process... Which inevitably
 leads to your humiliation when your colleagues notice you committed said shortcut.

**Example**  
Simply adding the [`@DeveloperFunction`](https://nextfaze.github.io/dev-fun/com.nextfaze.devfun.function/-developer-function/) annotation to a function/method is all that is needed.
```kotlin
class MyClass {
    @DeveloperFunction
    fun someFunction() {
        // ...
    }
}
```
See the documentation for advanced usage, including custom names, custom arguments, groups, function processing, etc.

<img src="https://github.com/NextFaze/dev-fun/raw/gh-pages/assets/gif/enable-sign-in.gif" align="right">

## Quick Start
#### Project Setup
- **REQUIRED** Android Gradle 3.0.0+ (due to [#37140464](https://issuetracker.google.com/issues/37140464) and [KT-16589](https://youtrack.jetbrains.com/issue/KT-16589))
- Recommended to use Kotlin 1.2.71, though should work down to 1.1.1 _(somewhat untested)_
- Recommended to use KAPT3 (`plugins { kotlin("kapt") }`), though KAPT1 also works
- Compiled with `minSdkVersion` >= 15
- Built against **AndroidX 1.0.0** (this is equivalent to Android Support libraries 28.0.0)  
  See [Migrating to AndroidX](https://developer.android.com/jetpack/androidx/migrate) for more information (it's actually quite easy).  
  Also, despite the migration docs above mentioning `-rc01` for some things - most things have final releases (Google's Maven Repo)[https://maven.google.com].

#### Build File `build.gradle`
Add the DevFun Gradle plugin to your build script.

If you can use the Gradle `plugins` block (which locates and downloads it for you):
```kotlin
plugins {
    id("com.nextfaze.devfun") version "2.0.0-RC1"
}
```

**Or** the legacy method using `apply`;  
Add the plugin to your classpath (found in the `jcenter()` repository):
```kotlin
buildscript {
    dependencies {
        classpath("com.nextfaze.devfun:devfun-gradle-plugin:2.0.0-RC1")
    }
}
```

And in your `build.gradle`:
```kotlin
apply {
    plugin("com.nextfaze.devfun")
}
```

#### Modules
Can be categorized into 5 types:
- Main: minimum required libraries (annotations and compiler).
- Core: extend the functionality or accessibility of DevFun (e.g. add menu/http server).
- Inject: facilitate dependency injection for function invocation.
- Util: frequently used or just handy functions (e.g. show Glide memory use).
- Invoke UI: provide additional views for the invocation UI (e.g. a color picker for `int` args)

Add `jcenter()` repository or via bintray directly: _(will be put on MavenCentral eventually)_
```kotlin
repositories {
    maven { setUrl("https://dl.bintray.com/nextfaze/dev-fun") }
}
```

Add dependencies to build.gradle:
```kotlin
dependencies {
    // Annotations, Compiler, and Developer Menu
    kaptDebug("com.nextfaze.devfun:devfun-compiler:2.0.0-RC1")
    implementation("com.nextfaze.devfun:devfun-annotations:2.0.0-RC1")
    debugImplementation("com.nextfaze.devfun:devfun-menu:2.0.0-RC1")
    
    // Dagger 2.x component inspector - only if using Dagger 2.x!
    debugImplementation("com.nextfaze.devfun:devfun-inject-dagger2:2.0.0-RC1")
    
    // Chrome Dev Tools JavaScript console integration
    debugImplementation("com.nextfaze.devfun:devfun-stetho:2.0.0-RC1")
        
    // HTTP server and simple index page
    debugImplementation("com.nextfaze.devfun:devfun-httpd:2.0.0-RC1")
    debugImplementation("com.nextfaze.devfun:devfun-httpd-frontend:2.0.0-RC1")
    
    // Glide util functions
    debugImplementation("com.nextfaze.devfun:devfun-util-glide:2.0.0-RC1")
    
    // Leak Canary util functions
    debugImplementation("com.nextfaze.devfun:devfun-util-leakcanary:2.0.0-RC1")
    
    /*
     * Transitively included libs - in general you don"t need to add these explicitly (except maybe for custom module libs).
     */
    
    // Adds view factory handler for @ColorPicker for invoke UI - transitively included via devfun-menu
    // debugImplementation("com.nextfaze.devfun:devfun-invoke-view-colorpicker:2.0.0-RC1")
    
    // DevFun core - transitive included from menu et al.
    // debugImplementation("com.nextfaze.devfun:devfun:2.0.0-RC1")
}
```

That's it!

Start adding [`@DeveloperFunction`](https://nextfaze.github.io/dev-fun/com.nextfaze.devfun.function/-developer-function/)
annotations to methods (can be private, and arguments will be injected or rendered as needed), and these will be added to the menu.

For advanced uses and configuration such as custom categories, item groups, argument providers, etc.
- See the [wiki](https://nextfaze.github.io/dev-fun/wiki/), or
- Extensive (Dokka generated) documentation can be accessed at [GitHub Pages](https://nextfaze.github.io/dev-fun/).


## Key Features

<img src="https://github.com/NextFaze/dev-fun/raw/gh-pages/assets/images/invoke-ui.png" alt="Invocation UI for manual entry" width="35%" align="right"/>

#### Zero-configuration Setup  
A [Content Provider](https://nextfaze.github.io/dev-fun/com.nextfaze.devfun.core/-dev-fun-initializer-provider/)
 is used to automatically initialize DevFun, and most simple Dagger 2.x object graphs should work.  
See [Initialization](https://nextfaze.github.io/dev-fun/wiki/-setup%20and%20-initialization.html#initialization) for more information or for manual initialization details.

#### Context Aware  
Attempts are made to be aware of the current app state, in that the "Context" category should contain only the items
that are relevant to the current screen (i.e. methods tagged in the current Activity and any attached Fragments).

#### Invocation UI
If an annotated function has argument types that cannot be injected then DevFun will attempt to render a UI
for you to manually set them (right). The arguments can be injected or otherwise. Simple types and a color picker UI is provided by default.

Optional annotations can be added to configure/tweak their state and custom types can be added:
- [`@From`](https://nextfaze.github.io/dev-fun/com.nextfaze.devfun.invoke.view/-from/) for an initial value.
- [`@Ranged`](https://nextfaze.github.io/dev-fun/com.nextfaze.devfun.invoke.view/-ranged/) on a `number` type for a constrained value.
- [`@ColorPicker`](https://nextfaze.github.io/dev-fun/com.nextfaze.devfun.invoke.view/-color-picker/) on an `int` types renders a color picker.

#### Modular
DevFun is designed to be modular, in terms of both its dependencies (limiting impact to main source tree) and its plugin-like architecture.
See [Components](https://nextfaze.github.io/dev-fun/wiki/-components.html) for more information.

![Component Dependencies](https://github.com/NextFaze/dev-fun/raw/gh-pages/assets/uml/components.png)  

#### And More
- Keep your methods private, but be able to easily invoke them when needed.
- Avoid those one-time development conditions.
- Provide future developers (undoubtedly yourself) access to these quick dev switches *"oh, there's already a way to toggle xyz"*.


## Showcase

<img src="https://github.com/NextFaze/dev-fun/raw/gh-pages/assets/images/menu-auth.png" alt="Developer Menu on authenticate screen" width="35%" align="right"/>  

#### Developer Menu
An easy to use developer menu accessible at any time via a floating cog button added by the `devfun-menu` module (right):

Can be accessed via a floating cog, the tilde/grave key `` ` ``, or the volume button sequence `down, down, up, down`.

#### Invocation UI
When DevFun can't inject a type it will attempt to render a UI for manual entry. By default all simple type are supported.
View factories can be provided to allow DevFun to render a custom view for other types.

An example of this is the [`@ColorPicker`](https://nextfaze.github.io/dev-fun/com.nextfaze.devfun.invoke.view/-color-picker/) annotation used
 by the menu cog overlay (second right) the renders a color picker view for an int parameter.

#### Overlay Loggers
Adding [`@DeveloperLogger`](https://nextfaze.github.io/dev-fun/com.nextfaze.devfun.reference/-developer-logger/) to a property, type, or
function will generate a floating text overlay that (by default) updates once a second with the `.toString()` of that reference.
The loggers are context-aware with additional configuration options available with a long-press.

This feature is somewhat experimental in that it's quite new and has not been used extensively beyond the demo app and a few in-house
apps so please report any issues you have with it.

Adding [`@DeveloperProperty`](https://nextfaze.github.io/dev-fun/com.nextfaze.devfun.function/-developer-property/) as well will allow you
to tap on the overlay to edit its value using the invocation UI system.

#### Local HTTP Server (experimental)  
Using the HTTPD modules `devfun-httpd` and `devfun-httpd-frontend`, a local server can be exported using ADB `adb forward tcp:23075 tcp:23075` 
and accessed via http://localhost:23075 where you can invoke functions from your browser:
<img src="https://github.com/NextFaze/dev-fun/raw/gh-pages/assets/images/httpd-auth-context.png" alt="Authenticate screen via local HTTP server" width="60%"/>
<img src="https://github.com/NextFaze/dev-fun/raw/gh-pages/assets/images/color-picker.png" alt="Invocation UI with custom view handling" width="35%" align="right"/>  

#### Stetho Integration (experimental)
With the `devfun-stetho` module functions are exported and available to be invoked directly from Chrome's Developer Tools console:  
<img src="https://github.com/NextFaze/dev-fun/raw/gh-pages/assets/images/stetho-auth.png" alt="Stetho integration function list" width="60%"/>


## Annotations Overview and Custom Annotations/Properties
DevFun has three types of annotations. "Functions", "References", and "Categories". These are defined using the meta
annotation [`@DeveloperAnnotation`](https://nextfaze.github.io/dev-fun/com.nextfaze.devfun/-developer-annotation/).

- Function: intended to be invoked
- References: allows "tagging" elements
- Categories: purely for naming/grouping/ordering

The majority of DevFun's annotations are `SOURCE` retained. If for some reason you want a `RUNTIME` retained or whatever then copy-paste
an existing annotation and change the retention.

### Functions
- [`@DeveloperFunction`](https://nextfaze.github.io/dev-fun/com.nextfaze.devfun.function/-developer-function/)
  Annotating a function allows you to invoke it at run-time using best-effort DI and/or a UI for value entry.
- [`@DeveloperArguments`](https://nextfaze.github.io/dev-fun/com.nextfaze.devfun.function/-developer-arguments/)
  The same as `@DeveloperFunction` but allows you to supply a list of default argument values. Each entry is like adding another `@DeveloperFunction` with default arguments.
- [`@DeveloperProperty`](https://nextfaze.github.io/dev-fun/com.nextfaze.devfun.function/-developer-property/)
  Allows you to set a property value (only simple types supported at the moment)

### References
- [`@DeveloperReference`](https://nextfaze.github.io/dev-fun/com.nextfaze.devfun.reference/-developer-reference/)
  Tags the element - retrieve later at run-time via `devFun.developerReferences<DeveloperReference>()` - in general you'll want to create
  your own annotation "tag".
- [`@[Dagger2Component`](https://nextfaze.github.io/dev-fun/com.nextfaze.devfun.reference/-dagger2-component/)
  Used in conjunction with dagger2 inject lib. Use it to tag functions/properties/extensions to tell DevFun how/where to get your components.
- [`@DeveloperLogger`](https://nextfaze.github.io/dev-fun/com.nextfaze.devfun.reference/-developer-logger/)
  Results in a floating overlay they calls the function every 1 second (by default - long-press to change) showing the `.toString()` of the
  return value. When used in conjunction with `@DeveloperProperty` tapping the overlay shows the edit dialog.

### Categories
As mentioned above, they are for naming/grouping/ordering. There is just the standard one by default, however you could create one that has
different default values or whatever.
- [`@DeveloperCategory`](https://nextfaze.github.io/dev-fun/com.nextfaze.devfun.category/-developer-category/)
  Used to override default category names/groups/ordering
- [`@ContextCategory`](https://nextfaze.github.io/dev-fun/com.nextfaze.devfun.category/-context-category/)
  Used to manually flag an item into the "Context" category (usually reserved for Android "scoped" items such as Activity/Fragment/etc)

### Custom Annotations
As mentioned, DevFun annotations are defined using `@DeveloperAnnotation`. Properties named the same as the DevFun supplied defaults have
the same meaning/behaviour.

Other properties will be "serialized" and available during the transformation phase (or from the definition object) - the definition will
implement `WithProperties`


# Dependency Injection
DevFun uses a simple [`InstanceProvider`](https://nextfaze.github.io/dev-fun/com.nextfaze.devfun.inject/-instance-provider/) interface to
source object instances.

Providers are checked most recently added first (i.e. user-supplied providers will be checked first). A number of 
default providers are added, including;
- Android `Application`, `Activity`, `Context` (current `Activity` or `Application`), `Fragment`, and `View` types.
- All DevFun related objects
- Support for Kotlin `object` instances
- Object instantiation (new instance + DI). *(opt-in only)*

For more details see wiki entry [Dependency Injection](https://nextfaze.github.io/dev-fun/wiki/-dependency%20-injection.html).

## Dagger 2.x Support
Easiest method is to use `devfun-inject-dagger2` module - by default it should work just by adding the dependency depending on your setup
 (if the components are located in the Application and/or Activity classes). However if you use extension functions to retrieve your
 components (or you put them in weird places for whatever reason), then you can annotate the functions/properties/getters with
 [`@Dagger2Component`](https://nextfaze.github.io/dev-fun/com.nextfaze.devfun.reference/-dagger2-component/).

If all else fails you can define your own instance provider with utility functions from `devfun-inject-dagger2` (see the demo for an example). 

### Supported Versions
Dagger has been tested on the demo app from versions 2.4 to 2.16, and various in-house apps on more recent versions, and should function
 correctly for most simple scopes/graphs. 

For reference the demo app uses three scopes; Singleton, Retained (fragments), and an Activity scope. It uses both type-annotated scoping
 and provides scoping. It keeps component instances in the activity and obtains the singleton scope via an extension function. In general
 this should cover most use cases - if you encounter any problems please create an issue.

### Limitations
DevFun uses a number of methods iteratively to introspect the generated components/modules, however depending on scoping, visibility, and
 instantiation of a type it can be difficult to determine the source/scope in initial (but faster) introspection methods. 

When all else fails DevFun will use a form of heavy reflection to introspect the generated code - types with a custom scope and no
 constructor arguments are not necessarily obtainable from Dagger (depends on the version) by any other means. To help with this ensure your
 scope is `@Retention(RUNTIME)` so that DevFun wont unintentionally create a new instance when it can't find it right away.

Due to the way Dagger generates/injects it is not possible to obtain the instance of non-scoped types from the generated component/module
 as its instance is created/injected once (effectively inlined) at the inject site. It is intended to allow finding instances based on the
 context of the dev. function in the future (i.e. if the dev. function is in a fragment then check for the injected instance in the fragment
 etc.) - if this is desirable sooner make a comment in the issue #26.

### Module Dependencies
Though the inject module uses Dagger, it does not export it as a dependency (declared `compileOnly`). This is to stop DevFun from
 affecting your project's version. The inject module only uses references to Dagger's annotations `@Component`, `@Module`, and `@Provides`.
 It also references annotations from `javax.inject` (which is pulled in from Dagger). It does not use/generate any other aspect of the
 Dagger APIs explicitly (only via reflection during introspection) and thus is compatible with all Dagger versions from 2.4.


# Troubleshooting
## Java Compatibility
DevFun was design with Kotlin in mind. Having said that, Kotlin is implicitly compatible with Java and thus DevFun
 should work as expected when used in/with Java code (you will still need to use KAPT however as the generated code is
 in Kotlin).

Submit an issue if you encounter any problems as it is desirable that there be 100% compatibility.


## Documentation
See the [wiki](https://nextfaze.github.io/dev-fun/wiki/) for advanced configuration details.  
Documentation (Dokka) can be accessed at [GitHub Pages](https://nextfaze.github.io/dev-fun/).


## Logging
SLF4J is used for all logging.

By default `trace` is disable unless library is a snapshot build (i.e. `BuildConfig.VERSION.endsWith("-SNAPSHOT")`).  
This can also be toggled at any time via [devFunVerbose](https://nextfaze.github.io/dev-fun/com.nextfaze.devfun.core/dev-fun-verbose.html)


## Kotlin `stdlib` Conflict 
DevFun was compiled using Kotlin 1.2.71.  
*Earlier versions of Kotlin are largely untested and unsupported (this is unlikely to change unless explicitly requested).*  

Thus if you receive a dependency conflict error such as:  
`Error: Conflict with dependency 'org.jetbrains.kotlin:kotlin-stdlib' in project ':app'. Resolved versions for app (1.1.2-3) and test app (1.1.1) differ. See http://g.co/androidstudio/app-test-app-conflict for details.`
The simplest resolution is updating your Kotlin version to match.

If this is not possible, you can force Gradle to use a specific version of Kotlin:
```kotlin
// Force specific Kotlin version
configurations.all {
    resolutionStrategy.force(
            "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version",
            "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version",
            "org.jetbrains.kotlin:kotlin-reflect:$kotlin_version"
    )
}
```

## Gradle Plugin and APT Options
The gradle plugin `com.nextfaze.devfun` should automatically handle/provide the annotation processor with your project build configuration,
 but can also be configured within your build file ([DevFunExtension](https://nextfaze.github.io/dev-fun/com.nextfaze.devfun.gradle.plugin/-dev-fun-extension/)):
```kotlin
devFun {
    packageSuffix = "..."
    packageRoot = "..."
    packageOverride = "..."
}
```  

However due to limitations of the build system this can/is somewhat derived with string splitting and/or by using known relative paths to
 processor outputs as needed.    
e.g. To determine the package/buildType/flavor, `BuildConfig.java` is located as:
```regexp
(?<buildDir>.*)(/intermediates/classes/|/tmp/kotlin-classes/|/tmp/kapt3/classes/)(?<variant>.*)/META-INF/services/.*
```

If your build system has been customised or for whatever reason the processor cannot identify your build information then
 you can manually specify the required information using annotation processor options (values via APT args will override `devFun {}` gradle 
 plugin values).  
Apply using Android DSL:
```kotlin
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

See [com.nextfaze.devfun.compiler](https://nextfaze.github.io/dev-fun/com.nextfaze.devfun.compiler/#properties) for list of options.


## Gradle Sync `AbstractMethodError`
The DevFun Gradle plugin is used to tell DevFun your build variant information. It does this by setting APT values via
[`DevFunKotlinGradlePlugin`](https://nextfaze.github.io/dev-fun/com.nextfaze.devfun.gradle.plugin/-dev-fun-kotlin-gradle-plugin/) which
implements `KotlinGradleSubplugin`, which is a part of Kotlin's `kotlin-gradle-plugin-api` package. Unfortunately Kotlin have been changing
their interface definition for recent minor versions (<1.2.5x, 1.2.6x, and 1.2.7x), so if another plugin is using a different version
of `KotlinGradleSubplugin` one of them will fail.

Most of the time you don't not *need* the Gradle plugin - without it DevFun will attempt to "guess" your variant information using various
file naming and path regex hacks during the APT phase directly (and generally it's pretty good at it).  
TLDR; If you can remove the plugin and it still works than that's fine.

Wost case scenario you can explicitly tell DevFun where (the package) you want the generated files using APT args:
```kotlin
android.defaultConfig.javaCompileOptions.annotationProcessorOptions {
    argument("devfun.package.override", "my.app.devfun.generated")
}
```


## Proguard
Very basic support for proguard exists (libraries keep their generated sources), though it has not been tested extensively - DevFun is
 intended for Debug builds and thus support for Proguard has never been a priority.

- Rules that are supplied by DevFun libraries can be found in [proguard-rules-common.pro](https://github.com/NextFaze/dev-fun/blob/master/proguard-rules-common.pro) 
 (the Glide util module also adds a couple).
- See the demo [proguard-rules.pro](https://github.com/NextFaze/dev-fun/blob/master/demo/proguard-rules.pro) for the minimum you'll need in
 your own app.

As one of the goals of DevFun is to keep your production code clean, most of DevFun's annotations are `SOURCE` retained and cannot be
 referenced in your proguard rules. However if an annotated element is `public` or `internal` DevFun will reference the element directly in
 its generated code (rather than via reflection). Exceptions to this are (at the moment); `@Constructable`, `@ColorPicker`, `@Ranged`,
 and `@From`, which are all `RUNTIME` retained - it is desirable in the future that these too become `SOURCE` retained. 


## Getting `ClassInstanceNotFoundException`
When DevFun is unable to find the instance of a type it throws `ClassInstanceNotFoundException` - this happens when
 there is no instance provider capable of providing the type to DevFun. If there is a view factory defined for the type the
 invocation UI will be shown for manual entry, otherwise and error dialog will be shown with the exception details.

See the wiki entry on [Dependency Injection](https://nextfaze.github.io/dev-fun/wiki/-dependency%20-injection.html) 
for details on how to set up custom instance providers.

For a quick and dirty fix utility functions [`captureInstance`](https://nextfaze.github.io/dev-fun/com.nextfaze.devfun.inject/capture-instance.html)
 and [`singletonInstance`](https://nextfaze.github.io/dev-fun/com.nextfaze.devfun.inject/singleton-instance.html) can create an instance
 provider that you can give to DevFun `devFun.instanceProviders += singletonInstance { ... }`.
 
e.g.
```kotlin
class SomeType : BaseType

val provider = captureInstance { someObject.someType } // triggers for SomeType or BaseType
val someOtherThing = singletonInstance { MyClass() } // lazy initialization
val anotherThing = singletonInstance { devFun.get<MyType>() } // lazy initialization using DevFun to inject/create type
```

If you want to reduce the type range then specify its base type manually:
```kotlin
val provider = captureInstance<BaseType> { someObject.someType } // triggers only for BaseType
```

_Be aware of leaks! The lambda could implicitly hold a local `this` reference._


## Missing Items
If you are using Android Gradle versions prior to 3.0.0, then this is likely due to tooling issues where APT generated
 resources of Application projects were not packaged into the APK (see [#37140464](https://issuetracker.google.com/issues/37140464)
 and [KT-16589](https://youtrack.jetbrains.com/issue/KT-16589)).  
The extreme hacks/support for this were removed in DevFun 0.1.2 and are unlikely to be re-implemented.  
Android Gradle 3.0.0 and 3.1.0 are quite stable and its unlikely newer versions of Android Studio will support earlier than that anyway.



# Build
Open project using Android Studio (usually latest preview). Opening in IntelliJ is untested, though it should work.
```bash
git clone git@github.com:NextFaze/dev-fun.git
cd dev-fun
```

**Artifacts**  
Build using standard gradle.
```bash
./gradlew build
```

**Demo**  
See the included demo project for a simple app.
```bash
./gradlew :demo:installDebug
adb shell monkey -p com.nextfaze.devfun.demo.debug -c android.intent.category.LAUNCHER 1
```

- See [RELEASING.md](RELEASING.md) for building artifacts and documentation.



# Contributing
DevFun is intended for developers (though it can be handy for testers). Because of this the API is intended to be quite open and flexible. 
So if there is anything you want or think should be added then create an issue or PR and more than likely it will be accepted.  

- Any bugs please report (desirably with reproduction steps) and/or PR/fix them.
- Any crashes please report with the stack trace (doesn't need to be reproducible) and it'll be fixed (if DevFun's fault) and/or wrapped
 with proper error handling functionality.

Feel free also to submit your own handy util functions or whatever for addition.



# License

    Copyright 2018 NextFaze

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
       http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
