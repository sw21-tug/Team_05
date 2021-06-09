package at.tugraz05.slimcat

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import at.tugraz05.slimcat.databinding.SettingsActivityBinding
import java.io.File


class SettingsActivity: AppCompatActivity() {
    companion object {
            const val METRIC = 0
            const val IMPERIAL = 1

            const val MALE = 0
            const val FEMALE = 1
            const val DIVERSE = 2

            const val ENGLISH = 0
            const val MANDARIN = 1
    }

    private lateinit var binding: SettingsActivityBinding
    private lateinit var imageButton: ImageButton

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // data binding for layout
        binding = DataBindingUtil.setContentView(this, R.layout.settings_activity)
        binding.user = loadData(this)

        // make unit seeker
        val unitSeeker = findViewById<SeekBar>(R.id.settings_seek_measurement)

        // unit seeker helpers
        findViewById<TextView>(R.id.settings_unit_of_measurement_kg).setOnClickListener { unitSeeker.progress = METRIC }
        findViewById<TextView>(R.id.settings_unit_of_measurement_lbs).setOnClickListener { unitSeeker.progress = IMPERIAL }

        // gender spinner
        val spinnerGender = findViewById<Spinner>(R.id.settings_gender_spinner)
        ArrayAdapter.createFromResource(this, R.array.settings_gender, R.layout.spinner_closed_items).also {
            it.setDropDownViewResource(R.layout.spinner_items)
            spinnerGender.adapter = it
        }

        // language spinner
        val spinnerLang = findViewById<Spinner>(R.id.settings_language_spinner)
        ArrayAdapter.createFromResource(this, R.array.settings_language, R.layout.spinner_closed_items).also {
            it.setDropDownViewResource(R.layout.spinner_items)
            spinnerLang.adapter = it
        }

        // change language selection in spinner
        val sharedpref = getSharedPreferences("userprefs", MODE_PRIVATE)
        sharedpref.registerOnSharedPreferenceChangeListener { _, _ ->
            LanguageHandler.setLanguage(this)
            finish()
        }

        imageButton = findViewById(R.id.imageButton)
        imageButton.setOnClickListener {
            binding.user?.image = CaptureImage.captureImage(this) ?: ""
        }
        if (binding.user?.image?.isNotEmpty() == true)
            imageButton.setImageURI(Uri.fromFile(File(binding.user?.image!!)))

        // save button
        val saveButton = findViewById<Button>(R.id.setting_btn_save)
        saveButton.setOnClickListener {
            saveData(this)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val actionBar = supportActionBar
        actionBar!!.title = getString(R.string.title_activity_settings)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    // Saves data
    private fun saveData(context: Context) {
        val sharedpref = context.getSharedPreferences(Constants.USER_PREFS, MODE_PRIVATE)
        val editor = sharedpref.edit()
        editor.putString("name", binding.user?.name)
        editor.putString("email", binding.user?.email)
        editor.putInt("gender", binding.user?.gender ?:0)
        editor.putInt("unit", binding.user?.unit ?:0)
        editor.putInt("language", binding.user?.language ?:0)
        editor.putString("image", binding.user?.image)
        editor.apply()
    }

    fun loadData(context: Context): UserData {
        val sharedpref = context.getSharedPreferences(Constants.USER_PREFS, MODE_PRIVATE)
        return UserData(
            sharedpref.getString("name", "")!!,
            sharedpref.getString("email", "")!!,
            sharedpref.getInt("gender", 0),
            sharedpref.getInt("unit", 0),
            sharedpref.getInt("language", 0),
            sharedpref.getString("image", "")!!
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (binding.user == null)
            return

        val file = File(binding.user!!.image)
        CaptureImage.receiveIntent(requestCode, resultCode, data, this, file)

        if (binding.user!!.image.isNotEmpty())
            imageButton.setImageURI(Uri.fromFile(file))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        binding.user?.image = CaptureImage.captureImage(this) ?: ""
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LanguageHandler.setLanguage(newBase!!))
    }

    fun hideKeyboard() {
        val test: View? = window.decorView.rootView
        val inputMethodManager = this.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(test!!.windowToken, 0)
    }

    // data class for binding of userdata from sharedpreferences
    data class UserData(var name: String, var email: String, var gender: Int, var unit: Int, var language: Int, var image: String)
}