[gh-pages](../../index.md) / [com.nextfaze.devfun.inject](../index.md) / [Constructable](index.md) / [singleton](./singleton.md)

# singleton

`val singleton: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-annotations/src/main/java/com/nextfaze/devfun/inject/InstanceProvider.kt#L187)

If `true` then a single shared instance will be constructed.
Be careful when using this on inner classes as it will hold a reference to its outer class.
i.e. Only use this if the outer class is an object/singleton.

