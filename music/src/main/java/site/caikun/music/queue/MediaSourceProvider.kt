package site.caikun.music.queue

import android.util.Log
import site.caikun.music.utils.MusicInfo

class MediaSourceProvider {

    companion object {
        const val TAG = "KunMusic"
    }

    private var sources = linkedMapOf<String, MusicInfo>()

    var musicInfoList: MutableList<MusicInfo>
        get() {
            val list = mutableListOf<MusicInfo>()
            sources.forEach {
                list.add(it.value)
            }
            return list
        }
        set(value) {
            sources.clear()
            value.forEach {
                sources[it.musicId] = it
            }
        }

    fun addMusicInfo(musicInfo: MusicInfo) {
        if (!added(musicInfo.musicId)) {
            sources[musicInfo.musicId] = musicInfo
            Log.d(TAG, "addMusicInfo: ${musicInfo.musicId}")
        }
    }

    fun addMusicInfo(musicInfo: MusicInfo, index: Int) {
        if (!added(musicInfo.musicId)) {
            val list = mutableListOf<Pair<String, MusicInfo>>()
            sources.forEach {
                list.add(Pair(it.key, it.value))
            }
            if (index.isIndexEnabled(list)) {
                list.add(index, Pair(musicInfo.musicId, musicInfo))
            }
            sources.clear()
            list.forEach {
                sources[it.first] = it.second
            }
        }
    }

    fun addMusicInfo(musicInfoList: MutableList<MusicInfo>) {
        musicInfoList.forEach { addMusicInfo(it) }
    }

    fun removeMusicInfo(musicInfo: MusicInfo): Boolean {
        if (!added(musicInfo.musicId)) {
            sources.remove(musicInfo.musicId)
            return true
        }
        return false
    }

    fun clearMusicInfoList() {
        sources.clear()
        musicInfoList.clear()
    }

    /**
     * 根据id判断是否已添加
     * @param musicId id
     */
    private fun added(musicId: String): Boolean {
        return sources.containsKey(musicId)
    }

    private fun <T> Int.isIndexEnabled(list: List<T>?): Boolean {
        return list != null && this >= 0 && this < list.size
    }
}