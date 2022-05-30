package site.caikun.kunmusic.fragment

import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import site.caikun.kunmusic.R
import site.caikun.kunmusic.data.DataRepository
import site.caikun.kunmusic.data.bean.MusicUrl
import site.caikun.kunmusic.databinding.FragmentMusicStateBinding
import site.caikun.kunmusic.engine.EngineFragment

class MusicStateFragment :
    EngineFragment<FragmentMusicStateBinding>(R.layout.fragment_music_state) {

    private val musicUrl = MutableLiveData<MusicUrl>()

    override fun init() {
        musicUrl.observe(this) {
            binding.textView.text = it.url
        }

        binding.button.setOnClickListener {
            DataRepository.musicUrl("441491828", musicUrl)
        }
    }
}