package site.caikun.music.player

import site.caikun.music.utils.MusicInfo

interface CustomMusicPlayer {

    companion object {

        const val STATE_IDLE = 1          //空闲
        const val STATE_PLAYING = 2       //播放
        const val STATE_PAUSE = 3         //暂停
        const val STATE_ERROR = 4         //出错
        const val STATE_BUFFERING = 6     //缓冲
    }

    fun play(musicInfo: MusicInfo)

    fun start()

    fun pause()

    fun stop()

    fun seekTo(position: Long)

    fun duration(): Long

    fun position(): Long

    fun buffered(): Long

    fun setCallback(callback: Callback?)

    interface Callback {

        fun onPlayerStateChanged(musicInfo: MusicInfo?, state: Int)

        fun onComplete()

        fun onSkipToNext()

        fun onSkipToLast()

        fun onPlayerError(musicInfo: MusicInfo?, message: String)
    }
}