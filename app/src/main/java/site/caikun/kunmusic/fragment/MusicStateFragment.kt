package site.caikun.kunmusic.fragment

import android.util.Log
import site.caikun.kunmusic.R
import site.caikun.kunmusic.databinding.FragmentMusicStateBinding
import site.caikun.kunmusic.engine.EngineFragment
import site.caikun.music.KunMusic
import site.caikun.music.player.CustomMusicPlayer
import site.caikun.music.player.MusicState

class MusicStateFragment :
    EngineFragment<FragmentMusicStateBinding>(R.layout.fragment_music_state) {

    override fun init() {
        KunMusic.with()?.currentState()?.observe(this) {
            Log.d(TAG, "init: $it")
            if (it.equals(MusicState.PLAYING)) {
                binding.data = KunMusic.with()?.currentMusicInfo()
            }
        }
    }
}