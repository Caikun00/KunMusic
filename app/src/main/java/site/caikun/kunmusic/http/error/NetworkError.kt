package site.caikun.kunmusic.http.error

class NetworkError {

    companion object {
        const val UNKNOWN_ERROR = 1000
        const val PARSE_ERROR = 1001
        const val NETWORK_ERROR = 1002
        const val HTTP_ERROR = 1003
        const val SSL_ERROR = 1004
        const val TIMEOUT_ERROR = 1005
    }
}