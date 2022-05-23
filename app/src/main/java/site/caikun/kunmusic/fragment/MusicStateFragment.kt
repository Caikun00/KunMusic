package site.caikun.kunmusic.fragment

import site.caikun.kunmusic.R
import site.caikun.kunmusic.databinding.FragmentMusicStateBinding
import site.caikun.kunmusic.engine.EngineFragment
import site.caikun.music.KunMusic

class MusicStateFragment :
    EngineFragment<FragmentMusicStateBinding>(R.layout.fragment_music_state) {

    override fun init() {
        KunMusic.with()?.currentState()?.observe(this) {
            binding.state.text = it
        }
    }
}