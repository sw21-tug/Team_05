package at.tugraz05.slimcat

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TableLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import at.tugraz05.slimcat.databinding.ActivityTrackFoodBinding
import at.tugraz05.slimcat.databinding.ActivityTrackFoodElementBinding
import kotlin.properties.Delegates

class TrackFoodActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTrackFoodBinding
    private lateinit var bindings: List<ActivityTrackFoodElementBinding>
    private lateinit var cat: CatDataClass
    private var cals by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_track_food)

        cat = intent.extras!!.getParcelable(Constants.CAT_PARAM)!!
        cals = cat.calorieRecommendation

        DatabaseHelper.get().addValueEventListener {
            updateList(DatabaseHelper.get().readUserFoods())
        }

        val actionbar = supportActionBar
        actionbar!!.title = getString(R.string.catlist_track_food)
        actionbar.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.track_food_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        if (id == R.id.action_reset) {
            cat.calorieRecommendation = Util.calculateCalories(cat)
        }
        return super.onOptionsItemSelected(item)
    }

    @Synchronized
    private fun updateList(foods: List<FoodDetailsDataClass?>) {
        val container = findViewById<TableLayout>(R.id.scroll_track_food)
        container.removeAllViews()
        bindings = foods.map {
            val binding = DataBindingUtil.inflate<ActivityTrackFoodElementBinding>(layoutInflater, R.layout.activity_track_food_element, container, false)
            binding.cat = cat
            binding.food = it!!
            binding.activity = this
            container.addView(binding.root)
            binding.root.findViewById<EditText>(R.id.text_food_eaten).addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    updateCals()
                }
                override fun afterTextChanged(s: Editable?) {
                }
            })
            binding
        }
    }

    private fun updateCals() {
        cat.calorieRecommendation = cals - bindings.sumOf {
            try {
                it.food!!.calories * Integer.parseInt(it.root.findViewById<EditText>(R.id.text_food_eaten).text.toString()) / 100
            } catch (e: NumberFormatException) {
                0
            }
        }
        Log.d("trackFood", "new calories ${cat.calorieRecommendation}")
    }

    @Suppress("UNUSED_PARAMETER")
    fun save(view: View) {
        DatabaseHelper.get().editUser(cat.name!!, cat)
        finish()
    }
}