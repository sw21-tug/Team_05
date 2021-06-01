package at.tugraz05.slimcat

import android.animation.LayoutTransition
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import at.tugraz05.slimcat.databinding.FoodAccordionBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FoodListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_list)


        findViewById<FloatingActionButton>(R.id.btn_addfood).setOnClickListener {
            val intent = Intent(this, AddFoodActivity::class.java)
            startActivity(intent)
        }

        /*DatabaseHelper.get().addValueEventListener{
            displayFoods(DatabaseHelper.get().readUserFoods())
        }*/

        /*val listfood = arrayOf(
            FoodDetailsDataClass("Sheba", 20.0, 20.0, 20.0, 20.0, 10.0, 1) ,
            FoodDetailsDataClass("Shea2", 20.0, 20.0, 20.0, 20.0, 10.0, 1)
        )
*/
        //LanguageHandler.setLanguage(this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val actionBar = supportActionBar
        actionBar!!.title = getString(R.string.title_food_back)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun displayFoods(foods: List<FoodDetailsDataClass?>) {
        val container = findViewById<LinearLayout>(R.id.food_scroll_content)
        container.children.forEach { view ->
            val binding = DataBindingUtil.bind<FoodAccordionBinding>(view)
            if (binding != null) {
                val foundFood = foods.find {
                    it?.name == binding.food?.name
                }
                if (foundFood != null) {
                    binding.food = foundFood
                }
                else {
                    container.removeView(view)
                }
            }
        }

        foods.forEach { food ->
            if (container.children.find { view -> DataBindingUtil.bind<FoodAccordionBinding>(view)?.food?.name == food?.name} == null) {
                val binding = DataBindingUtil.inflate<FoodAccordionBinding>(layoutInflater, R.layout.food_accordion, container, false)
                binding.food = food
                binding.presenter = FoodAccordionPresenter(this, binding)
                container.addView(binding.root)
                binding.root.findViewById<Button>(R.id.delete_food_btn).setOnClickListener {
                    val name = binding.root.findViewById<TextView>(R.id.name_food).text.toString()
                    deleteFood(name)
                    recreate()
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                    binding.root.findViewById<FrameLayout>(R.id.food_collapsible).layoutTransition.enableTransitionType(
                        LayoutTransition.CHANGING)
            }
        }
    }

    private fun deleteFood(name: String) {
        // DatabaseHelper.get().deleteFood(name)
    }
}





