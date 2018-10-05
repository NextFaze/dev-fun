[gh-pages](../../index.md) / [com.nextfaze.devfun.internal](../index.md) / [WithSubGroup](./index.md)

# WithSubGroup

`interface WithSubGroup` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/internal/Menu.kt#L13)

When implemented by a [FunctionItem](../../com.nextfaze.devfun.function/-function-item/index.md) the DevMenu will render a sub-group for it. *Not explicitly intended for public use - primarilyhere as a "fix/workaround" for #19 (where Context overrides the user defined group)*

Happy to hear of alternatives on how the menu is rendered w.r.t. groups/subgroups/trees/etc/whatever.

**Internal**
Use at your own risk.

### Properties

| Name | Summary |
|---|---|
| [subGroup](sub-group.md) | `abstract val subGroup: `[`CharSequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char-sequence/index.html)`?` |
