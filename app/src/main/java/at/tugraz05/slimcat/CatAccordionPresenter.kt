package at.tugraz05.slimcat

import android.view.View
import androidx.databinding.ObservableBoolean

data class CatAccordionPresenter(var open: ObservableBoolean = ObservableBoolean(false)) {

    fun toggleOpen(view: View) {
        open.set(!open.get())
    }
}