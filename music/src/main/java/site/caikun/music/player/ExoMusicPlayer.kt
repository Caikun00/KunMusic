package site.caikun.music.player

import android.content.Context
import android.text.TextUtils

import android.util.Log
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import site.caikun.music.utils.MusicInfo

class ExoMusicPlayer(private val context: Context) : CustomMusicPlayer {

    companion object {
        const val TAG = "KunMusic"
    }

    init {
        createExoPlayer()
    }

    private var player: ExoPlayer? = null

    /**
     * 创建播放器
     * @return ExoPlayer
     */
    private fun createExoPlayer() {
        Log.d(TAG, "createExoPlayer: ")

        if (player == null) {
            //创建渲染工厂
            val mode = DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER
            val renderers = DefaultRenderersFactory(context).setExtensionRendererMode(mode)

            //创建轨道
            val trackSelector = DefaultTrackSelector(context)
            trackSelector.parameters = DefaultTrackSelector.ParametersBuilder(context).build()

            //创建播放器
            val exoPlayer = ExoPlayer.Builder(context, renderers)
                .setTrackSelector(trackSelector)
                .setAudioAttributes(AudioAttributes.DEFAULT, true)
                .build()
            exoPlayer.addListener(ExoListener())
            player = exoPlayer
        }

    }

    override fun play(musicInfo: MusicInfo) {
        if (!TextUtils.isEmpty(musicInfo.musicId)) {
            val source = musicInfo.musicUrl
            val mediaSource = ExoMediaSource.createMediaSource(context, source)

            player?.apply {
                setMediaSource(mediaSource)
                prepare()
                playWhenReady = true
                Log.d(TAG, "play: $source")
            }
        }
    }

    override fun start() {
        TODO("Not yet implemented")
    }

    override fun pause() {
        TODO("Not yet implemented")
    }

    override fun stop() {
        TODO("Not yet implemented")
    }

    override fun seekTo(position: Long) {
        TODO("Not yet implemented")
    }

    override fun duration(): Long {
        TODO("Not yet implemented")
    }

    override fun position(): Long {
        TODO("Not yet implemented")
    }

    override fun buffered(): Long {
        TODO("Not yet implemented")
    }

    override fun setCallback(callback: CustomMusicPlayer.Callback?) {
        TODO("Not yet implemented")
    }

    inner class ExoListener : Player.Listener {

    }
}