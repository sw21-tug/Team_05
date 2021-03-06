package at.tugraz05.slimcat

import android.app.Activity
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableBoolean
import at.tugraz05.slimcat.databinding.CatAccordionBinding
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.widget.ImageView
import android.widget.TableLayout
import androidx.databinding.DataBindingUtil
import at.tugraz05.slimcat.databinding.CatAccordionFoodBinding
import java.nio.file.Files

var calType:Int = 0

data class CatAccordionPresenter(val context: Activity, val binding: CatAccordionBinding, var foods: List<FoodDetailsDataClass?>, var open: ObservableBoolean = ObservableBoolean(false)) {
    init {
        update()
    }

    fun update(newFoods: List<FoodDetailsDataClass?>? = null) {
        if (newFoods != null) foods = newFoods
        updateCatImage()
        updateFoods()
    }

    @Suppress("UNUSED_PARAMETER")
    fun toggleOpen(view: View) {
        open.set(!open.get())
    }

    @Suppress("UNUSED_PARAMETER")
    fun edit(view: View) {
        val intent = Intent(context, AddcatActivity::class.java)
        val bundle = bundleOf(Constants.CAT_PARAM to binding.cat)
        intent.putExtras(bundle)
        context.startActivity(intent)
    }

    @Suppress("UNUSED_PARAMETER")
    fun track(view: View) {
        val intent = Intent(context, TrackFoodActivity::class.java)
        val bundle = bundleOf(Constants.CAT_PARAM to binding.cat)
        intent.putExtras(bundle)
        context.startActivity(intent)
    }

    fun getWeightStr(cat: CatDataClass): String {
        val metricSystem = context.getSharedPreferences(Constants.USER_PREFS, AppCompatActivity.MODE_PRIVATE).getInt("unit", 0 )
        return if (metricSystem == SettingsActivity.METRIC) {
            calType = 0
            context.getString(R.string.catlist_text_weight).format(cat.weight ?: 0.0)
        } else {
            calType = 1
            context.getString(R.string.catlist_text_weight_lbs).format(cat.weight?.let {
                Util.convertKgToLbs(it)
            } ?: 0.0)
        }
    }

    private fun updateCatImage() {
        if (binding.cat?.imageString?.isNotEmpty() == true) {
            val file = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!.toPath().resolve(binding.cat!!.imageString!!).toFile()

            if (!file.exists()) {
                Files.createDirectories(file.parentFile!!.toPath())
                file.createNewFile()
                DatabaseHelper.get().getImage("${DatabaseHelper.get().getUserId()}/${binding.cat!!.imageString!!}", file) {
                    binding.root.findViewById<ImageView>(R.id.imageView).setImageURI(Uri.fromFile(file))
                }
            }
            else {
                binding.root.findViewById<ImageView>(R.id.imageView).setImageURI(Uri.fromFile(file))
            }
        }
    }

    private fun updateFoods() {
        val table = binding.root.findViewById<TableLayout>(R.id.accordion_food_list)
        table.removeAllViews()

        foods.forEach {
            val b = DataBindingUtil.inflate<CatAccordionFoodBinding>(
                context.layoutInflater,
                R.layout.cat_accordion_food,
                table,
                false
            )
            b.cat = binding.cat
            b.food = it
            table.addView(b.root)
        }
    }
}

@BindingAdapter("cat", "presenter")
fun catWeightBinding(view: TextView, cat:CatDataClass, presenter: CatAccordionPresenter){
    view.text = presenter.getWeightStr(cat)
}

@BindingAdapter("calories", "food")
fun setGrams(view: TextView, calories: Int, food: FoodDetailsDataClass) {
    Log.d("setGrams", "${food.name}: ${food.calories} ${calories} ${Util.calcGramsOfFood(food, calories)}")
    if (calType == 0){
        view.text = view.resources.getString(R.string.catlist_text_food_amount, Util.calcGramsOfFood(food, calories))
    }
    else{
        view.text = view.resources.getString(R.string.catlist_text_food_amount_lbs, Util.convertGrammToLbs(Util.calcGramsOfFood(food, calories)))
    }
}
