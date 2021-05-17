package at.tugraz05.slimcat

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.service.autofill.Validators.not
import android.util.Log
import android.widget.Button
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.junit.Test
import org.junit.runner.RunWith
import android.widget.SeekBar
import android.widget.TextView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.matcher.ViewMatchers.*

import junit.framework.TestCase
import org.hamcrest.CoreMatchers
import org.mockito.Mockito
import java.util.EnumSet.allOf


@RunWith(AndroidJUnit4::class)
class SettingsActivityTest : TestCase(){
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
        onView(withId(R.id.settings_seek_measurement)).perform(waitFor<SeekBar> { it.progress == SettingsActivity.METRIC })
        onView(withId(R.id.settings_unit_of_measurement_lbs)).perform(click())
        onView(withId(R.id.settings_seek_measurement)).perform(waitFor<SeekBar> { it.progress == SettingsActivity.IMPERIAL })
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
            assertEquals( "test",user.name)
            assertEquals("test@email",user.email)
            assertEquals(0, user.gender )
            assertEquals(0, user.language)
            assertEquals(0, user.unit)
            assertEquals("testimage", user.image)
        }
    }

    @Test
    fun testMandarinBtnSave(){
        ActivityScenario.launch(SettingsActivity::class.java)
        onView(withId(R.id.settings_language_spinner)).perform(scrollTo()).perform(click())
        onView(withText("mandarin (chinese)")).perform(scrollTo()).perform(click())
        onView(withId(R.id.setting_btn_save)).perform(scrollTo()).perform(click())
        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.btn_addcat)).perform(click())
        onView(withId(R.id.label_name)).check(matches(withText("名")))
    }
    @Test
    fun testEnglishBtnSave(){
        ActivityScenario.launch(SettingsActivity::class.java)
        onView(withId(R.id.settings_language_spinner)).perform(scrollTo()).perform(click())
        onView(withText("english")).perform(scrollTo()).perform(click())
        onView(withId(R.id.setting_btn_save)).perform(scrollTo()).perform(click())
        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.btn_addcat)).perform(click())
        onView(withId(R.id.label_name)).check(matches(withText("Name")))
    }
}