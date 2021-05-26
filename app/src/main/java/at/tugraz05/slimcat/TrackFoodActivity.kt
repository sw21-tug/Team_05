package at.tugraz05.slimcat

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import at.tugraz05.slimcat.databinding.ActivityTrackFoodBinding

class TrackFoodActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTrackFoodBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_track_food)

        val actionbar = supportActionBar
        actionbar!!.title = getString(R.string.catlist_track_food)
        actionbar.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}