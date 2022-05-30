package site.caikun.kunmusic.data.bean

import com.google.gson.annotations.SerializedName

data class MusicUrl(
    @SerializedName("id")
    var id: Long,
    @SerializedName("url")
    var url: String?,
    @SerializedName("br")
    var br: Long,
    @SerializedName("size")
    var size: Long,
    @SerializedName("encodeType")
    var encodeType: String?
)