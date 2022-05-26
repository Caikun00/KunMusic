package site.caikun.music.player

import android.util.Log
import androidx.lifecycle.MutableLiveData
import site.caikun.music.KunMusic
import site.caikun.music.interceptor.InterceptorService
import site.caikun.music.interceptor.MusicInterceptor
import site.caikun.music.interceptor.MusicInterceptorCallback
import site.caikun.music.listener.OnPlayProgressListener
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
    private val state = MutableLiveData(MusicState.IDLE)

    private var timerTaskManager: TimerTaskManager? = null
    private var isRunningTimerTask = false
    private var onPlayProgressListener: OnPlayProgressListener? = null

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
                    add(musicInfo)
                    player?.play(currentMusicInfo())
                }
            }

            override fun onInterrupt(message: String?) {
                onPlayerError(musicInfo, message.orEmpty())
            }
        })
    }

    fun playMusic(musicInfoList: MutableList<MusicInfo>) {
        if (musicInfoList.isNotEmpty()){
            mediaSourceManager.addMusicInfo(musicInfoList)
            playMusic(mediaSourceManager.currentMusicInfo())
        }
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
        onPlayerStateChanged(currentMusicInfo(), CustomMusicPlayer.STATE_SWITCH)
        Log.d(TAG, "onSkipToNext: ")
    }

    override fun onSkipToLast() {
        onPlayerStateChanged(currentMusicInfo(), CustomMusicPlayer.STATE_SWITCH)
        Log.d(TAG, "onSkipToLast: ")
    }

    override fun onPlayerError(musicInfo: MusicInfo?, message: String) {
        Log.d(TAG, "onPlayerError: $message")
    }
}