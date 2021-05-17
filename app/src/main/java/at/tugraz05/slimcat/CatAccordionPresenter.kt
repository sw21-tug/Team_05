package at.tugraz05.slimcat

import android.content.res.Resources
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableBoolean

data class CatAccordionPresenter(var open: ObservableBoolean = ObservableBoolean(false)) {
    fun toggleOpen(view: View) {
        open.set(!open.get())
    }
}

@BindingAdapter("cat", "food")
fun setGrams(view: TextView, cat: CatDataClass, food: Food) {
    if(cat.calorieRecommendation == null){
        return
    }
    Log.d("setGrams", "${food.name}: ${food.kcalPer100G} ${cat.calorieRecommendation} ${Util.calcGramsOfFood(food, cat.calorieRecommendation!!)}")
    view.text = view.resources.getString(R.string.catlist_text_food_amount, Util.calcGramsOfFood(food, cat.calorieRecommendation!!))
}