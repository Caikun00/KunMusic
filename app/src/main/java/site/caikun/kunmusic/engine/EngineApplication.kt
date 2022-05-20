package site.caikun.kunmusic.engine

import android.app.Application
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import site.caikun.music.KunMusic

class EngineApplication : Application(), ViewModelStoreOwner {

    private val applicationViewModelStore by lazy {
        ViewModelStore()
    }

    override fun onCreate() {
        super.onCreate()
        init()
    }

    private fun init() {
        KunMusic.init(this)
    }

    override fun getViewModelStore(): ViewModelStore {
        return applicationViewModelStore
    }
}