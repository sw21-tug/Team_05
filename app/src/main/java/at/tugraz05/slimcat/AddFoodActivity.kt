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
                binding.food!!.calories = Util.calcFoodCals(binding.food!!)
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
        return (((binding.food!!.rawFiber ?: 0.0) + (binding.food!!.crudeAsh
            ?: 0.0) + (binding.food!!.rawFat ?: 0.0) + (binding.food!!.rawProtein
            ?: 0.0) + (binding.food!!.water ?: 0.0)) <= 100.0)
    }
}