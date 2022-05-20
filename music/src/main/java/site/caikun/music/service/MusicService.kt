package site.caikun.music.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import site.caikun.music.utils.MainLooper
import site.caikun.music.utils.NotificationUtil

class MusicService : Service() {

    private var binder: MusicServiceBinder? = null

    override fun onBind(intent: Intent?): IBinder? {
        binder = MusicServiceBinder(this)
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = NotificationUtil.createNoCrashNotification(this)
        MainLooper.instance.ui({
            startForeground(1000, notification)
        }, 3000L)
        return super.onStartCommand(intent, flags, startId)
    }
}