package at.tugraz05.slimcat

import android.animation.LayoutTransition
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import at.tugraz05.slimcat.databinding.CatAccordionBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class AboutActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.about_activity)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val actionBar = supportActionBar
        actionBar!!.title = getString(R.string.title_activity_about)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}