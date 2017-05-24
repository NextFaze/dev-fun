[gh-pages](../../index.md) / [com.nextfaze.devfun.inject.dagger2](../index.md) / [InjectFromDagger2](index.md) / [&lt;init&gt;](.)

# &lt;init&gt;

`InjectFromDagger2()`

This module adds rudimentary support for searching Dagger 2.x component graphs for object instances.

*Due to limitations in KAPT it is not possible to generate Kotlin code that would then generate dagger bindings.Once this has been resolved it should be possible to resolve this more gracefully.*

On [DevFunModule.initialize](../../com.nextfaze.devfun.core/-dev-fun-module/initialize.md), your application (and its subclasses) are searched for a [Component](#). This is assumed
to be your top-level (singleton scoped) dagger component. An instance provider is then added referencing this instance.

At runtime (upon [InstanceProvider.get](../../com.nextfaze.devfun.inject/-instance-provider/get.md)) the component is traversed in a top-down fashion for a [Provider](#) or [Module](#)
that [Provides](#) the requested type *(or subclasses the type)*.

**This has not been tested extensively beyond simple object graphs!**

### Complex Object Graphs

A simple object graph is one that is linear; [Singleton](#) -&gt; [Activity](#) -&gt; [Fragment](#) -&gt; etc., or one that only ever
has a single type of some scope active at once. i.e. for the example below `MyChildScope1` and `MyChildScope2` - if
only one of the `ChildScope` is active at a time. It should also work for branched scopes *as long as the requested type iseffectively unique across scopes*.

An example where this may fail:

![Dagger 2 Scopes](https://github.com/NextFaze/dev-fun/raw/gh-pages/assets/uml/dagger2-scopes.png)

* Attempting to get an instance of the `PerScopeObject` will return the **first encountered** (which could be from
either scope depending on what the standard Java reflection API returns - *which explicitly states order is undefined*).

* Requesting `SomeFactory` or `SomeOtherObject` should work as expected since they are both unique.

If your dependency graph is too complicated you will need to provide your own instance provider with customized behaviour.

*I am looking into better ways to support this - suggestions/PRs welcomed.*

### Custom Provider

A helper function [tryGetInstanceFromComponent](../try-get-instance-from-component.md) can be used to reduce the search scope.

See [DemoInstanceProvider](https://github.com/NextFaze/dev-fun/tree/master/demo/src/debug/java/com/nextfaze/devfun/demo/devfun/DevFun.kt#L33) for this in use.

``` kotlin
private class DemoInstanceProvider(private val application: Application, private val activityProvider: ActivityProvider) : InstanceProvider {
    private val applicationComponent by lazy { application.applicationComponent!! }

    override fun <T : Any> get(clazz: KClass<out T>): T? {
        tryGetInstanceFromComponent(applicationComponent, clazz)?.let { return it }

        activityProvider()?.let { activity ->
            if (activity is DaggerActivity) {
                tryGetInstanceFromComponent(activity.retainedComponent, clazz)?.let { return it }
                tryGetInstanceFromComponent(activity.activityComponent, clazz)?.let { return it }
            }
        }

        return null
    }
}
```

When supplying your own instance provider (but want to use [tryGetInstanceFromComponent](../try-get-instance-from-component.md)), set
[useAutomaticDagger2Injector](../use-automatic-dagger2-injector.md) to `false` to disable the default instance provider.

Add/remove providers using [DevFun.instanceProviders](../../com.nextfaze.devfun.core/-dev-fun/instance-providers.md).

