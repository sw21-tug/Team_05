package at.tugraz05.slimcat

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class AlertReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val nHelper = NotificationHelper(context)
        val nb = nHelper.getChannelNotification(
            message = "Please don't forget to feed your cat",
            title = "Feeding reminder"
        )
        nHelper.manager!!.notify(1, nb.build())

    }
}