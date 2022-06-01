package site.caikun.music.player

import android.util.Log
import androidx.lifecycle.MutableLiveData
import site.caikun.music.KunMusic
import site.caikun.music.interceptor.InterceptorService
import site.caikun.music.interceptor.MusicInterceptor
import site.caikun.music.interceptor.MusicInterceptorCallback
import site.caikun.music.listener.OnPlayProgressListener
import site.caikun.music.listener.OnPlayerStatusListener
import site.caikun.music.player.CustomMusicPlayer.Companion.STATE_SWITCH
import site.caikun.music.queue.MediaSourceManager
import site.caikun.music.queue.MediaSourceProvider
import site.caikun.music.utils.MusicInfo
import site.caikun.music.utils.TimerTaskManager

class MusicController(
    interceptors: List<Pair<MusicInterceptor, String>>
) : CustomMusicPlayer.Callback {

    private var player = KunMusic.binder()?.player
    private val interceptorService = InterceptorService()
    private val mediaSourceManager = MediaSourceManager(MediaSourceProvider())
    private val state = MutableLiveData(PlayerStatus.IDLE)

    private var timerTaskManager: TimerTaskManager? = null
    private var isRunningTimerTask = false
    private var onPlayProgressListener: OnPlayProgressListener? = null
    private var onPlayerStatusListener: OnPlayerStatusListener? = null

    companion object {
        const val TAG = "KunMusic"
    }

    init {
        player?.setCallback(this)
        interceptorService.attachInterceptors(interceptors)

        //开始更新进度任务
        timerTaskManager = TimerTaskManager()
        timerTaskManager?.setUpdateProgressTask {
            isRunningTimerTask = true
            if (player != null) {
                val position = player!!.position()
                val duration = player!!.duration()
                val buffered = player!!.buffered()
                onPlayProgressListener?.onPlayProgress(position, duration, buffered)
            }
        }
    }

    fun playMusic(musicInfo: MusicInfo?) {
        if (musicInfo == null) return
        interceptorService.handlerInterceptor(musicInfo, object : MusicInterceptorCallback {
            override fun onNext(musicInfo: MusicInfo?) {
                if (musicInfo == null || musicInfo.musicId.isEmpty()) {
                    onInterrupt("播放地址为空")
                } else {
                    addMusic(musicInfo)
                    player?.play(currentMusicInfo())
                }
            }

            override fun onInterrupt(message: String?) {
                onPlayerError(musicInfo, message.orEmpty())
            }
        })
    }

    fun playMusic(musicInfoList: MutableList<MusicInfo>) {
        if (musicInfoList.isNotEmpty()) {
            setMusicList(musicInfoList)
            playMusic(mediaSourceManager.currentMusicInfo())
        }
    }

    fun playMusicByIndex(index: Int = 0) {
        val musicInfo = mediaSourceManager.musicInfoIndex(index)
        onPlayerStateChanged(musicInfo, STATE_SWITCH)
        playMusic(musicInfo)
        mediaSourceManager.index = index
    }

    fun findMusicInfoIndex(musicInfo: MusicInfo): Int {
        return mediaSourceManager.findMusicInfoIndex(musicInfo)
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

    /**
     * 设置播放进度监听
     * @param listener OnPlayProgressListener
     */
    fun setOnPlayProgressListener(listener: OnPlayProgressListener) {
        onPlayProgressListener = listener
        if (!isRunningTimerTask) {
            timerTaskManager?.startUpdateProgress()
        }
    }

    /**
     * 设置播放器状态监听
     * @param listener OnPlayerStatusListener
     */
    fun setOnPlayerStatusListener(listener: OnPlayerStatusListener) {
        onPlayerStatusListener = listener
    }

    fun pause() = player?.pause()

    fun stop() = player?.stop()

    fun seekTo(position: Long) = player?.seekTo(position)

    fun currentState() = state

    fun currentMusicInfo(): MusicInfo? = mediaSourceManager.currentMusicInfo()

    fun currentMusicInfoList(): MutableLiveData<List<MusicInfo>> {
        return mediaSourceManager.currentMusicInfoList()
    }

    /**
     * 判断是否为当前播放音乐
     * @param musicInfo 传入音乐信息对象
     * @return boolean
     */
    fun isCurrentMusicPlaying(musicInfo: MusicInfo): Boolean {
        return currentMusicInfo()?.musicId == musicInfo.musicId
    }

    fun index() = mediaSourceManager.index

    fun addMusic(musicInfo: MusicInfo) = mediaSourceManager.add(musicInfo)

    fun addMusic(musicInfo: MusicInfo, index: Int) = mediaSourceManager.add(musicInfo, index)

    fun removeMusic(musicInfo: MusicInfo) = mediaSourceManager.remove(musicInfo)

    fun clearMusic() = mediaSourceManager.clear()

    fun setMusicList(musicInfoList: MutableList<MusicInfo>) {
        mediaSourceManager.setMusicInfoList(musicInfoList)
    }

    /**
     * ========================= 播放器回调 =============================
     */

    override fun onPlayerStateChanged(musicInfo: MusicInfo?, state: Int) {
        val s = PlayerStatus.transitionState(state)
        this.state.postValue(s)
        onPlayerStatusListener?.onPlayerStateChange(s)
        Log.d(TAG, "onPlayerStateChanged: ${PlayerStatus.transitionState(state)}")
    }

    override fun onComplete() {
        Log.d(TAG, "onComplete: ")
        skipToNext()
    }

    override fun onSkipToNext() {
        onPlayerStateChanged(currentMusicInfo(), STATE_SWITCH)
        Log.d(TAG, "onSkipToNext: ")
    }

    override fun onSkipToLast() {
        onPlayerStateChanged(currentMusicInfo(), STATE_SWITCH)
        Log.d(TAG, "onSkipToLast: ")
    }

    override fun onPlayerError(musicInfo: MusicInfo?, message: String) {
        Log.d(TAG, "onPlayerError: $message")
    }
}