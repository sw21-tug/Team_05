package at.tugraz05.slimcat

import android.os.Bundle
import android.widget.TableLayout
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import at.tugraz05.slimcat.databinding.ActivityTrackFoodBinding
import at.tugraz05.slimcat.databinding.ActivityTrackFoodElementBinding
import at.tugraz05.slimcat.databinding.CatAccordionBinding

class TrackFoodActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTrackFoodBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_track_food)

        val container = findViewById<TableLayout>(R.id.scroll_track_food)
        val cat:CatDataClass = intent.extras!!.getParcelable("Cat")!!

        Food.foods.forEach{
            val binding = DataBindingUtil.inflate<ActivityTrackFoodElementBinding>(layoutInflater, R.layout.activity_track_food_element, container, false)
            binding.cat = cat
            binding.food = it
            container.addView(binding.root)
        }

        val actionbar = supportActionBar
        actionbar!!.title = getString(R.string.catlist_track_food)
        actionbar.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}