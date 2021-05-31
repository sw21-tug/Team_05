package at.tugraz05.slimcat

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TableLayout
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import at.tugraz05.slimcat.databinding.ActivityTrackFoodBinding
import at.tugraz05.slimcat.databinding.ActivityTrackFoodElementBinding
import at.tugraz05.slimcat.databinding.CatAccordionBinding
import kotlin.properties.Delegates

class TrackFoodActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTrackFoodBinding
    private lateinit var bindings: List<ActivityTrackFoodElementBinding>
    private lateinit var cat: CatDataClass
    private var cals by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_track_food)

        val container = findViewById<TableLayout>(R.id.scroll_track_food)
        cat = intent.extras!!.getParcelable("Cat")!!
        cals = cat.calorieRecommendation

        bindings = Food.foods.map {
            val binding = DataBindingUtil.inflate<ActivityTrackFoodElementBinding>(layoutInflater, R.layout.activity_track_food_element, container, false)
            binding.cat = cat
            binding.food = it
            binding.activity = this
            container.addView(binding.root)
            binding.root.findViewById<EditText>(R.id.text_food_eaten).addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    updateCals()
                }
                override fun afterTextChanged(s: Editable?) {
                }
            })
            binding
        }

        val actionbar = supportActionBar
        actionbar!!.title = getString(R.string.catlist_track_food)
        actionbar.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun updateCals() {
        cat.calorieRecommendation = cals - bindings.sumOf {
            try {
                it.food!!.kcalPer100G * Integer.parseInt(it.root.findViewById<EditText>(R.id.text_food_eaten).text.toString()) / 100
            } catch (e: NumberFormatException) {
                0
            }
        }
        Log.d("trackFood", "new calories ${cat.calorieRecommendation}")
    }

    fun save(view: View) {
        DatabaseHelper.get().editUser(cat.name!!, cat)
        finish()
    }
}