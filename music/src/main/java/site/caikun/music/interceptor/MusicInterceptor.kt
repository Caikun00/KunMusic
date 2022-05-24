package site.caikun.music.interceptor

import site.caikun.music.utils.MusicInfo

abstract class MusicInterceptor {

    abstract fun process(musicInfo: MusicInfo?, callback: MusicInterceptorCallback)

    abstract fun tag(): String
}