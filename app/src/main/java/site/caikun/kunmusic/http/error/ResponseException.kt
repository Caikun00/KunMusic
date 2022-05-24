package site.caikun.kunmusic.http.error

import java.lang.Exception

data class ResponseException constructor(
    val code: Int,
    val throwable: Throwable
) : Exception() {

    override var message: String? = null
}