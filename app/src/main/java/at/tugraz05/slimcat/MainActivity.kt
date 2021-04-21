package at.tugraz05.slimcat

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import at.tugraz05.slimcat.CatDummy
import java.io.File
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        databaseHelper = DatabaseHelper()
        databaseHelper.initializeDatabaseReference()
        databaseHelper.addPostEventListener()
        checkAndCreateUserId()

        findViewById<FloatingActionButton>(R.id.add_cat).setOnClickListener { view ->
            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            //        .setAction("Action", null).show()
            addCat()
        }

        findViewById<FloatingActionButton>(R.id.show_cat).setOnClickListener { view ->
            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            //        .setAction("Action", null).show()
            printCat()
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

    fun printCat() {
        val cat: CatDummy? = databaseHelper.readUserCat("Phil")
        var catName: String = cat!!.name
        var catAge: Int = cat.age
        var catWeight: Double = cat.weight

        Toast.makeText(applicationContext, "$catName $catAge $catWeight", Toast.LENGTH_LONG).show()
    }

    fun addCat() {
        var catClass: CatDummy = CatDummy("Phil", 15, 23.4)
        databaseHelper.writeNewCat(catClass)
    }

    fun checkAndCreateUserId() {
        var filename = "userid"
        try {
            var file: File = File(applicationContext.filesDir, filename)
            var userId: String? = ""

            if(file.exists()){
                userId = file.readText()
                if(userId == ""){
                    userId = databaseHelper.createUserId(null)
                }
                else {
                    userId = databaseHelper.createUserId(userId)
                }
                file.writeText(userId)
            }
            else {
                //should never be reached, only if file construction fails, need to create file
                userId = databaseHelper.createUserId(null)
                Log.w("FILE NOT CREATED", "MainActivity::checkAndCreateUserId this should never happen")
            }
        }
        catch (e: Exception){
            Log.w("ERROR", e)
        }

    }
}