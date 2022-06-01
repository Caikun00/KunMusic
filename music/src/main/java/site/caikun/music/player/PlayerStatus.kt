package site.caikun.music.player

/**
 * 播放器所有状态
 */
class PlayerStatus {

    companion object {
        const val IDLE = "IDLE"
        const val PLAYING = "PLAYING"
        const val SWITCH = "SWITCH"
        const val PAUSE = "PAUSE"
        const val BUFFERING = "BUFFERING"
        const val ERROR = "ERROR"

        fun transitionState(state: Int): String {
            return when (state) {
                CustomMusicPlayer.STATE_IDLE -> IDLE
                CustomMusicPlayer.STATE_BUFFERING -> BUFFERING
                CustomMusicPlayer.STATE_PLAYING -> PLAYING
                CustomMusicPlayer.STATE_PAUSE -> PAUSE
                CustomMusicPlayer.STATE_ERROR -> ERROR
                CustomMusicPlayer.STATE_SWITCH -> SWITCH
                else -> IDLE
            }
        }
    }
}