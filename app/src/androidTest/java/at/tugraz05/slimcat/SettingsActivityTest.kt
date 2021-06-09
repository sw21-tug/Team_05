package at.tugraz05.slimcat

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.service.autofill.Validators.not
import android.util.Log
import android.widget.*
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.junit.Test
import org.junit.runner.RunWith
import android.widget.SeekBar
import android.widget.TextView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.matcher.ViewMatchers.*
import com.google.firebase.database.DataSnapshot

import junit.framework.TestCase
import org.hamcrest.CoreMatchers
import org.hamcrest.Matchers
import org.mockito.Mockito
import org.mockito.kotlin.any
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
        Mockito.`when`(context.getSharedPreferences(Constants.USER_PREFS, MODE_PRIVATE)).thenReturn(sharedPreferences)
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
            assertEquals(0, user.gender)
            assertEquals(0, user.language)
            assertEquals(0, user.unit)
            assertEquals("testimage", user.image)
        }
    }

    @Test
    fun testMandarinBtnSave() {
        val targetContext: Context = ApplicationProvider.getApplicationContext()
        DatabaseHelper.mock(Mockito.mock(DatabaseHelper::class.java))
        ActivityScenario.launch(SettingsActivity::class.java)
        onView(withId(R.id.settings_language_spinner)).perform(scrollTo()).perform(click())
        onView(withText(targetContext.resources.getStringArray(R.array.settings_language)[1])).perform(scrollTo()).perform(click())
        onView(withId(R.id.setting_btn_save)).perform(scrollTo()).perform(click())
        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.btn_addcat)).perform(click())
        onView(withId(R.id.label_name)).check(matches(withText("名")))
    }

    @Test
    fun testEnglishBtnSave() {
        val targetContext: Context = ApplicationProvider.getApplicationContext()
        DatabaseHelper.mock(Mockito.mock(DatabaseHelper::class.java))
        ActivityScenario.launch(SettingsActivity::class.java)
        onView(withId(R.id.settings_language_spinner)).perform(scrollTo()).perform(click())
        onView(withText(targetContext.resources.getStringArray(R.array.settings_language)[0])).perform(scrollTo()).perform(click())
        onView(withId(R.id.setting_btn_save)).perform(scrollTo()).perform(click())
        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.btn_addcat)).perform(click())
        onView(withId(R.id.label_name)).check(matches(withText("Name")))
    }

    @Test
    fun testMetricImperialChanges(){
        ActivityScenario.launch(SettingsActivity::class.java)
        onView(withId(R.id.settings_unit_of_measurement_lbs)).perform(click())
        onView(withId(R.id.settings_seek_measurement)).perform(waitFor<SeekBar> { it.progress == SettingsActivity.IMPERIAL })
        onView(withId(R.id.setting_btn_save)).perform(scrollTo()).perform(click())

        val cats = listOf(CatDataClass(name = "Jeffrey", age = 5, weight = 2.5))
        val foods = listOf(FoodDetailsDataClass(), FoodDetailsDataClass())
        val mock = Mockito.mock(DatabaseHelper::class.java)
        val snap = Mockito.mock(DataSnapshot::class.java)

        Mockito.doAnswer { (it.arguments[1] as (DataSnapshot) -> Unit)(snap) }.`when`(mock).addValueEventListener(
            any(), any()
        )
        Mockito.doReturn(cats).`when`(mock).readUserCats()
        Mockito.doReturn(foods).`when`(mock).readUserFoods()
        DatabaseHelper.mock(mock)

        ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.scroll_content)).perform(waitFor<LinearLayout> { it.childCount == cats.size })
        onView(withId(R.id.accordion_food_list)).perform(waitFor<TableLayout> { it.childCount == foods.size })

        onView(withId(R.id.name_cat)).perform(scrollTo()).perform(click())

        onView(
            Matchers.allOf(
                withParent(withPositionInParent(R.id.accordion_food_list, 0)),
                withId(R.id.text_food_cal)
            )
        ).perform(scrollTo()).check(matches(withSubstring("lbs")))
    }
}