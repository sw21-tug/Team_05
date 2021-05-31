package at.tugraz05.slimcat

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.content.res.Resources
import android.util.Log
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableBoolean
import at.tugraz05.slimcat.databinding.CatAccordionBinding

data class CatAccordionPresenter(val context: Context, val binding: CatAccordionBinding, var open: ObservableBoolean = ObservableBoolean(false)) {
    @Suppress("UNUSED_PARAMETER")
    fun toggleOpen(view: View) {
        open.set(!open.get())
    }

    fun getWeightStr(cat: CatDataClass):String{
        val metricSystem = context.getSharedPreferences("userprefs", AppCompatActivity.MODE_PRIVATE).getInt("unit", 0 )
        return if (metricSystem == SettingsActivity.METRIC) {
            context.getString(R.string.catlist_text_weight).format(cat.weight)
        } else {
            context.getString(R.string.catlist_text_weight_lbs).format(Util.convertKgToLbs(cat.weight))
        }
    }


/*
    fun getSizeStr():String{
        val metricSystem = context.getSharedPreferences("userprefs", AppCompatActivity.MODE_PRIVATE).getInt("unit", 0 )
        return if (metricSystem == SettingsActivity.METRIC) {
            cat.getSizeStr()
            context.getString(R.string.catlist_text_size).format(cat.weight)
        } else {
            Util.convertCmToInch(cat.size.toDouble()).toInt().toString()
        }
    }
    */

}
@BindingAdapter("cat", "presenter")
fun catWeightBinding(view: TextView, cat:CatDataClass, presenter: CatAccordionPresenter){
    view.text = presenter.getWeightStr(cat)
}

@BindingAdapter("cat", "food")
fun setGrams(view: TextView, cat: CatDataClass, food: Food) {
    Log.d("setGrams", "${food.name}: ${food.kcalPer100G} ${cat.calorieRecommendation} ${Util.calcGramsOfFood(food, cat.calorieRecommendation)}")
    view.text = view.resources.getString(R.string.catlist_text_food_amount, Util.calcGramsOfFood(food, cat.calorieRecommendation))
}