package at.tugraz05.slimcat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class FoodListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_list)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val actionBar = supportActionBar
        actionBar!!.title = getString(R.string.title_food_back)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}