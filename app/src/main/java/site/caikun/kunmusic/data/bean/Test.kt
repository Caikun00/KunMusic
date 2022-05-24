package site.caikun.kunmusic.data.bean
import com.google.gson.annotations.SerializedName


data class Test(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: List<Data>
) {
    data class Data(
        @SerializedName("br")
        var br: Int,
        @SerializedName("canExtend")
        var canExtend: Boolean,
        @SerializedName("code")
        var code: Int,
        @SerializedName("encodeType")
        var encodeType: String,
        @SerializedName("expi")
        var expi: Int,
        @SerializedName("fee")
        var fee: Int,
        @SerializedName("flag")
        var flag: Int,
        @SerializedName("freeTimeTrialPrivilege")
        var freeTimeTrialPrivilege: FreeTimeTrialPrivilege,
        @SerializedName("freeTrialInfo")
        var freeTrialInfo: Any,
        @SerializedName("freeTrialPrivilege")
        var freeTrialPrivilege: FreeTrialPrivilege,
        @SerializedName("gain")
        var gain: Double,
        @SerializedName("id")
        var id: Int,
        @SerializedName("level")
        var level: String,
        @SerializedName("md5")
        var md5: String,
        @SerializedName("payed")
        var payed: Int,
        @SerializedName("size")
        var size: Int,
        @SerializedName("type")
        var type: String,
        @SerializedName("uf")
        var uf: Any,
        @SerializedName("url")
        var url: String,
        @SerializedName("urlSource")
        var urlSource: Int
    ) {
        data class FreeTimeTrialPrivilege(
            @SerializedName("remainTime")
            var remainTime: Int,
            @SerializedName("resConsumable")
            var resConsumable: Boolean,
            @SerializedName("type")
            var type: Int,
            @SerializedName("userConsumable")
            var userConsumable: Boolean
        )

        data class FreeTrialPrivilege(
            @SerializedName("listenType")
            var listenType: Any,
            @SerializedName("resConsumable")
            var resConsumable: Boolean,
            @SerializedName("userConsumable")
            var userConsumable: Boolean
        )
    }
}