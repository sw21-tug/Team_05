package at.tugraz05.slimcat

import android.animation.LayoutTransition
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.fragment.app.commit
import at.tugraz05.slimcat.databinding.CatAccordionBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import at.tugraz05.slimcat.databinding.CatAccordionFoodBinding
import java.nio.file.Files
import java.nio.file.Paths

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

//        val cats = arrayOf(
//                CatDummy("cat1", 8, 5.4), CatDummy("cat2", 15, 2.1),
//                CatDummy("cat3"), CatDummy("cat4"), CatDummy("cat5")
//        )
//        cats.forEach { databaseHelper.writeNewCat(it) }

        DatabaseHelper.get().addValueEventListener{
            val today = Calendar.getInstance()
            today.set(Calendar.HOUR_OF_DAY,0)
            today.set(Calendar.MINUTE,0)
            today.set(Calendar.SECOND,0)

            val userCats = DatabaseHelper.get().readUserCats().map {
                val date = Date(it!!.timestamp)
                if (date.before(today.time)){
                    it.calorieRecommendation = Util.calculateCalories(it)
                    DatabaseHelper.get().editUser(it.name!!, it)
                }
                it
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
        container.children.forEach { view ->
            val binding = DataBindingUtil.bind<CatAccordionBinding>(view)
            if (binding != null) {
                val foundCat = cats.find {
                    it?.name == binding.cat?.name
                }
                if (foundCat != null) {
                    binding.cat = foundCat
                    binding.presenter!!.update()
                }
                else {
                    container.removeView(view)
                }
            }
        }

        cats.forEach { cat ->
            if (container.children.find { view -> DataBindingUtil.bind<CatAccordionBinding>(view)?.cat?.name == cat?.name} == null) {
                val binding = DataBindingUtil.inflate<CatAccordionBinding>(layoutInflater, R.layout.cat_accordion, container, false)
                binding.cat = cat
                binding.presenter = CatAccordionPresenter(this, binding)
                container.addView(binding.root)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                    binding.root.findViewById<FrameLayout>(R.id.collapsible).layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
            }
        }
    }
}
