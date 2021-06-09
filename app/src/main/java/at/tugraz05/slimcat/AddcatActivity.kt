package at.tugraz05.slimcat

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
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
import java.util.*

class AddcatActivity : AppCompatActivity() {
    private lateinit var scrollView: ScrollView
    private lateinit var nameField: EditText
    private var edit = false
    private var oldName = ""
    private var calDiff = 0

    private lateinit var binding: ActivityAddcatBinding
    private lateinit var imageButton: ImageButton
    private var imagePath: String = ""

    companion object {
        const val MALE = 0
        const val FEMALE = 1
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_addcat)
        binding.view = this
        val bundle = intent.extras
        if (bundle != null) {
            edit = true
            binding.cat = bundle.getParcelable(Constants.CAT_PARAM)!!
            oldName = binding.cat!!.name!!
            calDiff = calculateCalories(binding.cat!!) - binding.cat!!.calorieRecommendation
        }
        else
            binding.cat = CatDataClass()

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
                binding.cat!!.calorieRecommendation = calculateCalories(binding.cat!!) - calDiff

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

        findViewById<Button>(R.id.btn_dob).setOnClickListener {
            showCalendarDialog()
        }

        // gender seeker helpers
        findViewById<TextView>(R.id.label_gender_male).setOnClickListener { binding.cat!!.gender = MALE }
        findViewById<TextView>(R.id.label_gender_female).setOnClickListener { binding.cat!!.gender = FEMALE }

        if (!edit)
            findViewById<Button>(R.id.btn_delete).visibility = View.GONE

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
        val metricSystem = this.getSharedPreferences(Constants.USER_PREFS, AppCompatActivity.MODE_PRIVATE).getInt("unit", 0 )
        return if (metricSystem == SettingsActivity.METRIC) {
            binding.cat!!.getWeightStr()
        } else {
            binding.cat!!.weight?.let { Util.convertKgToLbs(it).toString() } ?: ""
        }
    }

    fun setWeightStr(weight:String){
        val metricSystem = this.getSharedPreferences(Constants.USER_PREFS, AppCompatActivity.MODE_PRIVATE).getInt("unit", 0 )
        val finalWeight = if (metricSystem == SettingsActivity.METRIC) {
            try {
                weight.toDouble()
            } catch (e: NumberFormatException) {
                0.0
            }
        } else {
            try {
                Util.convertLbsToKg(weight.toDouble())
            } catch (e: NumberFormatException) {
                0.0
            }
        }
        binding.cat!!.weight = finalWeight
    }

    // adjust when size change to double!!!!!
    fun getSizeStr():String{
        val metricSystem = this.getSharedPreferences(Constants.USER_PREFS, AppCompatActivity.MODE_PRIVATE).getInt("unit", 0 )
        return if (metricSystem == SettingsActivity.METRIC) {
            binding.cat!!.getSizeStr()
        } else {
            binding.cat!!.size?.let { Util.convertCmToInch(it).toInt().toString() } ?: ""
        }
    }

    // adjust when size change to double!!!!!
    fun setSizeStr(size: String){
        val metricSystem = this.getSharedPreferences(Constants.USER_PREFS, AppCompatActivity.MODE_PRIVATE).getInt("unit", 0 )
        val finalSize = if (metricSystem == SettingsActivity.METRIC) {
            try {
                size.toDouble()
            } catch (e: NumberFormatException) {
                0.0
            }
        } else {
            try {
                Util.convertLbsToKg(size.toDouble())
            } catch (e: NumberFormatException) {
                0.0
            }
        }
        binding.cat!!.size = finalSize
    }

    fun getWeightHintStr():String{
        val metricSystem = this.getSharedPreferences(Constants.USER_PREFS, AppCompatActivity.MODE_PRIVATE).getInt("unit", 0 )
        return if (metricSystem == SettingsActivity.METRIC) {
            getString(R.string.input_weight_hint)
        } else {
            getString(R.string.input_weight_hint_imperial)
        }
    }

    fun getSizeHintStr():String{
        val metricSystem = this.getSharedPreferences(Constants.USER_PREFS, AppCompatActivity.MODE_PRIVATE).getInt("unit", 0 )
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

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LanguageHandler.setLanguage(newBase!!))
    }

    private fun showCalendarDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.calendar_dialog)

        Locale.setDefault(Locale.CHINA)
        val formatDate = SimpleDateFormat("y-M-d", Locale.CHINESE)
        var selectedDate = ""
        val datePicker = dialog.findViewById<DatePicker>(R.id.calendar)
        val today = Calendar.getInstance()
        datePicker.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH))
        {
            _, year, month, day ->
            val selectDate = Calendar.getInstance()
            selectDate.set(Calendar.YEAR, year)
            selectDate.set(Calendar.MONTH, month)
            selectDate.set(Calendar.DAY_OF_MONTH, day)
            selectedDate = formatDate.format((selectDate.time))
        }
        val yesBtn = dialog.findViewById(R.id.btnSave) as Button
        val noBtn = dialog.findViewById(R.id.btnCancel) as Button
        yesBtn.setOnClickListener {
            findViewById<TextView>(R.id.txt_dob).text = selectedDate
            binding.cat!!.date_of_birth = selectedDate
            dialog.dismiss()
        }
        noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    fun hideKeyboard() {
        val test: View? = window.decorView.rootView
        val inputMethodManager = this.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(test!!.windowToken, 0)
    }
}
