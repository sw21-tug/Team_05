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
    private lateinit var saveButton: Button
    private lateinit var errorField: EditText



    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_food)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_food)
        binding.food = FoodDetailsDataClass()

        // initialize all fields
        scrollView = findViewById(R.id.addFood_scroll_view)
        nameField = findViewById(R.id.addFood_name)
        saveButton = findViewById(R.id.addFood_btn_save)
        errorField = findViewById(R.id.addFood_protein)

        saveButton.setOnClickListener {
            if (TextUtils.isEmpty(nameField.text)) {
                nameField.error = resources.getString(R.string.error_add_food)
                scrollView.fullScroll(ScrollView.FOCUS_UP)
            }
            else if (!addUpCatPercentage()) {
                errorField.error = resources.getString(R.string.error_percentage_food)
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
       DatabaseHelper.get().writeNewFood(binding.food!!)
    }

    private fun addUpCatPercentage(): Boolean {
        if((binding.food!!.rawFiber + binding.food!!.crudeAsh + binding.food!!.rawFat + binding.food!!.rawProtein + binding.food!!.water) > 100.0){
            return false
        }
        return true
    }
}