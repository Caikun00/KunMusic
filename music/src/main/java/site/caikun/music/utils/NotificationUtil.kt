package site.caikun.music.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.net.Uri
import androidx.core.app.NotificationCompat
import site.caikun.music.R

class NotificationUtil {

    companion object {
        private const val CHANNEL_ID = "music_default_notification"
        private const val NOTIFICATION_TITLE = "Music"
        private const val NOTIFICATION_TEXT = "this is a music player notification"

        /**
         * 创建通知通道
         */
        private fun createNotificationChannel(manager: NotificationManager) {
            if (manager.getNotificationChannel(CHANNEL_ID) == null) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    NOTIFICATION_TEXT,
                    NotificationManager.IMPORTANCE_LOW
                )
                manager.createNotificationChannel(channel)
            }
        }

        /**
         * 创建前台服务防崩溃通知
         */
        fun createNoCrashNotification(context: Context): Notification {
            val manager = context.getSystemService(Service.NOTIFICATION_SERVICE)
            createNotificationChannel(manager as NotificationManager)

            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            notification.apply {
                this.setVibrate(longArrayOf(0L))
                this.setSound(null as Uri?)
                this.setDefaults(0)
                this.setContentTitle(NOTIFICATION_TITLE)
                this.setContentText(NOTIFICATION_TEXT)
                this.setSmallIcon(R.drawable.ic_baseline_music)
            }
            return notification.build()
        }
    }
}