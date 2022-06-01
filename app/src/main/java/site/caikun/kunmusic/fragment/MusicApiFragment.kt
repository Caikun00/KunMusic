package site.caikun.kunmusic.fragment

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import com.drake.brv.utils.linear
import com.drake.brv.utils.models
import com.drake.brv.utils.setup
import site.caikun.kunmusic.R
import site.caikun.kunmusic.data.MusicApiViewModel
import site.caikun.kunmusic.databinding.FragmentMusicApiBinding
import site.caikun.kunmusic.engine.EngineFragment
import site.caikun.kunmusic.utils.ToastUtil
import site.caikun.music.KunMusic
import site.caikun.music.listener.OnPlayProgressListener
import site.caikun.music.player.PlayerStatus
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
                PlayerStatus.SWITCH, PlayerStatus.ERROR ->
                    binding.data = KunMusic.with()?.currentMusicInfo()
            }
            Log.d(TAG, "state: $it")
        }

        binding.pause.setOnClickListener { KunMusic.with()?.pause() }

        binding.add.setOnClickListener {
            KunMusic.with()?.apply {
                setMusicList(musicData())
                playMusicByIndex()
            }
        }

        binding.last.setOnClickListener { KunMusic.with()?.skipToLast() }
        binding.next.setOnClickListener { KunMusic.with()?.skipToNext() }

        binding.local.setOnClickListener {
            val path = "rawresource:///" + R.raw.local
            val music = MusicInfo(
                "558290126",
                "爱你",
                "王心凌",
                path,
                "https://bkimg.cdn.bcebos.com/pic/78310a55b319ebc4b74599628b72d8fc1e178b82b8b6?x-bce-process=image/watermark,image_d2F0ZXIvYmFpa2U4MA==,g_7,xp_5,yp_5/format,f_auto"
            )
            KunMusic.with()?.apply {
                playMusic(music)
            }
        }

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
                val data = getModel<MusicInfo>()
                findView<TextView>(R.id.number).text = modelPosition.toString()
                findView<TextView>(R.id.id).text = data.musicId
                findView<TextView>(R.id.active).visibility = View.GONE
                val info = KunMusic.with()?.currentMusicInfo()
                if (info?.musicId == data.musicId) {
                    findView<TextView>(R.id.active).visibility = View.VISIBLE
                }
            }
            onClick(R.id.id) {
                val info = getModel<MusicInfo>(modelPosition)
                val index = KunMusic.with()?.findMusicInfoIndex(info)
                ToastUtil.show("${info.musicId},$index")
            }
            onClick(R.id.root) {
                KunMusic.with()?.playMusicByIndex(modelPosition)
            }
        }
        KunMusic.with()?.currentMusicInfoList()?.observe(this) {
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