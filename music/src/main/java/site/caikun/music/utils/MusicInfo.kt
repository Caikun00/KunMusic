package site.caikun.music.utils

data class MusicInfo(
    var musicId: String = "",
    var musicName: String = "",
    var musicAuthor: String = "",
    var musicUrl: String = "",
    var musicCover: String = "",
    var musicDuration: Long = -1,
    var musicHeadData: HashMap<String, String>? = hashMapOf()
)
