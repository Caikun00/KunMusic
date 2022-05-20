package site.caikun.music.player

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.util.Util
import java.lang.IllegalStateException

class ExoMediaSource {

    companion object {

        private fun buildDataSourceFactory(context: Context): DataSource.Factory {
            return DefaultDataSource.Factory(context, DefaultHttpDataSource.Factory())
        }

        fun createMediaSource(context: Context, source: String): MediaSource {
            val uri = Uri.parse(source)
            val factory = buildDataSourceFactory(context)
            return when (Util.inferContentType(uri, null)) {
                C.TYPE_DASH -> DashMediaSource.Factory(factory)
                    .createMediaSource(MediaItem.fromUri(uri))
                C.TYPE_SS -> SsMediaSource.Factory(factory)
                    .createMediaSource(MediaItem.fromUri(uri))
                C.TYPE_OTHER -> ProgressiveMediaSource.Factory(factory)
                    .createMediaSource(MediaItem.fromUri(uri))
                else -> {
                    val type = Util.inferContentType(uri, null)
                    throw IllegalStateException("不支持类型:$type")
                }
            }
        }
    }
}