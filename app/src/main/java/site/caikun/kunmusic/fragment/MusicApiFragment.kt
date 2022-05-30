package site.caikun.kunmusic.fragment

import android.annotation.SuppressLint
import android.util.Log
import android.widget.SeekBar
import android.widget.TextView
import com.drake.brv.utils.addModels
import com.drake.brv.utils.linear
import com.drake.brv.utils.models
import com.drake.brv.utils.setup
import org.w3c.dom.Text
import site.caikun.kunmusic.R
import site.caikun.kunmusic.data.MusicApiViewModel
import site.caikun.kunmusic.databinding.FragmentMusicApiBinding
import site.caikun.kunmusic.engine.EngineFragment
import site.caikun.music.KunMusic
import site.caikun.music.listener.OnPlayProgressListener
import site.caikun.music.player.MusicState
import site.caikun.music.utils.MusicInfo
import site.caikun.music.utils.TimeTransition

class MusicApiFragment : EngineFragment<FragmentMusicApiBinding>(R.layout.fragment_music_api) {

    private val viewModel: MusicApiViewModel by lazy {
        fragmentScopeViewModel(MusicApiViewModel::class.java)
    }

    override fun init() {

        //点击播放
        binding.playUrl.setOnClickListener {
            if (binding.input.text.isNotEmpty()) {
                val musicInfo = MusicInfo()
                musicInfo.musicId = binding.input.text.toString()
                KunMusic.with()?.apply {
                    playMusic(musicInfo)
                }
            }
        }

        KunMusic.with()?.currentState()?.observe(this) {
            binding.state.text = it.toString()
            when (it) {
                MusicState.SWITCH, MusicState.ERROR ->
                    binding.data = KunMusic.with()?.currentMusicInfo()
            }
            Log.d(TAG, "state: $it")
        }

        binding.pause.setOnClickListener { KunMusic.with()?.pause() }

        binding.add.setOnClickListener {
            KunMusic.with()?.apply {
                playMusic(musicData())
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

        setRecycler()
    }

    private fun setRecycler() {
        binding.musicRecycler.linear().setup {
            addType<MusicInfo>(R.layout.item_music_layout)
            onBind {
                findView<TextView>(R.id.number).text = modelPosition.toString()
                findView<TextView>(R.id.id).text = getModel<MusicInfo>().musicId.toString()
            }
        }
        KunMusic.with()?.currentMusicInfoList()?.observe(this) {
            Log.d("KunMusic", "setRecycler: ${it.size}")
            binding.musicRecycler.models = it

        }
    }

    private fun musicData(): MutableList<MusicInfo> {
        val music: MutableList<MusicInfo> = ArrayList()
        music.apply {
            add(
                MusicInfo(
                    "441491828",
                    "水星记",
                    "郭顶",
                    "",
                    "https://bkimg.cdn.bcebos.com/pic/6c224f4a20a44623ef24dc499122720e0cf3d72b?x-bce-process=image/watermark,image_d2F0ZXIvYmFpa2U5Mg==,g_7,xp_5,yp_5/format,f_auto"
                )
            )
            add(
                MusicInfo(
                    "1365898499",
                    "失眠飞行",
                    "沈以诚,薛黛霏",
                    "",
                    "https://bkimg.cdn.bcebos.com/pic/503d269759ee3d6d5dfbac0b4d166d224f4ade34?x-bce-process=image/watermark,image_d2F0ZXIvYmFpa2U4MA==,g_7,xp_5,yp_5/format,f_auto"
                )
            )
            add(
                MusicInfo(
                    "1407551413",
                    "麻雀",
                    "李荣浩",
                    "",
                    "https://bkimg.cdn.bcebos.com/pic/8435e5dde71190ef76c6318615528a16fdfaaf51ca1a?x-bce-process=image/watermark,image_d2F0ZXIvYmFpa2UyNzI=,g_7,xp_5,yp_5/format,f_auto"
                )
            )
        }
        return music
    }
}