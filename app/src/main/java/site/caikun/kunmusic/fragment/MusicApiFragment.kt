package site.caikun.kunmusic.fragment

import android.annotation.SuppressLint
import android.util.Log
import android.widget.SeekBar
import site.caikun.kunmusic.R
import site.caikun.kunmusic.databinding.FragmentMusicApiBinding
import site.caikun.kunmusic.engine.EngineFragment
import site.caikun.music.KunMusic
import site.caikun.music.listener.OnPlayProgressListener
import site.caikun.music.player.MusicState
import site.caikun.music.utils.MusicInfo
import site.caikun.music.utils.TimeTransition

class MusicApiFragment : EngineFragment<FragmentMusicApiBinding>(R.layout.fragment_music_api) {

    override fun init() {

        //点击播放
        binding.playUrl.setOnClickListener {
            if (binding.input.text.isNotEmpty()) {
                val musicInfo = MusicInfo()
                musicInfo.musicId = binding.input.text.toString()
                KunMusic.with()?.apply {
                    add(musicInfo)
                    playMusic(musicInfo)
                }
            }
        }

        KunMusic.with()?.currentState()?.observe(this) {
            binding.state.text = it.toString()
            when (it) {
                MusicState.SWITCH, MusicState.PLAYING, MusicState.ERROR ->
                    binding.data = KunMusic.with()?.currentMusicInfo()
            }
            Log.d(TAG, "state: $it")
        }

        binding.pause.setOnClickListener { KunMusic.with()?.pause() }

        binding.add.setOnClickListener {
            val musicInfoList = mutableListOf<MusicInfo>()
            musicInfoList.add(MusicInfo("441491828"))
            musicInfoList.add(MusicInfo("1365898499"))
            musicInfoList.add(MusicInfo("1407551413"))
            KunMusic.with()?.apply {
                playMusic(musicInfoList)
            }
        }

        binding.last.setOnClickListener { KunMusic.with()?.skipToLast() }
        binding.next.setOnClickListener { KunMusic.with()?.skipToNext() }

        KunMusic.with()?.setOnPlayProgressListener(object : OnPlayProgressListener {
            @SuppressLint("SetTextI18n")
            override fun onPlayProgress(position: Long, duration: Long, buffered: Long) {
                binding.seekBar.max = duration.toInt()
                binding.seekBar.progress = position.toInt()
                binding.seekBar.secondaryProgress = buffered.toInt()
                binding.time.text = TimeTransition.millisecondToMinute(position, duration)
            }
        })

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekbar: SeekBar?, position: Int, user: Boolean) {
                if (user) KunMusic.with()?.seekTo(position.toLong())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
    }
}