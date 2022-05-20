package site.caikun.kunmusic

import android.app.Application
import site.caikun.music.KunMusic

class App :Application() {

    override fun onCreate() {
        super.onCreate()
        KunMusic.init(this)
    }
}