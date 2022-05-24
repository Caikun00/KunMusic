package site.caikun.kunmusic.http

import com.google.gson.annotations.SerializedName

open class MusicResponseResult<T> {

    @SerializedName("data")
    val data: T? = null

    @SerializedName("code")
    val code: Int? = null
}
