package site.caikun.kunmusic.fragment

import android.util.Log
import site.caikun.kunmusic.R
import site.caikun.kunmusic.databinding.FragmentMusicApiBinding
import site.caikun.kunmusic.engine.EngineFragment
import site.caikun.kunmusic.utils.ToastUtil
import site.caikun.music.KunMusic
import site.caikun.music.player.MusicState
import site.caikun.music.utils.MusicInfo

class MusicApiFragment : EngineFragment<FragmentMusicApiBinding>(R.layout.fragment_music_api) {

    override fun init() {

        //点击播放
        binding.playUrl.setOnClickListener {
            val musicInfo = MusicInfo()
            musicInfo.musicId = "441491828"
            KunMusic.with()?.playMusic(musicInfo)
//            if (binding.input.text.isNotEmpty()) {
//                musicInfo.musicId = "123"
//                musicInfo.musicName = "麻雀"
//                musicInfo.musicAuthor = "李荣浩"
//                musicInfo.musicUrl = binding.input.text.toString()
//                musicInfo.musicCover = "https://album.caikun.site/images/OTO.jpg"
//                KunMusic.with()?.playMusic(musicInfo)
//
//                ToastUtil.show(KunMusic.with()?.currentMusicInfo()?.musicUrl.toString())
//            } else {
//                musicInfo.musicId = "441491828"
//                KunMusic.with()?.playMusic(musicInfo)
//            }
        }

        KunMusic.with()?.currentState()?.observe(this) {
            when (it) {
                MusicState.SWITCH, MusicState.PLAYING, MusicState.ERROR ->
                    binding.data = KunMusic.with()?.currentMusicInfo()
            }
            binding.text.text = it
            Log.d(TAG, "state: $it")
        }

        binding.pause.setOnClickListener { KunMusic.with()?.pause() }
        binding.last.setOnClickListener { KunMusic.with()?.skipToLast() }
        binding.next.setOnClickListener { KunMusic.with()?.skipToNext() }
    }
}