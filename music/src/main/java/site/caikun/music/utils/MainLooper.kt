package site.caikun.music.utils

import android.os.Handler
import android.os.Looper

class MainLooper(looper: Looper) : Handler(looper) {

    companion object {
        val instance = MainLooper(Looper.getMainLooper())
    }

    fun ui(runnable: Runnable) {
        if (isMainThread()) {
            runnable.run()
        } else instance.post(runnable)
    }

    fun ui(runnable: Runnable, delay: Long) {
        instance.postDelayed(runnable, delay)
    }

    private fun isMainThread() = Looper.myLooper() == Looper.getMainLooper()
}