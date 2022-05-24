package site.caikun.music.listener

import site.caikun.music.player.MusicState

interface OnPlayerStateListener {

    fun onPlayerStateChange(state: MusicState)
}