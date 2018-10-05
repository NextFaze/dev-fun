# Module annotations
The shared DevFun library - should be put in your `implementation` dependency.

Contains the annotations and interfaces that can be referenced from your main code without leaking their implementation
details into your final release build.

# Package com.nextfaze.devfun.core
Contains interfaces and default implementations.

# Package com.nextfaze.devfun.generated
Generated code will reference this package.

# Package com.nextfaze.devfun.inject
Facilitates dependency injection across DevFun.

# Package com.nextfaze.devfun.internal
Intended for use across DevFun libraries. Not intended for public consumption.

You're free to use it, but backwards compatibility wont be prioritized (though attempts will be made to do so), so no whining if anything in here breaks.
