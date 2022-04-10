
inline fun <T : Any> jso(): T = js("({})")
inline fun js(builder: dynamic.() -> Unit): dynamic = jso(builder)
inline fun <T : Any> jso(builder: T.() -> Unit): T = jso<T>().apply(builder)
