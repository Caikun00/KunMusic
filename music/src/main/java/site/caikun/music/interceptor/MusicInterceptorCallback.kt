package site.caikun.music.interceptor

import site.caikun.music.utils.MusicInfo

interface MusicInterceptorCallback {

    fun onNext(musicInfo: MusicInfo?)

    fun onInterrupt(message: String?)
}