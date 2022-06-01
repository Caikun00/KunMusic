package site.caikun.kunmusic.fragment

import android.util.Log
import androidx.lifecycle.MutableLiveData
import site.caikun.kunmusic.R
import site.caikun.kunmusic.data.DataRepository
import site.caikun.kunmusic.data.bean.MusicUrl
import site.caikun.kunmusic.databinding.FragmentMusicStateBinding
import site.caikun.kunmusic.engine.EngineFragment
import site.caikun.kunmusic.utils.ToastUtil
import site.caikun.music.KunMusic
import site.caikun.music.listener.OnPlayerStatusListener

class MusicStateFragment :
    EngineFragment<FragmentMusicStateBinding>(R.layout.fragment_music_state) {

    private val musicUrl = MutableLiveData<MusicUrl>()

    override fun init() {
        musicUrl.observe(this) {
            ToastUtil.show(this.musicUrl.toString())
        }

        binding.button.setOnClickListener {
            DataRepository.musicUrl("441491828", musicUrl)
        }

        KunMusic.with()?.setOnPlayerStatusListener(object : OnPlayerStatusListener {
            override fun onPlayerStateChange(state: String) {
                binding.textView.text = state
                Log.d(TAG, "onPlayerStateChange: $state")
            }
        })
    }
}