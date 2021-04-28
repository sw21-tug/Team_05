package at.tugraz05.slimcat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.SeekBar
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat


class SettingsActivity : AppCompatActivity() {
    companion object {
            const val KG = 0
            const val LF = 1
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        // make unit seeker
        val unitSeeker = findViewById<SeekBar>(R.id.settings_seek_measurement)

        // unit seeker helpers
        findViewById<TextView>(R.id.settings_unit_of_measurement_kg).setOnClickListener { unitSeeker.progress = KG }
        findViewById<TextView>(R.id.settings_unit_of_measurement_lf).setOnClickListener { unitSeeker.progress = LF
        Toast.makeText(applicationContext,"Hallo", Toast.LENGTH_SHORT).show()}


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val actionBar = supportActionBar
        actionBar!!.title = getString(R.string.title_activity_settings)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) onBackPressed()
        return super.onOptionsItemSelected(item)
    }



}


