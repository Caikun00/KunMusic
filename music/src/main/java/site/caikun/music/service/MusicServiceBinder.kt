package site.caikun.music.service

import android.content.Context
import android.os.Binder
import android.util.Log
import site.caikun.music.player.ExoMusicPlayer
import kotlin.random.Random

class MusicServiceBinder(private var context: Context) : Binder() {

    companion object {
        const val TAG = "KunMusic"
    }

    init {
        initPlayer()
    }

    var player: ExoMusicPlayer? = null

    private fun initPlayer() {
        player = ExoMusicPlayer(context)
        Log.d(TAG, "initPlayer: ")
    }
}