package at.tugraz05.slimcat

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.junit.Test
import org.junit.runner.RunWith
import android.widget.SeekBar
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.*
import junit.framework.Assert.assertEquals
import org.hamcrest.CoreMatchers
import org.mockito.Mockito


@RunWith(AndroidJUnit4::class)
class SettingsActivityTest {
    private val editTxtIds = arrayOf(R.id.settings_name, R.id.settings_email)
    private val spinnerIds = arrayOf(R.id.settings_gender_spinner, R.id.settings_language_spinner)
    private val ids =  editTxtIds + spinnerIds + arrayOf(R.id.settings_seek_measurement, R.id.imageButton)

    @Test
    fun hasAllInputFields() {
        ActivityScenario.launch(SettingsActivity::class.java)
        (ids + R.id.setting_btn_save).forEach { onView(withId(it)) }
    }

    @Test
    fun inputFieldsEditable() {
        ActivityScenario.launch(SettingsActivity::class.java)
        (editTxtIds + spinnerIds).forEach { onView(withId(it)).check(matches(isClickable())) }
    }

    @Test
    fun clickingUnitLabelsChangesSeeker() {
        ActivityScenario.launch(SettingsActivity::class.java)
        onView(withId(R.id.settings_unit_of_measurement_kg)).perform(click())
        onView(withId(R.id.settings_unit_of_measurement)).check(assertView<SeekBar> { assertThat(it.progress, CoreMatchers.equalTo(SettingsActivity.KG)) })
        onView(withId(R.id.settings_unit_of_measurement_lf)).perform(click())
        onView(withId(R.id.settings_unit_of_measurement)).check(assertView<SeekBar> { assertThat(it.progress, CoreMatchers.equalTo(SettingsActivity.LF)) })
    }

    @Test
    fun testLoadData() {
        val scenario = ActivityScenario.launch(SettingsActivity::class.java)
        val sharedPreferences = Mockito.mock(SharedPreferences::class.java)
        val context = Mockito.mock(Context::class.java)
        Mockito.`when`(context.getSharedPreferences("userprefs", MODE_PRIVATE)).thenReturn(sharedPreferences)
        Mockito.`when`(sharedPreferences.getString("name","" )).thenReturn("test")
        Mockito.`when`(sharedPreferences.getString("email","" )).thenReturn("test@email")
        Mockito.`when`(sharedPreferences.getInt("gender", 0)).thenReturn(0)
        Mockito.`when`(sharedPreferences.getInt("language", 0)).thenReturn(0)
        Mockito.`when`(sharedPreferences.getInt("unit", 0)).thenReturn(0)
        Mockito.`when`(sharedPreferences.getString("image","" )).thenReturn("testimage")
        scenario.onActivity {
            val user = it.loadData(context)
            assertEquals(user.name, "test")
            assertEquals(user.email, "test@email")
            assertEquals(user.gender, 0)
            assertEquals(user.language, 0)
            assertEquals(user.unit, 0)
            assertEquals(user.image, "testimage")
        }
    }

}