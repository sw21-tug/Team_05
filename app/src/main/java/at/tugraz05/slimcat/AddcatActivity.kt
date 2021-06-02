package at.tugraz05.slimcat

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import at.tugraz05.slimcat.Util.calculateCalories
import at.tugraz05.slimcat.databinding.ActivityAddcatBinding
import java.io.File
import java.lang.NumberFormatException
import java.nio.file.Files
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class AddcatActivity : AppCompatActivity() {
    private lateinit var scrollView: ScrollView
    private lateinit var seeker: GenderSeeker
    private lateinit var nameField: EditText
    private var edit = false
    private lateinit var oldName: String

    private lateinit var binding: ActivityAddcatBinding
    private lateinit var imageButton: ImageButton
    private var imagePath: String = ""

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_addcat)
        binding.view = this
        val bundle = intent.extras
        if (bundle != null) {
            edit = true
            binding.cat = bundle.getParcelable("Cat")!!
            oldName = binding.cat!!.name!!
        }
        else
        {
            binding.cat = CatDataClass()
            oldName = ""
        }

        // camera
        imageButton = findViewById(R.id.btn_camera)
        imageButton.setOnClickListener {
            if (TextUtils.isEmpty(nameField.text)) {
                nameField.error = resources.getString(R.string.error_create_cat)
                scrollView.fullScroll(ScrollView.FOCUS_UP)
            }
            else
                imagePath = CaptureImage.captureImage(this, "cats/${binding.cat!!.name}") ?: ""
        }

        if (binding.cat?.imageString?.isNotEmpty() == true && binding.cat?.name?.isNotEmpty() == true)
        {
            val file = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!.toPath().resolve(binding.cat!!.imageString!!).toFile()
            imagePath = file.absolutePath

            if (!file.exists()) {
                Files.createDirectories(file.parentFile!!.toPath())
                file.createNewFile()
                DatabaseHelper.get().getImage("${DatabaseHelper.get().getUserId()}/${binding.cat!!.imageString!!}", file) {
                    imageButton.setImageURI(Uri.fromFile(file))
                }
            }
            else {
                imageButton.setImageURI(Uri.fromFile(file))
            }
        }

        // initialize all fields
        scrollView = findViewById(R.id.main_scroll_view)
        nameField = findViewById(R.id.txt_name)

        findViewById<Button>(R.id.btn_save).setOnClickListener {
            if (TextUtils.isEmpty(nameField.text)) {
                nameField.error = resources.getString(R.string.error_create_cat)
                scrollView.fullScroll(ScrollView.FOCUS_UP)
            }
            else {
                if (binding.cat!!.date_of_birth != null)
                    binding.cat!!.age = Util.calculateAge(LocalDate.parse(binding.cat!!.date_of_birth, DateTimeFormatter.ofPattern("y-M-d")), LocalDate.now())

                binding.cat!!.calorieRecommendation = calculateCalories(binding.cat!!)

                if (edit) updateCat()
                else createCat()

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
        Locale.setDefault(Locale.CHINA)
        val formatDate = SimpleDateFormat("y-M-d", Locale.CHINESE)

        findViewById<Button>(R.id.btn_dob).setOnClickListener {
            val getDate : Calendar = Calendar.getInstance()
            val datepicker = DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                { _, year, month, day ->

                    val selectDate = Calendar.getInstance()
                    selectDate.set(Calendar.YEAR, year)
                    selectDate.set(Calendar.MONTH, month)
                    selectDate.set(Calendar.DAY_OF_MONTH, day)
                    val date = formatDate.format((selectDate.time))
                    Toast.makeText(this, "Date : $date", Toast.LENGTH_SHORT).show()
                    findViewById<TextView>(R.id.txt_dob).text = date
                    binding.cat!!.date_of_birth = date
                }, getDate.get(Calendar.YEAR), getDate.get(Calendar.MONTH), getDate.get(Calendar.DAY_OF_MONTH))
            datepicker.show()
        }

        // make gender seeker hide female-only options
        val genderSeeker = findViewById<SeekBar>(R.id.seek_gender)
        val femaleSwitches = listOf<TableRow>(
                findViewById(R.id.row_gestation),
                findViewById(R.id.row_lactation),
        )
        genderSeeker.progress = binding.cat?.gender ?: 1
        seeker = GenderSeeker(genderSeeker.progress, femaleSwitches, binding)
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

    private fun createCat() {
        DatabaseHelper.get().writeNewCat(binding.cat!!)
    }

    private fun updateCat() {
        DatabaseHelper.get().editUser(oldName, binding.cat!!)
    }

    private fun deleteCat() {
        val catName: String = nameField.text.toString()
        DatabaseHelper.get().deleteCat(catName)
    }

    fun getWeightStr():String{
        val metricSystem = this.getSharedPreferences("userprefs", AppCompatActivity.MODE_PRIVATE).getInt("unit", 0 )
        return if (metricSystem == SettingsActivity.METRIC) {
            binding.cat!!.getWeightStr()
        } else {
            Util.convertKgToLbs(binding.cat!!.weight).toString()
        }
    }

    fun setWeightStr(weight:String){
        var finalWeight = 0.0
        val metricSystem = this.getSharedPreferences("userprefs", AppCompatActivity.MODE_PRIVATE).getInt("unit", 0 )
        if (metricSystem == SettingsActivity.METRIC) {
            finalWeight = try {
                weight.toDouble()
            } catch (e: NumberFormatException) {
                0.0
            }
        }
        else {
            finalWeight = try {
                Util.convertLbsToKg(weight.toDouble())
            } catch (e: NumberFormatException) {
                0.0
            }
        }
        binding.cat!!.weight = finalWeight
        Log.d("test", "$finalWeight")
    }

    // adjust when size change to double!!!!!
    fun getSizeStr():String{
        val metricSystem = this.getSharedPreferences("userprefs", AppCompatActivity.MODE_PRIVATE).getInt("unit", 0 )
        return if (metricSystem == SettingsActivity.METRIC) {
            binding.cat!!.getSizeStr()
        } else {
            Util.convertCmToInch(binding.cat!!.size.toDouble()).toInt().toString()
        }
    }

    // adjust when size change to double!!!!!
    fun setSizeStr(size: String){
        var finalSize: Double
        val metricSystem = this.getSharedPreferences("userprefs", AppCompatActivity.MODE_PRIVATE).getInt("unit", 0 )
        if (metricSystem == SettingsActivity.METRIC) {
            finalSize = try {
                size.toDouble()
            } catch (e: NumberFormatException) {
                0.0
            }
        }
        else {
            finalSize = try {
                Util.convertLbsToKg(size.toDouble())
            } catch (e: NumberFormatException) {
                0.0
            }
        }
        binding.cat!!.size = finalSize.toDouble()
    }

    fun getWeightHintStr():String{
        val metricSystem = this.getSharedPreferences("userprefs", AppCompatActivity.MODE_PRIVATE).getInt("unit", 0 )
        return if (metricSystem == SettingsActivity.METRIC) {
            getString(R.string.input_weight_hint)
        } else {
            getString(R.string.input_weight_hint_imperial)
        }
    }

    fun getSizeHintStr():String{
        val metricSystem = this.getSharedPreferences("userprefs", AppCompatActivity.MODE_PRIVATE).getInt("unit", 0 )
        return if (metricSystem == SettingsActivity.METRIC) {
            getString(R.string.input_size_hint)
        } else {
            getString(R.string.input_size_hint_imperial)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val file = File(imagePath)
        CaptureImage.receiveIntent(requestCode, resultCode, data, this, file)

        val uri = Uri.fromFile(file)
        DatabaseHelper.get().uploadImagesToFirebase("${DatabaseHelper.get().getUserId()}/cats/${binding.cat!!.name}/${file.name}", uri) {
            imageButton.setImageURI(uri)
            binding.cat!!.imageString = "cats/${binding.cat!!.name}/${file.name}"
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        imagePath = CaptureImage.captureImage(this) ?: ""
    }

}

class GenderSeeker(p: Int, private var switches: List<TableRow>, private var binding: ActivityAddcatBinding) : SeekBar.OnSeekBarChangeListener {
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
        binding.cat?.gender = progress
    }
}
