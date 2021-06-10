package at.tugraz05.slimcat

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class AlertReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (showNotification(context)) {
            val nHelper = NotificationHelper(context)
            val nb = nHelper.getChannelNotification(
                message = "Please don't forget to feed your cat",
                title = "Feeding reminder"
            )
            nHelper.manager!!.notify(1, nb.build())
        }
    }

    fun showNotification(context: Context): Boolean {
        val calendar: Calendar = Calendar.getInstance()
        val dayOfWeek: Int = calendar.get(Calendar.DAY_OF_WEEK)

        val sharedpref = context.getSharedPreferences("userprefs", AppCompatActivity.MODE_PRIVATE)

        when (dayOfWeek) {
            Calendar.MONDAY -> {
                return sharedpref.getBoolean("notification_monday", false)
            }
            Calendar.TUESDAY -> {
                return sharedpref.getBoolean("notification_tuesday", false)
            }
            Calendar.WEDNESDAY -> {
                return sharedpref.getBoolean("notification_wednesday", false)
            }
            Calendar.THURSDAY -> {
                return sharedpref.getBoolean("notification_thursday", false)
            }
            Calendar.FRIDAY -> {
                return sharedpref.getBoolean("notification_friday", false)
            }
            Calendar.SATURDAY -> {
                return sharedpref.getBoolean("notification_saturday", false)
            }
            Calendar.SUNDAY -> {
                return sharedpref.getBoolean("notification_sunday", false)
            }
        }
        return false
    }
}