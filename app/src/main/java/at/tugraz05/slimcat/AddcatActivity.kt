package at.tugraz05.slimcat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AddcatActivity : AppCompatActivity() {
    lateinit var seeker: GenderSeeker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addcat)

        //click on camera (The image will be automatically saved in a default directory)
        findViewById<ImageButton>(R.id.btn_camera).setOnClickListener {
            val intent = Intent("android.media.action.IMAGE_CAPTURE")
            startActivity(intent)
        }

        // make gender seeker hide female-only options
        val genderSeeker = findViewById<SeekBar>(R.id.seek_gender)
        val femaleSwitches = listOf<TableRow>(
                findViewById(R.id.row_gestation),
                findViewById(R.id.row_lactation),
        )
        seeker = GenderSeeker(genderSeeker.progress, femaleSwitches)
        genderSeeker.setOnSeekBarChangeListener(seeker)
        // gender seeker helpers
        findViewById<TextView>(R.id.label_gender_male).setOnClickListener { genderSeeker.progress = GenderSeeker.MALE }
        findViewById<TextView>(R.id.label_gender_female).setOnClickListener { genderSeeker.progress = GenderSeeker.FEMALE }

        //Back-Button
        val actionbar = supportActionBar
        actionbar?.title = getString(R.string.title_cat_details)
        actionbar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun setFemale(on: Boolean = true) {
        seeker.updateSwitches(if (on) 1 else 0)
    }
}

class GenderSeeker(p: Int, private var switches: List<TableRow>) : SeekBar.OnSeekBarChangeListener {
    companion object {
        const val MALE = 0
        const val FEMALE = 1
    }
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

    fun updateSwitches(progress: Int) {
        val visible = if (progress == FEMALE) View.VISIBLE else View.GONE
        switches.forEach { it.visibility = visible }
    }
}
