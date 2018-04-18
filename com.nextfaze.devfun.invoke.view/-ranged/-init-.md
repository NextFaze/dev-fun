[gh-pages](../../index.md) / [com.nextfaze.devfun.invoke.view](../index.md) / [Ranged](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`Ranged(from: `[`Double`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)` = 0.0, to: `[`Double`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)` = 100.0)`

Used to restrict the range of a [Number](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-number/index.html) for user input. Using this will render a slider rather than a text view.

Behind the scenes this is scaling the value range within `0 → 100` (via `SeekBar`).
Thus if you want a small range (e.g. 0 → 1 for say a color value), then you should use `to = 255.0` and then normalize it.

e.g.

``` kotlin
@DeveloperFunction
fun setRed(@Ranged(from = 0.0, to = 255.0) red: Int) {
    val redPct = red / 255f
    val someRedColor = Color.rgb(redPct, 0, 0) // pretend rgb() can't take ints...
    ...
}
```

Using this on anything other than a [Number](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-number/index.html) will do nothing.
