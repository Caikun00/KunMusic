package site.caikun.music.interceptor

import site.caikun.music.utils.MainLooper
import site.caikun.music.utils.MusicInfo

class InterceptorService {

    companion object {
        const val TAG = "KunMusic"
    }

    private var interceptors = mutableListOf<Pair<MusicInterceptor, String>>()

    fun attachInterceptors(interceptors: List<Pair<MusicInterceptor, String>>) {
        this.interceptors.apply {
            clear()
            addAll(interceptors)
        }
    }

    fun handlerInterceptor(musicInfo: MusicInfo, callback: MusicInterceptorCallback?) {
        if (interceptors.isEmpty()) {
            callback?.onNext(musicInfo)
        } else {
            runCatching {
                doInterceptor(0, musicInfo, callback)
            }.onFailure {
                callback?.onInterrupt(it.message)
            }
        }
    }

    private fun doInterceptor(
        index: Int,
        musicInfo: MusicInfo?,
        callback: MusicInterceptorCallback?
    ) {
        if (index < interceptors.size) {
            val interceptor = interceptors[index].first
            val thread = interceptors[index].second

            if (thread == InterceptorThread.UI) {
                MainLooper.instance.ui {
                    doInterceptorImplementation(interceptor, index, musicInfo, callback)
                }
            } else {
                Thread {
                    kotlin.run {
                        doInterceptorImplementation(interceptor, index, musicInfo, callback)
                    }
                }.start()
            }
        } else {
            MainLooper.instance.ui {
                callback?.onNext(musicInfo)
            }
        }
    }

    private fun doInterceptorImplementation(
        interceptor: MusicInterceptor,
        index: Int,
        musicInfo: MusicInfo?,
        callback: MusicInterceptorCallback?
    ) {
        interceptor.process(musicInfo, object : MusicInterceptorCallback {
            override fun onNext(musicInfo: MusicInfo?) {
                doInterceptor(index + 1, musicInfo, callback)
            }

            override fun onInterrupt(message: String?) {
                MainLooper.instance.ui {
                    callback?.onInterrupt(message)
                }
            }
        })
    }
}