[gh-pages](../../index.md) / [com.nextfaze.devfun.httpd](../index.md) / [AbstractUriHandler](./index.md)

# AbstractUriHandler

`abstract class AbstractUriHandler : UriResponder` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-httpd/src/main/java/com/nextfaze/devfun/httpd/HttpD.kt#L260)

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `AbstractUriHandler()` |

### Functions

| Name | Summary |
|---|---|
| [delete](delete.md) | `open fun delete(uriResource: UriResource, urlParams: `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>, session: IHTTPSession): Response` |
| [get](get.md) | `open fun get(uriResource: UriResource, urlParams: `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>, session: IHTTPSession): Response` |
| [other](other.md) | `open fun other(method: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, uriResource: UriResource, urlParams: `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>, session: IHTTPSession): Response` |
| [post](post.md) | `open fun post(uriResource: UriResource, urlParams: `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>, session: IHTTPSession): Response` |
| [put](put.md) | `open fun put(uriResource: UriResource, urlParams: `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>, session: IHTTPSession): Response` |
