package site.caikun.kunmusic.http.error

import com.google.gson.JsonParseException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import javax.net.ssl.SSLHandshakeException

/**
 * 网络异常处理
 */
class ExceptionHandler {

    companion object {

        private const val UNAUTHORIZED = 401
        private const val FORBIDDEN = 403
        private const val NOT_FOUND = 404
        private const val REQUEST_TIMEOUT = 408
        private const val INTERNAL_SERVER_ERROR = 500
        private const val BAD_GATEWAY = 502
        private const val SERVICE_UNAVAILABLE = 503
        private const val GATEWAY_TIMEOUT = 504

        fun handleException(throwable: Throwable): ResponseException = when (throwable) {
            is HttpException -> {
                val responseException = ResponseException(NetworkError.HTTP_ERROR, throwable)
                when (throwable.code()) {
                    UNAUTHORIZED,
                    FORBIDDEN,
                    NOT_FOUND,
                    REQUEST_TIMEOUT,
                    INTERNAL_SERVER_ERROR,
                    BAD_GATEWAY,
                    SERVICE_UNAVAILABLE,
                    GATEWAY_TIMEOUT -> {
                        responseException.message = throwable.code().toString()
                    }
                }
                responseException
            }
            is ServiceException -> {
                val serviceException: ServiceException = throwable
                val responseException = ResponseException(serviceException.code, serviceException)
                responseException.message = "服务器错误"
                responseException
            }
            is JsonParseException -> {
                val responseException = ResponseException(NetworkError.PARSE_ERROR, throwable)
                responseException.message = "解析错误"
                responseException
            }
            is SocketTimeoutException -> {
                val responseException = ResponseException(NetworkError.TIMEOUT_ERROR, throwable)
                responseException.message = "连接超时"
                responseException
            }
            is SocketException -> {
                val responseException = ResponseException(NetworkError.NETWORK_ERROR, throwable)
                responseException.message = "连接错误"
                responseException
            }
            is ConnectException -> {
                val responseException = ResponseException(NetworkError.NETWORK_ERROR, throwable)
                responseException.message = "连接错误"
                responseException
            }
            is SSLHandshakeException -> {
                val responseException = ResponseException(NetworkError.SSL_ERROR, throwable)
                responseException.message = "证书验证失败"
                responseException
            }
            else -> {
                val responseException = ResponseException(NetworkError.UNKNOWN_ERROR, throwable)
                responseException.message = "未知错误"
                responseException
            }
        }
    }
}