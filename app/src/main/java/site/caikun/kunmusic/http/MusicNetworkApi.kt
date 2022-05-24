package site.caikun.kunmusic.http

import okhttp3.Interceptor

object MusicNetworkApi : Network("https://api.music.caikun.site/") {

    fun <T> create(api: Class<T>): T {
        return getRetrofit(api).create(api)
    }

    override fun initInterceptor(): Interceptor? {
        return null
    }
}