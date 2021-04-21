package at.tugraz05.slimcat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class AddcatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addcat)

        //click on camera (The image will be automatically saved in a default directory)
        findViewById<ImageButton>(R.id.btn_camera).setOnClickListener {
            val intent = Intent("android.media.action.IMAGE_CAPTURE")
            startActivity(intent)
        }

        // make gender seeker hide female-only options
        var genderSeeker = findViewById<SeekBar>(R.id.seek_gender)
        var femaleSwitches = listOf(
                findViewById<TableRow>(R.id.row_gestation),
                findViewById<TableRow>(R.id.row_lactation),
        )
        genderSeeker.setOnSeekBarChangeListener(GenderSeeker(genderSeeker.progress, femaleSwitches))
        // gender seeker helpers
        findViewById<TextView>(R.id.label_gender_male).setOnClickListener { genderSeeker.progress = 0 }
        findViewById<TextView>(R.id.label_gender_female).setOnClickListener { genderSeeker.progress = 1 }

        //Back-Button
        val actionbar = supportActionBar
        actionbar!!.title = getString(R.string.title_cat_details)
        actionbar.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}

class GenderSeeker(p: Int, private var switches: List<TableRow>) : SeekBar.OnSeekBarChangeListener {
    init {
        updateSwitches(p)
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        Log.d("SeekBar", "%d".format(progress))
        updateSwitches(progress)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }

    private fun updateSwitches(progress: Int) {
        val visible = if (progress == 1) View.VISIBLE else View.GONE
        switches.forEach { it.visibility = visible }
    }
}
