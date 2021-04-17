package at.tugraz05.slimcat

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import at.tugraz05.slimcat.CatDummy

class MainActivity : AppCompatActivity() {
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        databaseHelper = DatabaseHelper()
        databaseHelper.initializeDatabaseReference()
        databaseHelper.addPostEventListener()

        findViewById<FloatingActionButton>(R.id.add_cat).setOnClickListener { view ->
            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            //        .setAction("Action", null).show()
            addCat("test123")
        }

        findViewById<FloatingActionButton>(R.id.show_cat).setOnClickListener { view ->
            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            //        .setAction("Action", null).show()
            printCat("test123")
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

    fun printCat(userId: String) {
        val cat: CatDummy = databaseHelper.readUserCat(userId)
        var catName: String = cat.getName()
        var catAge: Int = cat.getAge()
        var catWeight: Double = cat.getWeight()

        Toast.makeText(applicationContext, "$catName $catAge $catWeight", Toast.LENGTH_LONG).show()
    }

    fun addCat(userId: String) {
        var catClass: CatDummy = CatDummy("Phil", 15, 23.4)
        databaseHelper.writeNewCat(userId, catClass)
    }
}