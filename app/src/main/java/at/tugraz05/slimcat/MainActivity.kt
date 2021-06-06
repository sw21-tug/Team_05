package at.tugraz05.slimcat

import android.animation.LayoutTransition
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import com.google.android.material.floatingactionbutton.FloatingActionButton
import at.tugraz05.slimcat.databinding.CatAccordionBinding
import java.util.*

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        DatabaseHelper.get().maybeInit(applicationContext)

        findViewById<FloatingActionButton>(R.id.btn_addcat).setOnClickListener {
            val intent = Intent(this, AddcatActivity::class.java)
            startActivity(intent)
        }

        DatabaseHelper.get().addValueEventListener {
            val today = Calendar.getInstance()
            today.set(Calendar.HOUR_OF_DAY,0)
            today.set(Calendar.MINUTE,0)
            today.set(Calendar.SECOND,0)

            val userCats = DatabaseHelper.get().readUserCats()
            userCats.forEach {
                val date = Date(it!!.timestamp)
                if (date.before(today.time)) {
                    it.calorieRecommendation = Util.calculateCalories(it)
                    DatabaseHelper.get().editUser(it.name!!, it)
                }
            }
            displayCats(userCats)
        }

        LanguageHandler.setLanguage(this)
        supportActionBar!!.title = getString(R.string.app_name)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when(item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivityForResult(intent, 0)
            }
            R.id.action_food -> {
                val intent = Intent(this, FoodListActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        recreate()

    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
    }

    fun displayCats(cats: List<CatDataClass?>) {
        val container = findViewById<LinearLayout>(R.id.scroll_content)
        val f = arrayOfNulls<Boolean>(cats.size)
        val toRemove = arrayOfNulls<View>(container.childCount)
        container.children.forEachIndexed { i, view ->
            val binding = DataBindingUtil.bind<CatAccordionBinding>(view)!!
            val index = cats.indexOfFirst { it?.name == binding.cat?.name }
            if (index != -1) {
                binding.cat = cats[index]
                binding.presenter!!.update()
                f[index] = true
            }
            else {
                toRemove[i] = view
            }
        }
        toRemove.forEach { it?.let { container.removeView(it) } }

        cats.forEachIndexed { index, cat ->
            if (f[index] == true)
                return@forEachIndexed
            val binding = DataBindingUtil.inflate<CatAccordionBinding>(layoutInflater, R.layout.cat_accordion, container, false)
            binding.cat = cat
            binding.presenter = CatAccordionPresenter(this, binding)
            container.addView(binding.root)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                binding.root.findViewById<FrameLayout>(R.id.collapsible).layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        }
    }
}
