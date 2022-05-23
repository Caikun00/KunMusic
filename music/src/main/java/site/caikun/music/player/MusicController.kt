package site.caikun.music.player

import android.util.Log
import androidx.lifecycle.MutableLiveData
import site.caikun.music.KunMusic
import site.caikun.music.queue.MediaSourceManager
import site.caikun.music.queue.MediaSourceProvider
import site.caikun.music.utils.MusicInfo

class MusicController : CustomMusicPlayer.Callback {

    private var player = KunMusic.binder()?.player

    companion object {
        const val TAG = "KunMusic"
    }

    init {
        player?.setCallback(this)
    }

    private val mediaSourceManager = MediaSourceManager(MediaSourceProvider())
    private val state = MutableLiveData<String>(MusicState.IDLE)

    fun playMusic(musicInfo: MusicInfo) {
        add(musicInfo)
        player?.play(currentMusicInfo())
    }

    /**
     * 上一首
     */
    fun skipToLast() {
        if (mediaSourceManager.skipQueue(-1)) {
            onSkipToLast()
            playMusic(currentMusicInfo())
        }
    }

    /**
     * 下一首
     */
    fun skipToNext() {
        if (mediaSourceManager.skipQueue(1)) {
            onSkipToNext()
            playMusic(currentMusicInfo())
        }
    }

    fun pause() = player?.pause()

    fun stop() = player?.stop()

    fun seekTo(position: Long) = player?.seekTo(position)

    fun currentState() = state

    fun currentMusicInfo(): MusicInfo = mediaSourceManager.currentMusicInfo()

    fun currentMusicInfoList(): MutableLiveData<List<MusicInfo>> {
        return mediaSourceManager.currentMusicInfoList()
    }

    /**
     * 判断是否为当前播放音乐
     * @param musicInfo 传入音乐信息对象
     * @return boolean
     */
    fun isCurrentMusicPlaying(musicInfo: MusicInfo): Boolean {
        return currentMusicInfo().musicId == musicInfo.musicId
    }

    fun add(musicInfo: MusicInfo) = mediaSourceManager.addMusicInfo(musicInfo)

    fun add(musicInfo: MusicInfo, index: Int) = mediaSourceManager.addMusicInfo(musicInfo, index)

    fun remove(musicInfo: MusicInfo) = mediaSourceManager.removeMusicInfo(musicInfo)

    fun clear() = mediaSourceManager.clearMusicInfoList()

    fun setMusicList(musicInfoList: MutableList<MusicInfo>) {
        mediaSourceManager.setMusicInfoList(musicInfoList)
    }

    /**
     * ========================= 播放器回调 =============================
     */

    override fun onPlayerStateChanged(musicInfo: MusicInfo?, state: Int) {
        this.state.postValue(MusicState.transitionState(state))
        Log.d(TAG, "onPlayerStateChanged: ${MusicState.transitionState(state)}")
    }

    override fun onComplete() {
        Log.d(TAG, "onComplete: ")
        skipToNext()
    }

    override fun onSkipToNext() {
        onPlayerStateChanged(currentMusicInfo(),CustomMusicPlayer.STATE_SWITCH)
        Log.d(TAG, "onSkipToNext: ")
    }

    override fun onSkipToLast() {
        onPlayerStateChanged(currentMusicInfo(),CustomMusicPlayer.STATE_SWITCH)
        Log.d(TAG, "onSkipToLast: ")
    }

    override fun onPlayerError(musicInfo: MusicInfo?, message: String) {
        Log.d(TAG, "onPlayerError: $message")
    }
}