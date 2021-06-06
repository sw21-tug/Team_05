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

data class CatAccordionPresenter(val context: Activity, val binding: CatAccordionBinding, var open: ObservableBoolean = ObservableBoolean(false)) {
    init {
        update()
    }

    fun update() {
        updateCatImage()
        updateFoods()
    }

    fun toggleOpen(view: View) {
        open.set(!open.get())
    }

    fun edit(view: View) {
        val intent = Intent(context, AddcatActivity::class.java)
        val bundle = bundleOf(Constants.CAT_PARAM to binding.cat)
        intent.putExtras(bundle)
        context.startActivity(intent)
    }

    fun track(view: View) {
        val intent = Intent(context, TrackFoodActivity::class.java)
        val bundle = bundleOf(Constants.CAT_PARAM to binding.cat)
        intent.putExtras(bundle)
        context.startActivity(intent)
    }

    fun getWeightStr(cat: CatDataClass): String {
        val metricSystem = context.getSharedPreferences(Constants.USER_PREFS, AppCompatActivity.MODE_PRIVATE).getInt("unit", 0 )
        return if (metricSystem == SettingsActivity.METRIC) {
            context.getString(R.string.catlist_text_weight).format(cat.weight ?: 0.0)
        } else {
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

        Food.foods.forEach {
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
fun setGrams(view: TextView, calories: Int, food: Food) {
    Log.d("setGrams", "${food.name}: ${food.kcalPer100G} $calories ${Util.calcGramsOfFood(food, calories)}")
    view.text = view.resources.getString(R.string.catlist_text_food_amount, Util.calcGramsOfFood(food, calories))
}