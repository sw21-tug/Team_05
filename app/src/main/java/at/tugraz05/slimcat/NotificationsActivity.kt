package at.tugraz05.slimcat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView

class NotificationsActivity : AppCompatActivity() {
    private var buttonActionNotification: MenuItem? = null
    private var buttonTimePicker: Button? = null
    var TextTimePicker: TextView? = null
    var TextNotification: TextView? = null
    var TextDays: TextView? = null
    private var checkMonday: CheckBox? = null
    private var checkTuesday: CheckBox? = null
    private var checkWednesday: CheckBox? = null
    private var checkThursday: CheckBox? = null
    private var checkFriday: CheckBox? = null
    private var checkSaturday: CheckBox? = null
    private var checkSunday: CheckBox? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        buttonActionNotification = findViewById(R.id.action_button_notification)
        buttonTimePicker = findViewById(R.id.btn_timePicker)
        TextTimePicker = findViewById(R.id.txt_timePicker)
        TextNotification = findViewById(R.id.txt_notifications)
        TextDays = findViewById(R.id.txt_days)

        setCheckboxes()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val actionBar = supportActionBar
        actionBar!!.title = getString(R.string.title_activity_notifications)

        changeVisibility()
    }

    fun setCheckboxes() {
        val sharedpref = this.getSharedPreferences("userprefs", MODE_PRIVATE)

        checkMonday = findViewById(R.id.chk_Monday)
        checkMonday?.isChecked = sharedpref.getBoolean("notification_monday", false)
        checkMonday?.setOnCheckedChangeListener {_, isChecked ->
            sharedpref.edit().putBoolean("notification_monday", isChecked).apply()
        }

        checkTuesday = findViewById(R.id.chk_Tuesday)
        checkTuesday?.isChecked = sharedpref.getBoolean("notification_tuesday", false)
        checkTuesday?.setOnCheckedChangeListener {_, isChecked ->
            sharedpref.edit().putBoolean("notification_tuesday", isChecked).apply()
        }

        checkWednesday = findViewById(R.id.chk_Wednesday)
        checkWednesday?.isChecked = sharedpref.getBoolean("notification_wednesday", false)
        checkWednesday?.setOnCheckedChangeListener {_, isChecked ->
            sharedpref.edit().putBoolean("notification_wednesday", isChecked).apply()
        }

        checkThursday = findViewById(R.id.chk_Thursday)
        checkThursday?.isChecked = sharedpref.getBoolean("notification_thursday", false)
        checkThursday?.setOnCheckedChangeListener {_, isChecked ->
            sharedpref.edit().putBoolean("notification_thursday", isChecked).apply()
        }

        checkFriday = findViewById(R.id.chk_Friday)
        checkFriday?.isChecked = sharedpref.getBoolean("notification_friday", false)
        checkFriday?.setOnCheckedChangeListener {_, isChecked ->
            sharedpref.edit().putBoolean("notification_friday", isChecked).apply()
        }

        checkSaturday = findViewById(R.id.chk_Saturday)
        checkSaturday?.isChecked = sharedpref.getBoolean("notification_saturday", false)
        checkSaturday?.setOnCheckedChangeListener {_, isChecked ->
            sharedpref.edit().putBoolean("notification_saturday", isChecked).apply()
        }

        checkSunday = findViewById(R.id.chk_Sunday)
        checkSunday?.isChecked = sharedpref.getBoolean("notification_sunday", false)
        checkSunday?.setOnCheckedChangeListener {_, isChecked ->
            sharedpref.edit().putBoolean("notification_sunday", isChecked).apply()
        }
    }

    fun changeVisibility() {
        val sharedpref = this.getSharedPreferences("userprefs", MODE_PRIVATE)
        val getNotifications = sharedpref.getBoolean("notifications", false)

        if(getNotifications) {
            buttonTimePicker?.visibility = View.VISIBLE
            TextTimePicker?.visibility = View.VISIBLE
            TextNotification?.visibility = View.VISIBLE
            TextDays?.visibility = View.VISIBLE
            checkMonday?.visibility = View.VISIBLE
            checkTuesday?.visibility = View.VISIBLE
            checkWednesday?.visibility = View.VISIBLE
            checkThursday?.visibility = View.VISIBLE
            checkFriday?.visibility = View.VISIBLE
            checkSaturday?.visibility = View.VISIBLE
            checkSunday?.visibility = View.VISIBLE
        } else {
            buttonTimePicker?.visibility = View.GONE
            TextTimePicker?.visibility = View.GONE
            TextNotification?.visibility = View.GONE
            TextDays?.visibility = View.GONE
            checkMonday?.visibility = View.GONE
            checkTuesday?.visibility = View.GONE
            checkWednesday?.visibility = View.GONE
            checkThursday?.visibility = View.GONE
            checkFriday?.visibility = View.GONE
            checkSaturday?.visibility = View.GONE
            checkSunday?.visibility = View.GONE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val sharedpref = this.getSharedPreferences("userprefs", MODE_PRIVATE)
        val getNotifications = sharedpref.getBoolean("notifications", false)

        when(item.itemId) {
            R.id.action_button_notification -> {
                if(!getNotifications) {
                    item.setIcon(R.drawable.ic_baseline_notifications_24)
                    sharedpref.edit().putBoolean("notifications", true).apply()
                    changeVisibility()
                } else {
                    item.setIcon(R.drawable.ic_baseline_notifications_off_24)
                    sharedpref.edit().putBoolean("notifications", false).apply()
                    changeVisibility()
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_notifications, menu)
        var item = menu.findItem(R.id.action_button_notification)
        val sharedpref = this.getSharedPreferences("userprefs", MODE_PRIVATE)
        val getNotifications = sharedpref.getBoolean("notifications", false)
        if(getNotifications) {
            item.setIcon(R.drawable.ic_baseline_notifications_24)
        } else {
            item.setIcon(R.drawable.ic_baseline_notifications_off_24)
        }
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}