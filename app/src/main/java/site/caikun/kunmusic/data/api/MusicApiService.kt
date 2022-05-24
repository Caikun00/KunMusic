package site.caikun.kunmusic.data.api

import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import site.caikun.kunmusic.data.bean.MusicUrl
import site.caikun.kunmusic.http.MusicResponseResult

interface MusicApiService {

    @GET("/song/url")
    fun url(@Query("id") id: String): Observable<MusicResponseResult<List<MusicUrl>>>
}