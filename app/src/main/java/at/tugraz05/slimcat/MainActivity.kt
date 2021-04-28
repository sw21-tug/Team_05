package at.tugraz05.slimcat

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.fragment.app.add
import androidx.fragment.app.commit
import at.tugraz05.slimcat.databinding.CatAccordionBinding

class MainActivity : AppCompatActivity() {
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        databaseHelper = DatabaseHelper()
        databaseHelper.initializeDatabaseReference()
        databaseHelper.addPostEventListener()
        databaseHelper.checkAndCreateUserId(applicationContext)

        findViewById<FloatingActionButton>(R.id.btn_addcat).setOnClickListener {
            val intent = Intent(this, AddcatActivity::class.java)
            startActivity(intent)
        }

        val cats = arrayOf(
                CatDummy("cat1", 8), CatDummy("cat2", 15),
                CatDummy("cat3"), CatDummy("cat4"), CatDummy("cat5")
        )
        if (savedInstanceState == null) {
            val container = findViewById<LinearLayout>(R.id.scroll_content)
            cats.forEach { supportFragmentManager.commit {
                val binding = DataBindingUtil.inflate<CatAccordionBinding>(layoutInflater, R.layout.cat_accordion, container, false)
                binding.cat = it
                container.addView(binding.root)
            } }
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
}
