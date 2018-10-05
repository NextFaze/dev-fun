[gh-pages](../../index.md) / [com.nextfaze.devfun.internal.android](../index.md) / [BaseDialogFragment](./index.md)

# BaseDialogFragment

`abstract class BaseDialogFragment : AppCompatDialogFragment` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-internal/src/main/java/com/nextfaze/devfun/internal/android/Fragments.kt#L12)

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `BaseDialogFragment()` |

### Functions

| Name | Summary |
|---|---|
| [onCreate](on-create.md) | `open fun onCreate(savedInstanceState: `[`Bundle`](https://developer.android.com/reference/android/os/Bundle.html)`?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [onDestroy](on-destroy.md) | `open fun onDestroy(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [onDestroyView](on-destroy-view.md) | `open fun onDestroyView(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [onDismiss](on-dismiss.md) | `open fun onDismiss(dialog: `[`DialogInterface`](https://developer.android.com/reference/android/content/DialogInterface.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [onPerformDismiss](on-perform-dismiss.md) | `open fun onPerformDismiss(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>We will not get an onDismiss callback if a dialog fragment is opened on an activity that is then finished while the dialog is open. However we generally want to clean up if we are destroyed, so worst-case we perform the dismiss callback then if it hasn't already happened. |

### Companion Object Functions

| Name | Summary |
|---|---|
| [dismiss](dismiss.md) | `fun <T : DialogFragment> dismiss(activity: FragmentActivity): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [show](show.md) | `fun <T : DialogFragment> show(activity: FragmentActivity, obtain: () -> `[`T`](show.md#T)`): `[`T`](show.md#T) |
| [showNow](show-now.md) | `fun <T : DialogFragment> showNow(activity: FragmentActivity, obtain: () -> `[`T`](show-now.md#T)`): `[`T`](show-now.md#T) |

### Extension Properties

| Name | Summary |
|---|---|
| [defaultTag](../androidx.fragment.app.-fragment/default-tag.md) | `val Fragment.defaultTag: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

### Extension Functions

| Name | Summary |
|---|---|
| [show](../androidx.fragment.app.-dialog-fragment/show.md) | `fun DialogFragment.show(fragmentManager: FragmentManager): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [showNow](../androidx.fragment.app.-dialog-fragment/show-now.md) | `fun DialogFragment.showNow(fragmentManager: FragmentManager): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
