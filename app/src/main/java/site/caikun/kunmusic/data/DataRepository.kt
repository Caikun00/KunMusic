package site.caikun.kunmusic.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import site.caikun.kunmusic.data.api.IDatabase
import site.caikun.kunmusic.data.api.ILocal
import site.caikun.kunmusic.data.api.INetwork
import site.caikun.kunmusic.data.api.MusicApiService
import site.caikun.kunmusic.data.bean.MusicUrl
import site.caikun.kunmusic.http.MusicNetworkApi
import site.caikun.kunmusic.http.MusicResponseResult
import site.caikun.kunmusic.http.NetworkObserver
import site.caikun.kunmusic.http.SchedulerProvider
import site.caikun.kunmusic.http.error.ResponseException
import site.caikun.kunmusic.utils.ToastUtil

object DataRepository : IDatabase, INetwork, ILocal {

    const val TAG = "DataRepository"

    override fun musicUrl(id: String, musicUrl: MutableLiveData<MusicUrl>) {
        MusicNetworkApi.create(MusicApiService::class.java)
            .url(id)
            .compose(SchedulerProvider.applySchedulers())
            .subscribe(object : NetworkObserver<MusicResponseResult<List<MusicUrl>>>() {
                override fun onSuccess(data: MusicResponseResult<List<MusicUrl>>) {
                    if (data.code == 200) {
                        musicUrl.postValue(data.data?.get(0))
                    }
                }

                override fun onFailure(e: ResponseException) {
                    Log.d(TAG, "onFailure: $e")
                    ToastUtil.show(e.message.toString())
                }
            })
    }
}