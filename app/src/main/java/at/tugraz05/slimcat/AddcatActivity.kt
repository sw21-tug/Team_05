package at.tugraz05.slimcat

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*

class AddcatActivity : AppCompatActivity() {
    lateinit var scrollView: ScrollView
    lateinit var seeker: GenderSeeker
    lateinit var nameField: EditText
    lateinit var raceField: EditText
    // lateinit var ageField: EditText
    lateinit var sizeField: EditText
    lateinit var weightField: EditText
    lateinit var genderSeeker: SeekBar

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addcat)

        //click on camera (The image will be automatically saved in a default directory)
        findViewById<ImageButton>(R.id.btn_camera).setOnClickListener {
            val intent = Intent("android.media.action.IMAGE_CAPTURE")
            startActivity(intent)
        }

        // initialize all fields
        scrollView = findViewById(R.id.main_scroll_view)
        nameField = findViewById(R.id.txt_name)
        raceField = findViewById(R.id.txt_race)
        // TODO: exchange after addCatActivity rework calculate age
        // ageField = findViewById(R.id.txt_age)
        sizeField = findViewById(R.id.txt_size)
        weightField = findViewById(R.id.txt_weight)
        genderSeeker = findViewById(R.id.seek_gender)

        findViewById<Button>(R.id.btn_save).setOnClickListener {
            if (TextUtils.isEmpty(nameField.text)) {
                nameField.error = resources.getString(R.string.error_create_cat)
                scrollView.fullScroll(ScrollView.FOCUS_UP)
            }
            else {
                createCat()
                finish()
            }
        }

        findViewById<Button>(R.id.btn_delete).setOnClickListener {
            if (TextUtils.isEmpty(nameField.text)) {
                nameField.error = resources.getString(R.string.error_delete_cat)
                scrollView.fullScroll(ScrollView.FOCUS_UP)
            }
            else {
                deleteCat()
                finish()
            }
        }

        //click on btn_dob to open the datepicker
        val formatDate = SimpleDateFormat("dd MMMM yyyy", Locale.US)

        findViewById<Button>(R.id.btn_dob).setOnClickListener {
            val getDate : Calendar = Calendar.getInstance()
            val datepicker = DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, DatePickerDialog.OnDateSetListener{ datePicker, year, month, day ->

                val selectDate = Calendar.getInstance()
                selectDate.set(Calendar.YEAR, year)
                selectDate.set(Calendar.MONTH, month)
                selectDate.set(Calendar.DAY_OF_MONTH, day)
                val date = formatDate.format((selectDate.time))
                Toast.makeText(this, "Date : " + date, Toast.LENGTH_SHORT).show()
                findViewById<TextView>(R.id.txt_dob).text = date

            }, getDate.get(Calendar.YEAR), getDate.get(Calendar.MONTH), getDate.get(Calendar.DAY_OF_MONTH))
            datepicker.show()

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
        actionbar!!.title = getString(R.string.title_cat_details)
        actionbar.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun setFemale(on: Boolean = true) {
        seeker.updateSwitches(if (on) 1 else 0)
    }

    private fun createCat() {
        val catName: String = nameField.text.toString()
        val catRace: String = raceField.text.toString()
        // val catAge: Int = if (ageField.text.toString() != "") ageField.text.toString().toInt() else 0
        val catSize: Int = if (sizeField.text.toString()!= "") sizeField.text.toString().toInt() else 0
        val catWeight: Double = if (weightField.text.toString() != "") weightField.text.toString().toDouble() else 0.0
        val genderValue:Int = genderSeeker.progress
        val catGender = if (genderValue == GenderSeeker.MALE) "male" else "female"

        val cat = CatDataClass(catName, catRace, 0, catSize, catWeight, catGender)
        DatabaseHelper.writeNewCat(cat)
    }

    private fun deleteCat() {
        val catName: String = nameField.text.toString()
        DatabaseHelper.deleteCat(catName)
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
