package site.caikun.music.player

import android.content.Context
import android.text.TextUtils

import android.util.Log
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import site.caikun.music.player.CustomMusicPlayer.Companion.STATE_BUFFERING
import site.caikun.music.player.CustomMusicPlayer.Companion.STATE_ERROR
import site.caikun.music.player.CustomMusicPlayer.Companion.STATE_IDLE
import site.caikun.music.player.CustomMusicPlayer.Companion.STATE_PAUSE
import site.caikun.music.player.CustomMusicPlayer.Companion.STATE_PLAYING
import site.caikun.music.player.CustomMusicPlayer.Companion.STATE_SWITCH
import site.caikun.music.utils.MusicInfo

class ExoMusicPlayer(private val context: Context) : CustomMusicPlayer {

    companion object {
        const val TAG = "KunMusic"
    }

    init {
        createExoPlayer()
    }

    private var player: ExoPlayer? = null
    private var error = false
    private var callback: CustomMusicPlayer.Callback? = null
    private var currentMusicInfo: MusicInfo? = null

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

    /**
     * 播放音乐
     * @param musicInfo
     */
    override fun play(musicInfo: MusicInfo) {
        if (!TextUtils.isEmpty(musicInfo.musicId)) {
            error = false
            val source = musicInfo.musicUrl
            val mediaSource = ExoMediaSource.createMediaSource(context, source)

            player?.apply {
                setMediaSource(mediaSource)
                prepare()
                playWhenReady = true
                Log.d(TAG, "play: $source")
            }
        } else {
            error = true
            callback?.onPlayerError(musicInfo, "播放地址为空")
        }
    }

    /**
     * 开始
     */
    override fun start() {
        if (player?.currentMediaItem != null) {
            Log.d(TAG, "start: ")
            player?.play()
        }
    }

    /**
     * 暂停 继续
     */
    override fun pause() {
        if (!error) {
            if (player?.isPlaying == true) {
                player?.pause()
                callback?.onPlayerStateChanged(currentMusicInfo, STATE_PAUSE)

            } else {
                player?.play()
                callback?.onPlayerStateChanged(currentMusicInfo, STATE_PLAYING)
            }
        }
    }

    /**
     * 停止
     */
    override fun stop() {
        player?.stop()
    }

    /**
     * 设置进度
     */
    override fun seekTo(position: Long) {
        if (position > 0 && position < duration()) {
            player?.seekTo(position)
        }
    }

    /**
     * 获取时长
     * @return 音乐时长 Long
     */
    override fun duration(): Long {
        if (player != null) {
            return if (player!!.duration < 0) 0 else player!!.duration
        }
        return 0
    }

    /**
     * 获取进度
     * @return 播放进度 Long
     */
    override fun position(): Long {
        return if (player != null) player!!.currentPosition else 0
    }

    /**
     * 缓冲进度
     * @return buffer Long
     */
    override fun buffered(): Long {
        return if (player != null) player!!.bufferedPosition else 0
    }

    override fun setCallback(callback: CustomMusicPlayer.Callback?) {
        this.callback = callback
    }

    inner class ExoListener : Player.Listener {

        /**
         * ExoPlayer 播放器状态改变回调
         * 更新播放器状态
         * @param playbackState 状态
         */
        override fun onPlaybackStateChanged(playbackState: Int) {
            var newState = STATE_IDLE
            when (playbackState) {
                Player.STATE_IDLE -> newState = if (error) STATE_ERROR else STATE_IDLE
                Player.STATE_READY -> newState = STATE_PLAYING
                Player.STATE_ENDED -> {
                    callback?.onComplete()
                }
                Player.STATE_BUFFERING -> newState = STATE_BUFFERING
            }
            callback?.onPlayerStateChanged(currentMusicInfo, newState)
        }

        /**
         * ExoPlayer 播放器错误回调
         * @param message 异常信息
         */
        override fun onPlayerError(message: PlaybackException) {
            error = true
            callback?.apply {
                onPlayerError(currentMusicInfo, message.message.toString())
            }
        }
    }
}