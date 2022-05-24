package site.caikun.music.interceptor

import android.util.Log
import site.caikun.music.utils.MainLooper
import site.caikun.music.utils.MusicInfo
import java.lang.Exception

class InterceptorService {

    companion object {
        const val TAG = "KunMusic"
    }

    private var interceptors = mutableListOf<Pair<MusicInterceptor, String>>()

    fun attachInterceptors(interceptors: List<Pair<MusicInterceptor, String>>) {
        this.interceptors.apply {
            clear()
            addAll(interceptors)
            Log.d(TAG, "attachInterceptors: ${interceptors.size}")
        }
    }

    fun handlerInterceptor(musicInfo: MusicInfo, callback: MusicInterceptorCallback?) {
        if (interceptors.isEmpty()) {
            callback?.onNext(musicInfo)
            Log.d(TAG, "handlerInterceptor: onNext")
        } else {
            runCatching {
                doInterceptor(0, musicInfo, callback)
                Log.d(TAG, "handlerInterceptor: doInterceptor")
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
        if (index <= interceptors.size) {
            val interceptor = interceptors[index].first
            val thread = interceptors[index].second
            if (thread == InterceptorThread.UI) {
                MainLooper.instance.ui {
                    doInterceptorImplementation(interceptor, index, musicInfo, callback)
                    Log.d(TAG, "doInterceptor: ${interceptor.tag()}")
                }
            } else {
                Runnable {
                    doInterceptorImplementation(interceptor, index, musicInfo, callback)
                    Log.d(TAG, "doInterceptor: ${interceptor.tag()}")
                }
            }
        } else {
            MainLooper.instance.ui {
                callback?.onNext(musicInfo)
                Log.d(TAG, "doInterceptor: onNext")
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