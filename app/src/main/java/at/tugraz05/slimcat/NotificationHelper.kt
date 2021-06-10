package at.tugraz05.slimcat

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

class NotificationHelper(base: Context?) : ContextWrapper(base) {
    companion object {
        const val channelID = "channelID"
        const val channelName = "Channel"
        const val VISIBILITY_PRIVATE = 0
    }

    private var nManager: NotificationManager? = null
    @TargetApi(Build.VERSION_CODES.O)
    fun createChannel() {
        val channel = NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_DEFAULT)
        channel.enableLights(true)
        channel.enableVibration(true)
        channel.lightColor = R.color.design_default_color_primary
        channel.lockscreenVisibility = VISIBILITY_PRIVATE
        manager!!.createNotificationChannel(channel)
    }

    val manager: NotificationManager?
        get() {
            if (nManager == null) {
                nManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            }
            return nManager
        }

    fun getChannelNotification(title: String?, message: String?): NotificationCompat.Builder {
        val resultIntent = Intent(this, MainActivity::class.java)
        val resultPendingIntent = PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        return NotificationCompat.Builder(applicationContext, channelID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setAutoCancel(true)
            .setContentIntent(resultPendingIntent)
    }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }
    }
}