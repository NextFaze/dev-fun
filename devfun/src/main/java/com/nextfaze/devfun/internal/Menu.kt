package com.nextfaze.devfun.internal

import com.nextfaze.devfun.core.FunctionItem

/**
 * When implemented by a [FunctionItem] the DevMenu will render a sub-group for it. _Not explicitly intended for public use - primarily
 * here as a "fix/workaround" for #19 (where Context overrides the user defined group)_
 *
 * Happy to hear of alternatives on how the menu is rendered w.r.t. groups/subgroups/trees/etc/whatever.
 *
 * @internal Use at your own risk.
 */
interface WithSubGroup {
    val subGroup: CharSequence?
}
