package site.caikun.music

import android.annotation.SuppressLint
import android.app.Application
import android.content.*
import android.os.IBinder
import android.util.Log
import site.caikun.music.player.MusicController
import site.caikun.music.service.MusicService
import site.caikun.music.service.MusicServiceBinder
import site.caikun.music.utils.ServiceToken
import java.lang.Exception
import java.util.*

object KunMusic {

    private var application: Application? = null

    private var isBindService: Boolean = false
    private var connectionMap = WeakHashMap<Context, ServiceConnection>()
    private var retryLineService = 0
    private var controller: MusicController? = null
    private var connection: ServiceConnection? = null

    @SuppressLint("StaticFieldLeak")
    private var serviceToken: ServiceToken? = null

    @SuppressLint("StaticFieldLeak")
    private var binder: MusicServiceBinder? = null

    private var TAG = "KunMusic"

    fun init(application: Application) {
        this.application = application;
        bindService()
    }

    fun with(): MusicController? {
        return controller
    }

    /**
     * 绑定服务
     */
    private fun bindService() {
        if (isBindService || application == null) return

        val contextWrapper = ContextWrapper(application)
        val intent = Intent(contextWrapper, MusicService::class.java)
        contextWrapper.startForegroundService(intent)

        val result = contextWrapper.bindService(
            intent,
            serviceConnection,
            Context.BIND_AUTO_CREATE
        )
        if (result) {
            connectionMap[contextWrapper] = serviceConnection
            serviceToken = ServiceToken(contextWrapper)
            Log.d(TAG, "bindService: ")
        }
    }

    /**
     * 解绑服务
     */
    private fun unBindService() {
        if (serviceToken == null || !isBindService) return

        val contextWrapper = serviceToken?.contextWrapper
        val connection = connectionMap.getOrDefault(contextWrapper, null)

        connection?.let {
            contextWrapper?.unbindService(connection)
            val intent = Intent(contextWrapper, MusicService::class.java)
            contextWrapper?.stopService(intent)
            isBindService = false
        }
        Log.d(TAG, "unBindService: ")
    }

    private val serviceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            try {
                if (service is MusicServiceBinder) {
                    retryLineService = 0
                    binder = service
                    isBindService = true
                    controller = MusicController()
                    connection?.onServiceConnected(name, service)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            Log.d(TAG, "onServiceConnected: ")
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBindService = false
            connection?.onServiceDisconnected(name)
            if (retryLineService > 3) {
                retryLineService++
                bindService()
            }
            if (connectionMap.isEmpty()) binder = null
            Log.d(TAG, "onServiceDisconnected: ")
        }
    }

    fun binder() = binder
}