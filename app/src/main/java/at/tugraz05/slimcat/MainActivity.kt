package at.tugraz05.slimcat

import android.animation.LayoutTransition
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.fragment.app.commit
import at.tugraz05.slimcat.databinding.CatAccordionBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        DatabaseHelper.maybeInit(applicationContext)

        findViewById<FloatingActionButton>(R.id.btn_addcat).setOnClickListener {
            val intent = Intent(this, AddcatActivity::class.java)
            startActivity(intent)
        }

//        val cats = arrayOf(
//                CatDummy("cat1", 8, 5.4), CatDummy("cat2", 15, 2.1),
//                CatDummy("cat3"), CatDummy("cat4"), CatDummy("cat5")
//        )
//        cats.forEach { databaseHelper.writeNewCat(it) }

        DatabaseHelper.addValueEventListener{
            displayCats(DatabaseHelper.readUserCats())
        }
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
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
    }

    fun displayCats(cats:List<CatDataClass?>) {
        val container = findViewById<LinearLayout>(R.id.scroll_content)
        container.children.forEach { view ->
            val binding = DataBindingUtil.bind<CatAccordionBinding>(view)
            if (binding != null) {
                val found_cat = cats.find {
                    it?.name == binding.cat?.name
                }
                if (found_cat != null) {
                    binding.cat = found_cat
                }
                else {
                    container.removeView(view)
                }
            }
        }

        cats.forEach {
            if (container.children.find { view -> DataBindingUtil.bind<CatAccordionBinding>(view)?.cat?.name == it?.name} == null) {

                val binding = DataBindingUtil.inflate<CatAccordionBinding>(layoutInflater, R.layout.cat_accordion, container, false)
                binding.cat = it
                binding.presenter = CatAccordionPresenter()
                container.addView(binding.root)
                binding.root.findViewById<Button>(R.id.edit_cat).setOnClickListener {
                    val intent = Intent(this, AddcatActivity::class.java)
                    startActivity(intent)
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                    binding.root.findViewById<FrameLayout>(R.id.collapsible).layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
            }
        }
    }
}
