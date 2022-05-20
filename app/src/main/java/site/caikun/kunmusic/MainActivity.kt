package site.caikun.kunmusic

import site.caikun.kunmusic.databinding.ActivityMainBinding
import site.caikun.music.KunMusic
import site.caikun.music.utils.MusicInfo

class MainActivity : EngineActivity<ActivityMainBinding>(R.layout.activity_main) {

    override fun init() {
        binding.button.setOnClickListener {
            binding.textView.text = KunMusic.binder()?.random()
            val musicInfo = MusicInfo()
            musicInfo.musicId = "123"
            musicInfo.musicUrl = "http://m701.music.126.net/20220521000003/f764c0abde130bea190b2b938226a770/jdymusic/obj/wo3DlMOGwrbDjj7DisKw/14096418064/eabf/4b08/3f90/3a4eeeebf753ddcab37c3143ce69526e.mp3"
            KunMusic.binder()?.player?.play(musicInfo)
        }
    }
}