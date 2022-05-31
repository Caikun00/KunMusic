package site.caikun.kunmusic.utils

import android.util.Log
import site.caikun.kunmusic.data.DataRepository
import site.caikun.kunmusic.data.api.MusicApiService
import site.caikun.kunmusic.data.bean.MusicUrl
import site.caikun.kunmusic.http.MusicNetworkApi
import site.caikun.kunmusic.http.MusicResponseResult
import site.caikun.kunmusic.http.NetworkObserver
import site.caikun.kunmusic.http.SchedulerProvider
import site.caikun.kunmusic.http.error.ResponseException
import site.caikun.music.interceptor.MusicInterceptor
import site.caikun.music.interceptor.MusicInterceptorCallback
import site.caikun.music.utils.MusicInfo

class MusicUrlInterceptor : MusicInterceptor() {

    companion object {
        const val TAG = "KunMusic"
    }

    override fun process(musicInfo: MusicInfo?, callback: MusicInterceptorCallback) {
        if (musicInfo?.musicId?.isEmpty() == true) {
            callback.onInterrupt("音乐ID为空")
            return
        }
        if (musicInfo?.musicUrl != "") {
            callback.onNext(musicInfo)
            return
        }

        //请求播放地址
        MusicNetworkApi.create(MusicApiService::class.java)
            .url(musicInfo.musicId)
            .compose(SchedulerProvider.applySchedulers())
            .subscribe(object : NetworkObserver<MusicResponseResult<List<MusicUrl>>>() {
                override fun onSuccess(data: MusicResponseResult<List<MusicUrl>>) {
                    if (data.code == 200 && data.data?.isNotEmpty() == true) {
                        musicInfo.musicUrl = data.data[0].url.toString()
                        if (musicInfo.musicUrl.isNotEmpty() && musicInfo.musicUrl != "null") {
                            callback.onNext(musicInfo)
                            return
                        }
                    }
                    callback.onInterrupt("拦截器获取播放地址失败")
                }

                override fun onFailure(e: ResponseException) {
                    Log.d(DataRepository.TAG, "onFailure: $e")
                    ToastUtil.show(e.message.toString())
                    callback.onInterrupt("拦截器获取播放地址失败")
                }
            })
    }

    override fun tag(): String {
        return this.javaClass.name
    }
}