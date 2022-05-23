package site.caikun.kunmusic.fragment

import android.util.Log
import site.caikun.kunmusic.R
import site.caikun.kunmusic.databinding.FragmentMusicApiBinding
import site.caikun.kunmusic.engine.EngineFragment
import site.caikun.kunmusic.utils.ToastUtil
import site.caikun.music.KunMusic
import site.caikun.music.utils.MusicInfo

class MusicApiFragment : EngineFragment<FragmentMusicApiBinding>(R.layout.fragment_music_api) {

    override fun init() {

        //点击播放
        binding.playUrl.setOnClickListener {
            if (binding.input.text.isNotEmpty()) {
                val musicInfo = MusicInfo()
                musicInfo.musicId = "123"
                musicInfo.musicUrl = binding.input.text.toString()
                musicInfo.musicCover = "https://album.caikun.site/images/OTO.jpg"
                KunMusic.with()?.playMusic(musicInfo)

                ToastUtil.show(KunMusic.with()?.currentMusicInfo()?.musicUrl.toString())
            }
        }

        KunMusic.with()?.currentState()?.observe(this) {
            binding.text.text = it
            Log.d(TAG, "state: $it")
        }

        binding.pause.setOnClickListener { KunMusic.with()?.pause() }
        binding.last.setOnClickListener { KunMusic.with()?.skipToLast() }
        binding.next.setOnClickListener { KunMusic.with()?.skipToNext() }
    }
}