[gh-pages](../../index.md) / [com.nextfaze.devfun.menu](../index.md) / [MenuHeader](.)

# MenuHeader

`interface MenuHeader<T : `[`View`](https://developer.android.com/reference/android/view/View.html)`>` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-menu/src/main/java/com/nextfaze/devfun/menu/DeveloperMenu.kt#L77)

Provide an implementation of this to define your own header view.

See `DemoMenuHeader` for example.

### Functions

| Name | Summary |
|---|---|
| [onBindView](on-bind-view.md) | `abstract fun onBindView(view: T, parent: `[`ViewGroup`](https://developer.android.com/reference/android/view/ViewGroup.html)`, activity: `[`Activity`](https://developer.android.com/reference/android/app/Activity.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [onCreateView](on-create-view.md) | `abstract fun onCreateView(parent: `[`ViewGroup`](https://developer.android.com/reference/android/view/ViewGroup.html)`): T` |
