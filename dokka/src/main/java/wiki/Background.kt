package wiki

/**
Background and History

This library grew very organically over a number of years, originally in Java and using Guice.

Having finished up with a client recently and wanting to delve into Kotlin/KAPT a bit more, it was decided that this
concept could be quite useful if developed more officially.

Attempts have been made to define a well-documented and flexible public API, but some of it is blatantly converted
Java code. Thus the API/implementation details are still very open to discussion/changes.

__Example Login Flow__ *(see demo [AuthenticateFragment.signInAs](https://github.com/NextFaze/dev-fun/blob/master/demo/src/main/java/com/nextfaze/devfun/demo/AuthenticateScreen.kt#L225))*
In the development of your app, you are a good developer and you use test accounts (and services of course) to validate
various requirements (such as underage, male, female, geo locks etc.).

Changing accounts over and over can be annoying, and especially coming back months later and trying to remember/find
them again. Or you can use DevFun and have yourself a nice auto-login flow.

__Example Registration Flow__ *(see demo [RegisterFragment.populateRandomly](https://github.com/NextFaze/dev-fun/blob/master/demo/src/main/java/com/nextfaze/devfun/demo/RegisterScreen.kt#L193))*
You're developing some affiliate registration flow, ensuring various flags are set/passed around correctly.

For each time you want to test it in its entirety you need to enter a name, password, date of birth, etc, etc.
Or you can use DevFun - write a bit of code that randomly fills these in, annotate the method, and run said method to
instantly fill in the whole form - all you have to do it click/tap a button.
 */
object Background
