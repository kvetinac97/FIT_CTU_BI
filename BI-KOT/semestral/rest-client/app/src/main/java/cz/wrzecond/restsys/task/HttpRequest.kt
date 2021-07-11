package cz.wrzecond.restsys.task

data class HttpRequest (
    val url: String,
    val method: HttpMethod,
    val headers: MutableMap<String, String> = mutableMapOf(),
    val body: String? = null
)