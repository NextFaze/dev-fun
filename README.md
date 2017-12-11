# DevFun - Developer (Fun)ctions
<img src="https://github.com/NextFaze/dev-fun/raw/gh-pages/assets/gif/registration-flow.gif" align="right">
A developer targeted library to facilitate the development and testing of Android apps by enabling the invocation of arbitrary functions and code using tagging annotations.

By tagging (annotating) functions of interest, DevFun provides direct access to these functions at run-time through a
number of means - such as an automatically generated "Developer Menu".

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->


  - [Quick Start](#quick-start)
      - [Project Setup](#project-setup)
      - [Dependencies](#dependencies)
  - [Key Features](#key-features)
      - [Zero-configuration Setup](#zero-configuration-setup)
      - [Context Aware](#context-aware)
      - [Modular](#modular)
      - [And More](#and-more)
  - [Showcase](#showcase)
      - [Developer Menu](#developer-menu)
      - [Local HTTP Server](#local-http-server)
      - [Stetho Integration](#stetho-integration)
- [Dependency Injection](#dependency-injection)
- [Troubleshooting](#troubleshooting)
  - [Java Compatibility](#java-compatibility)
  - [Documentation](#documentation)
  - [Logging](#logging)
  - [Kotlin `stdlib` Conflict](#kotlin-stdlib-conflict)
  - [Compiler Options](#compiler-options)
  - [Proguard](#proguard)
    - [Transformers](#transformers)
  - [Getting `ClassInstanceNotFoundException`](#getting-classinstancenotfoundexception)
  - [Missing Items](#missing-items)
- [Build](#build)
- [License](#license)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

**Reasoning**  
While developing some feature `Z`, there's nothing more annoying than having to go through `X`, to get to `Y`, to test
 your changes on `Z`. So it's not uncommon for developers to sometimes try and shortcut that process... Which inevitably
  leads to your humiliation when your colleagues notice you committed said shortcut.

**Example**  
Simply adding the [`@DeveloperFunction`](https://nextfaze.github.io/dev-fun/com.nextfaze.devfun.annotations/-developer-function/) annotation to a function/method is all that is needed.
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
- **REQUIRED** Android Gradle 3.0.0 _(earlier versions may work intermittently, but will have missing items due to annotation processing issues)_
  - See [#37140464](https://issuetracker.google.com/issues/37140464) and [KT-16589](https://youtrack.jetbrains.com/issue/KT-16589)
- Recommended to use Kotlin 1.2.10, though should work down to 1.1.1 _(largely untested)_
- Recommended to use KAPT3 (`apply plugin: 'kotlin-kapt'`), though KAPT1 also works
- Compiled with `minSdkVersion` >= 15
- Built against Android Support libraries 26.1.0

#### Dependencies
Can be categorized into 4 types:
- Main: minimum required libraries (annotations and compiler).
- Core: extend the accessibility of DevFun (e.g. add menu/http server).
- Inject: facilitate dependency injection for function invocation.
- Util:  frequently used or just handy functions (e.g. show Glide memory use).

Add repository to module: _(will be put on JCenter &amp; MavenCentral eventually)_
```gradle
repositories {
    maven { url 'https://dl.bintray.com/nextfaze/dev-fun' }
}
```

Add dependencies to build.gradle:
```gradle
    // Annotations, Compiler, and Developer Menu
    kaptDebug 'com.nextfaze.devfun:devfun-compiler:0.1.5'
    compile 'com.nextfaze.devfun:devfun-annotations:0.1.5'
    // debugCompile 'com.nextfaze.devfun:devfun:0.1.5' // shared lib - transitive from menu et al.
    debugCompile 'com.nextfaze.devfun:devfun-menu:0.1.5'
    
    // Dagger 2.x component inspector - only if using Dagger!
    debugCompile 'com.nextfaze.devfun:devfun-inject-dagger2:0.1.5'
    
    // Chrome Dev Tools JavaScript console integration
    debugCompile 'com.nextfaze.devfun:devfun-stetho:0.1.5'
        
    // HTTP server and simple index page
    debugCompile 'com.nextfaze.devfun:devfun-httpd:0.1.5'
    debugCompile 'com.nextfaze.devfun:devfun-httpd-frontend:0.1.5'
    
    // Glide util functions
    debugCompile 'com.nextfaze.devfun:devfun-util-glide:0.1.5'
    
    // Leak Canary util functions
    debugCompile 'com.nextfaze.devfun:devfun-util-leakcanary:0.1.5'
```

That's it!

Start adding [`@DeveloperFunction`](https://nextfaze.github.io/dev-fun/com.nextfaze.devfun.annotations/-developer-function/)
annotations to methods (can be private), and these will be added to the menu.

For advanced uses and configuration such as custom categories, item groups, argument providers, etc.
- See the [wiki](https://nextfaze.github.io/dev-fun/wiki/), or
- Extensive (Dokka generated) documentation can be accessed at [GitHub Pages](https://nextfaze.github.io/dev-fun/).


## Key Features
#### Zero-configuration Setup  
A [Content Provider](https://nextfaze.github.io/dev-fun/com.nextfaze.devfun.core/-dev-fun-initializer-provider/)
 is used to automatically initialize DevFun, and most simple Dagger 2.x object graphs should work.  
See [Initialization](https://nextfaze.github.io/dev-fun/wiki/-setup%20and%20-initialization.html#initialization) for more information or for manual initialization details.

#### Context Aware  
Attempts are made to be aware of the current app state, in that the "Context" category should contain only the items
that are relevant to the current screen (i.e. methods tagged in the current Activity and any attached Fragments).

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
An easy to use developer menu accessible at any time via a floating cog button is added with the `devfun-menu` module (right):  

#### Local HTTP Server  
Using the HTTPD modules `devfun-httpd` and `devfun-httpd-frontend`, a local server can be exported using ADB `adb forward tcp:23075 tcp:23075` 
and accessed via http://localhost:23075 where you can invoke functions from your browser:  
<img src="https://github.com/NextFaze/dev-fun/raw/gh-pages/assets/images/httpd-auth-context.png" alt="Authenticate screen via local HTTP server" width="60%"/>

#### Stetho Integration  
With the `devfun-stetho` module functions are exported and available to be invoked directly from Chrome's Developer Tools console:  
<img src="https://github.com/NextFaze/dev-fun/raw/gh-pages/assets/images/stetho-auth.png" alt="Stetho integration function list" width="60%"/>



# Dependency Injection
DevFun uses a simple [InstanceProvider](https://nextfaze.github.io/dev-fun/com.nextfaze.devfun.inject/-instance-provider/) interface to source object instances.

Providers are checked most recently added first (i.e. user-supplied providers will be checked first). A number of 
default providers are added, including;
- Android `Application`, `Activity`, `Context` (current `Activity` or `Application`), `Fragment`, and `View` types.
- All DevFun related objects
- Support for Kotlin `object` instances
- Object instantiation (new instance + DI). *(opt-in only)*

For more details see wiki entry [Dependency Injection](https://nextfaze.github.io/dev-fun/wiki/-dependency%20-injection.html).



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
This can also be toggled at any time via [devFunVerbose](https://nextfaze.github.io/dev-fun/com.nextfaze.devfun.internal/dev-fun-verbose.html)


## Kotlin `stdlib` Conflict 
DevFun was compiled using Kotlin 1.2.10.  
*Earlier versions of Kotlin are largely untested and unsupported (this is unlikely to change unless explicitly requested).*  

Thus if you receive a dependency conflict error such as:  
`Error: Conflict with dependency 'org.jetbrains.kotlin:kotlin-stdlib' in project ':app'. Resolved versions for app (1.1.2-3) and test app (1.1.1) differ. See http://g.co/androidstudio/app-test-app-conflict for details.`
The simplest resolution is updating your Kotlin version to match.

If this is not possible, you can force Gradle to use a specific version of Kotlin:
```gradle
// Force specific Kotlin version
configurations.all {
    resolutionStrategy.force(
            "org.jetbrains.kotlin:kotlin-stdlib-jre7:$kotlin_version",
            "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version",
            "org.jetbrains.kotlin:kotlin-reflect:$kotlin_version"
    )
}
```


## Compiler Options
Due to limitations of the build system, the context of the build is derived by using known relative paths to processor outputs.  
To determine the package/buildType/flavor, `BuildConfig.java` is located as 
```regexp
(?<buildDir>.*)(/intermediates/classes/|/tmp/kotlin-classes/|/tmp/kapt3/classes/)(?<variant>.*)/META-INF/services/.*
```

If your build system has been customised or for whatever reason the processor cannot identify your build information,
you can manually specify the required information using annotation processor options.  

Apply using Android DSL:
```gradle
 android {
      defaultConfig {
          javaCompileOptions {
              annotationProcessorOptions {
                  argument 'devfun.argument', 'value'
              }
          }
      }
 }
```

See [com.nextfaze.devfun.compiler](https://nextfaze.github.io/dev-fun/com.nextfaze.devfun.compiler/#properties) for list of options.


## Proguard
Support for proguard exists, however it has not been tested extensively beyond the demo app.

If you wish to use DevFun with Proguard you will need to configure your app to keep DevFun related code (the DevFun
 libraries handle themselves). See the demo [proguard-rules.pro](https://github.com/NextFaze/dev-fun/blob/master/demo/proguard-rules.pro)
 for a sample and related details.  

Rules that are supplied by DevFun libraries can be found in [proguard-rules-common.pro](https://github.com/NextFaze/dev-fun/blob/master/proguard-rules-common.pro) 
(the Glide util module also adds a couple).

If everything used by DevFun (the annotated classes and functions) is _public or internal_ (see demo rules otherwise) then
 all you need to do is keep the generated file:
```proguard
# Keep DevFun generated class
-keep class your.package.goes.here.** extends com.nextfaze.devfun.generated.DevFunGenerated
```

For non-public/internal you will need to adjust your proguard config appropriately, or more simply use `@Keep` on the classes/functions.  


### Transformers
At the moment transformers needs to be in the main source tree (or wherever it's being used) as its class is referenced
 in the annotation. If for some reason you cannot use proguard but need sensitive code/values removed (e.g. auto-login
 items for test accounts à la [SignInFunctionTransformer](https://github.com/NextFaze/dev-fun/blob/master/demo/src/main/java/com/nextfaze/devfun/demo/AuthenticateScreen.kt#L179)),
 then surrounding the values in an `if (BuildConfig.DEBUG) { ... } else { .. }` block will allow the compiler to remove
 it as "dead code" during release builds.
I'm looking into better ways to handle this (suggestions welcome).


## Getting `ClassInstanceNotFoundException`
When DevFun is unable to find an instance of an object it throws `ClassInstanceNotFoundException` - this happens when
 there is no instance provider that can give the object to DevFun.

See the wiki entry on [Dependency Injection](https://nextfaze.github.io/dev-fun/wiki/-dependency%20-injection.html) 
for details on how to set this up.

For a quick and dirty fix a utility function [`captureInstance`](https://nextfaze.github.io/dev-fun/com.nextfaze.devfun.inject/capture-instance.html) can be used to capture an instance of an object.
e.g.
```kotlin
class SomeType : BaseType

val provider = captureInstance { someObject.someType } // triggers for SomeType or BaseType
```

If you want to reduce the type range then specify its base type manually:
```kotlin
val provider = captureInstance<BaseType> { someObject.someType } // triggers only for BaseType
```

_Be aware of leaks! The lambda could implicitly hold a local `this` reference._


## Missing Items
If you are using Android Gradle versions prior to 3.0.0, then this is likely due to tooling issues where APT generated
 resources on Application projects were not packaged into the APK (see [#37140464](https://issuetracker.google.com/issues/37140464)
 and [KT-16589](https://youtrack.jetbrains.com/issue/KT-16589)). The extreme hacks/support for this were removed in DevFun 0.1.2.



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



# License

    Copyright 2017 NextFaze

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
       http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
