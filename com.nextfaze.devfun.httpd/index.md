[gh-pages](../index.md) / [com.nextfaze.devfun.httpd](./index.md)

## Package com.nextfaze.devfun.httpd

Adds a local HTTP server (uses [NanoHttpD](https://github.com/NanoHttpd/nanohttpd)).

### Types

| Name | Summary |
|---|---|
| [AbstractUriHandler](-abstract-uri-handler/index.md) | `abstract class AbstractUriHandler : UriResponder` |
| [DevHttpD](-dev-http-d/index.md) | `class DevHttpD : `[`AbstractDevFunModule`](../com.nextfaze.devfun.core/-abstract-dev-fun-module/index.md) |
| [HttpDRouter](-http-d-router/index.md) | `class HttpDRouter : RouterNanoHTTPD` |

### Properties

| Name | Summary |
|---|---|
| [devDefaultPort](dev-default-port.md) | `var devDefaultPort: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>The port to use for HTTPD server. Default value is 23075. |
| [devHttpD](dev-http-d.md) | `val `[`DevFun`](../com.nextfaze.devfun.core/-dev-fun/index.md)`.devHttpD: `[`DevHttpD`](-dev-http-d/index.md) |
