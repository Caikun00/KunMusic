package site.caikun.kunmusic.engine

import android.app.Application
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import site.caikun.kunmusic.utils.MusicUrlInterceptor
import site.caikun.kunmusic.utils.ToastUtil
import site.caikun.music.KunMusic
import site.caikun.music.interceptor.InterceptorService
import site.caikun.music.interceptor.InterceptorThread

class EngineApplication : Application(), ViewModelStoreOwner {

    private val applicationViewModelStore by lazy {
        ViewModelStore()
    }

    override fun onCreate() {
        super.onCreate()
        init()
    }

    private fun init() {
        KunMusic.apply {
            addInterceptor(MusicUrlInterceptor(), InterceptorThread.IO)
            init(this@EngineApplication)
        }
        ToastUtil.init(this)
    }

    override fun getViewModelStore(): ViewModelStore {
        return applicationViewModelStore
    }
}