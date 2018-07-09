[gh-pages](../../index.md) / [com.nextfaze.devfun.internal.android](../index.md) / [BaseDialogFragment](index.md) / [onPerformDismiss](./on-perform-dismiss.md)

# onPerformDismiss

`open fun onPerformDismiss(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-internal/src/main/java/com/nextfaze/devfun/internal/android/Fragments.kt#L49)

We will not get an onDismiss callback if a dialog fragment is opened on an activity that is then finished while the dialog is open.
However we generally want to clean up if we are destroyed, so worst-case we perform the dismiss callback then if it hasn't already happened.

We do this instead of (only using) onDestroy as the destroy callback can/will happen a few messenger loops after dismiss which can
cause some annoying UI bugs.

