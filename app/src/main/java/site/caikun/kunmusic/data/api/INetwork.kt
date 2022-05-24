package site.caikun.kunmusic.data.api

import androidx.lifecycle.MutableLiveData
import site.caikun.kunmusic.data.bean.MusicUrl

interface INetwork {

    fun musicUrl(id: String, musicUrl: MutableLiveData<MusicUrl>)
}