package at.tugraz05.slimcat


import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import at.tugraz05.slimcat.databinding.ActivityAddFoodBinding


class AddFoodActivity : AppCompatActivity() {
    private lateinit var scrollView: ScrollView
    private lateinit var nameField: EditText
    private lateinit var binding: ActivityAddFoodBinding


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_food)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_food)


        // initialize all fields
        scrollView = findViewById(R.id.addFood_scroll_view)
        nameField = findViewById(R.id.addFood_name)

        findViewById<Button>(R.id.addFood_btn_save).setOnClickListener {
            if (TextUtils.isEmpty(nameField.text)) {
                nameField.error = resources.getString(R.string.error_add_food)
                scrollView.fullScroll(ScrollView.FOCUS_UP)
            }
            else {
                createFood()
                finish()
            }
        }


        //Back-Button
        val actionbar = supportActionBar
        actionbar!!.title = getString(R.string.add_food)
        actionbar.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun createFood() {
       //  DatabaseHelper.get().writeNewFood(binding.cat!!)
    }

}