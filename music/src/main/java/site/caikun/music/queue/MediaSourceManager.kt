package site.caikun.music.queue

import android.util.Log
import androidx.lifecycle.MutableLiveData
import site.caikun.music.utils.MusicInfo

class MediaSourceManager(private val mediaSourceProvider: MediaSourceProvider) {

    private var musicInfoList = MutableLiveData<List<MusicInfo>>()
    private var index = 0

    companion object{
        const val TAG = "KunMusic"
    }

    /**
     * 设置播放列表
     * @param musicInfoList 音乐列表
     */
    fun setMusicInfoList(musicInfoList: MutableList<MusicInfo>) {
        mediaSourceProvider.clearMusicInfoList()
        mediaSourceProvider.addMusicInfo(musicInfoList)
    }

    /**
     * 当前播放音乐信息
     * @return MusicInfo
     */
    fun currentMusicInfo(): MusicInfo {
        return mediaSourceProvider.musicInfoList[index]
    }

    /**
     * 当前播放列表
     * @return LiveData 包装的音乐列表
     */
    fun currentMusicInfoList(): MutableLiveData<List<MusicInfo>> {
        musicInfoList.postValue(mediaSourceProvider.musicInfoList)
        return musicInfoList
    }

    fun addMusicInfo(musicInfo: MusicInfo) {
        mediaSourceProvider.addMusicInfo(musicInfo)
    }

    fun addMusicInfo(musicInfo: MusicInfo, index: Int) {
        mediaSourceProvider.addMusicInfo(musicInfo, index)
    }

    fun removeMusicInfo(musicInfo: MusicInfo): Boolean {
        return mediaSourceProvider.removeMusicInfo(musicInfo)
    }

    fun clearMusicInfoList() {
        mediaSourceProvider.clearMusicInfoList()
    }

    /**
     * 切换音乐实现
     * @param amount 跳过个数
     */
    fun skipQueue(amount: Int): Boolean {
        val musicSize = mediaSourceProvider.musicInfoList.size
        val position = index + amount

        if (musicSize == 0) return false
        if (musicSize == 1) {
            index = 0
            return true
        }
        if (amount > 1) {
            index = position
            if (musicSize <= index) index %= musicSize
        } else {
            index = position
            if (index == 0) index = musicSize - 1
        }
        return true
    }
}