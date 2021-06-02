package at.tugraz05.slimcat

import android.content.Context
import android.view.View
import androidx.databinding.ObservableBoolean
import at.tugraz05.slimcat.databinding.FoodAccordionBinding

class FoodAccordionPresenter(val context: Context, val binding: FoodAccordionBinding, var open: ObservableBoolean = ObservableBoolean(false)) {
    fun toggleOpen(view: View) {
        open.set(!open.get())
    }
}