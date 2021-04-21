package at.tugraz05.slimcat

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class AddcatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addcat)

        //Dropdown menu gender
        /*
        val spinnerGender = findViewById<Spinner>(R.id.spinner_gender)
        val adapter = ArrayAdapter(this, R.layout.spinner_closed_items, resources.getStringArray(R.array.spinner_gender))
        adapter.setDropDownViewResource(R.layout.spinner_items)
        spinnerGender.adapter = adapter
*/
        //click on camera (The image will be automatically saved in a default directory)
        findViewById<ImageButton>(R.id.btn_camera).setOnClickListener {
            val intent = Intent("android.media.action.IMAGE_CAPTURE")
            startActivity(intent)
        }

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
