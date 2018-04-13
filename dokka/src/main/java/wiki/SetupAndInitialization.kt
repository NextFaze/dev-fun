@file:Suppress("IllegalIdentifier", "ClassName", "unused")

package wiki

import com.nextfaze.devfun.core.DevFun
import com.nextfaze.devfun.core.DevFunInitializerProvider

/**
Compiler configuration and initialization process.

Attempts have been made to require as little set-up as possible.<br />
However many of us inherit a code base that is not necessarily so... Nice...<br />
Thus where possible most things can be initialized and configured manually.

If you encounter any instances where something can't be initialized manually or you have difficulties doing so (documentation
not clear etc.), then please create an issue about it - I have not extensively tested this aspect.


## Compiler Configuration
The compiler will attempt to auto-detect project details by using the class output directory and known/standard relative
paths to build files, but if necessary a number of aspects can be set as annotation processor args (see [com.nextfaze.devfun.compiler]).


## Initialization
### Automatic
A Content Provider [DevFunInitializerProvider] is used to automatically initialize DevFun.

This can be disabled using standard Android manifest merger syntax:
 * ```xml
 * <manifest xmlns:android="http://schemas.android.com/apk/res/android"
 *           xmlns:tools="http://schemas.android.com/tools">
 *
 *     <application>
 *         <!-- This will stop the provider node from being included -->
 *         <provider android:name="com.nextfaze.devfun.core.DevFunInitializerProvider"
 *                   android:authorities="*"
 *                   tools:ignore="ExportedContentProvider"
 *                   tools:node="remove"/>
 *     </application>
 * </manifest>
 * ```

### Manual
Manual initialization can be performed at any time.
See [DevFun.initialize] for more information.

```kotlin
DevFun().initialize(application)
```
 */
object `Setup and Initialization`
