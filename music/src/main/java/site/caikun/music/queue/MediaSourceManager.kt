package site.caikun.music.queue

import androidx.lifecycle.MutableLiveData
import site.caikun.music.utils.MusicInfo

class MediaSourceManager(private val mediaSourceProvider: MediaSourceProvider) {

    var index = 0
    private var musicInfoList = MutableLiveData<List<MusicInfo>>()

    companion object {
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
    fun currentMusicInfo(): MusicInfo? {
        return mediaSourceProvider.musicInfoList.elementAtOrNull(index)
    }

    /**
     * 当前播放列表
     * @return LiveData 包装的音乐列表
     */
    fun currentMusicInfoList(): MutableLiveData<List<MusicInfo>> = musicInfoList

    fun add(musicInfo: MusicInfo) {
        mediaSourceProvider.addMusicInfo(musicInfo)
        updateMusicInfoList()
    }

    fun add(musicInfo: MusicInfo, index: Int) {
        mediaSourceProvider.addMusicInfo(musicInfo, index)
    }

    fun add(musicInfoList: MutableList<MusicInfo>) {
        if (musicInfoList.isNotEmpty()) {
            mediaSourceProvider.addMusicInfo(musicInfoList)
        }
    }

    fun remove(musicInfo: MusicInfo): Boolean {
        val result = mediaSourceProvider.removeMusicInfo(musicInfo)
        updateMusicInfoList()
        return result
    }

    fun clear() {
        mediaSourceProvider.clearMusicInfoList()
        updateMusicInfoList()
    }

    fun musicInfoIndex(index: Int): MusicInfo? {
        val list = getMusicInfoList()
        if (index <= list.size && index >= 0) {
            return list[index]
        }
        return null
    }

    /**
     * 切换音乐实现
     * @param amount 跳过个数
     */
    fun skipQueue(amount: Int): Boolean {
        val musicSize = mediaSourceProvider.musicInfoList.size
        var position = index + amount

        if (musicSize == 0) return false

        //如果列表中只有一个，则重新播放
        if (musicSize == 1) {
            index = 0
            return true
        }
        //上一首
        if (amount > 0) {
            if (position >= musicSize) {
                position = 0
            }
        }
        //下一首
        else if (amount < 0) {
            if (position < 0) {
                position = musicSize - 1
            }
        }
        index = position
        return true
    }

    /**
     * 查找歌曲在列表中下标，如果找到返回下标，未找到-1
     * @param musicInfo MusicInfo
     * @return index
     */
    fun findMusicInfoIndex(musicInfo: MusicInfo): Int {
        var index = 0
        getMusicInfoList().forEach { music ->
            if (musicInfo.musicId == music.musicId) {
                return index
            }
            index++
        }
        return -1
    }

    private fun getMusicInfoList(): MutableList<MusicInfo> {
        return mediaSourceProvider.musicInfoList
    }

    private fun updateMusicInfoList() {
        musicInfoList.postValue(getMusicInfoList())
    }
}