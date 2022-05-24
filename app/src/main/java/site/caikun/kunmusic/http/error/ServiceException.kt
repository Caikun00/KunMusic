package site.caikun.kunmusic.http.error

import java.lang.RuntimeException

class ServiceException constructor(
    val code: Int
) : RuntimeException() {

}