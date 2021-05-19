package at.tugraz05.slimcat

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.core.os.bundleOf
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import junit.framework.TestCase
import org.hamcrest.CoreMatchers
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.kotlin.any
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class AddcatActivityTest : TestCase() {
    private val txtids = arrayOf(R.id.txt_name, R.id.txt_race, R.id.txt_size, R.id.txt_weight)
    private val switchids = arrayOf(
            R.id.switch_overweight, R.id.switch_hospitalized, R.id.switch_neutered,
            R.id.switch_gestation, R.id.switch_lactation,
    )
    private val ids = arrayOf(R.id.seek_gender, R.id.btn_camera) + txtids + switchids
    private val rowids = arrayOf(R.id.row_gestation, R.id.row_lactation)

    @Test
    fun hasAllInputFields() {
        ActivityScenario.launch(AddcatActivity::class.java)
        ids.forEach { onView(withId(it)) }
    }

    @Test
    fun inputFieldsEditable() {
        ActivityScenario.launch(AddcatActivity::class.java)
        (txtids + switchids).forEach { onView(withId(it)).check(matches(isClickable())) }
    }

    @Test
    fun maleLabelClickHideFemale() {
        ActivityScenario.launch(AddcatActivity::class.java)
        onView(withId(R.id.label_gender_female)).perform(scrollTo()).perform(click())
        onView(withId(R.id.label_gender_male)).perform(scrollTo()).perform(click())
        rowids.forEach { id -> onView(withId(id)).perform(waitFor<View> { it.visibility == View.GONE }) }
    }

    @Test
    fun maleSeekerChangeHideFemale() {
        ActivityScenario.launch(AddcatActivity::class.java)
        onView(withId(R.id.seek_gender)).perform(scrollTo()).perform(callMethod<SeekBar> { it.progress = GenderSeeker.FEMALE })
        onView(withId(R.id.seek_gender)).perform(scrollTo()).perform(callMethod<SeekBar> { it.progress = GenderSeeker.MALE })
        rowids.forEach { id -> onView(withId(id)).perform(waitFor<View> { it.visibility == View.GONE }) }
    }

    @Test
    fun clickingGenderLabelsChangesSeeker() {
        ActivityScenario.launch(AddcatActivity::class.java)
        onView(withId(R.id.label_gender_female)).perform(scrollTo()).perform(click())
        onView(withId(R.id.label_gender_male)).perform(scrollTo()).perform(click())
        onView(withId(R.id.seek_gender)).perform(waitFor<SeekBar> { it.progress == GenderSeeker.MALE })
        onView(withId(R.id.label_gender_female)).perform(scrollTo()).perform(click())
        onView(withId(R.id.seek_gender)).perform(waitFor<SeekBar> { it.progress == GenderSeeker.FEMALE })
    }

    @Test
    fun btnDatepickerIsClickable() {
        ActivityScenario.launch(AddcatActivity::class.java)
        onView(withId(R.id.btn_dob)).perform(scrollTo()).perform(click())
    }

    @Test
    fun btnDatepickerIsDisplayed() {
        ActivityScenario.launch(AddcatActivity::class.java)
        onView(withId(R.id.btn_dob)).check(matches(isDisplayed()))
    }

    @Test
    fun removeCatTest() {
        val dbHelper = Mockito.mock(DatabaseHelper::class.java)
        DatabaseHelper.mock(dbHelper)
        Mockito.doAnswer { Log.d("removeCatTest", it.arguments[0] as String) }.`when`(dbHelper).deleteCat("test")
        ActivityScenario.launch(AddcatActivity::class.java)
        onView(withId(R.id.txt_name)).perform(typeText("test"))
        onView(withId(R.id.btn_delete)).perform(scrollTo()).perform(click())
        Mockito.verify(dbHelper).deleteCat("test")
    }

    @Test
    fun openEditCat() {
        val cat = CatDataClass(name = "test", race = "liger", date_of_birth = "2019-5-12", size = 12.0, weight = 3.5, gender = GenderSeeker.MALE)
        val dbHelper = Mockito.mock(DatabaseHelper::class.java)
        DatabaseHelper.mock(dbHelper)
        Mockito.doAnswer { Log.d("openEditCat", it.arguments[0] as String) }.`when`(dbHelper).editUser(any(), any())
        val intent = Intent(ApplicationProvider.getApplicationContext(), AddcatActivity::class.java)
        intent.putExtras(bundleOf("Cat" to cat))
        ActivityScenario.launch<AddcatActivity>(intent)

        var obese : Boolean = true // TODO use obese calc

        cat.age = Util.calculateAge(date_of_birth = LocalDate.of(2019, 5, 12), LocalDate.now())
        cat.calorieRecommendation = Util.calculateCalories(cat, obese)

        onView(withId(R.id.txt_name)).check(matches(withText(cat.name)))
        onView(withId(R.id.txt_race)).check(matches(withText(cat.race)))
        onView(withId(R.id.txt_size)).check(matches(withText(cat.getSizeStr())))
        onView(withId(R.id.txt_weight)).check(matches(withText(cat.getWeightStr())))
        onView(withId(R.id.seek_gender)).check(assertView<SeekBar> { assertEquals(GenderSeeker.MALE, it.progress) })
        onView(withId(R.id.btn_save)).perform(scrollTo()).perform(click())
        Mockito.verify(dbHelper).editUser("test", cat)
    }

    @Test
    fun openAddCat() {
        val cat = CatDataClass(name = "test", race = "liger", size = 12.0, weight = 3.5, gender = GenderSeeker.MALE)
        val dbHelper = Mockito.mock(DatabaseHelper::class.java)
        DatabaseHelper.mock(dbHelper)
        Mockito.doAnswer { Log.d("openAddCat", (it.arguments[0] as CatDataClass).name!!) }.`when`(dbHelper).writeNewCat(any())
        ActivityScenario.launch(AddcatActivity::class.java)

        var obese : Boolean = true // TODO use obese calc

        cat.calorieRecommendation = Util.calculateCalories(cat, obese)

        // check that empty
        onView(withId(R.id.txt_name)).check(matches(withText("")))
        onView(withId(R.id.txt_race)).check(matches(withText("")))
        onView(withId(R.id.txt_size)).check(matches(withText("0.0")))
        onView(withId(R.id.txt_weight)).check(matches(withText("0.0")))
        onView(withId(R.id.seek_gender)).check(assertView<SeekBar> { assertEquals(GenderSeeker.FEMALE, it.progress) })

        // fill in cat data
        onView(withId(R.id.txt_name)).perform(scrollTo()).perform(typeText(cat.name))
        onView(withId(R.id.txt_race)).perform(scrollTo()).perform(typeText(cat.race))
        onView(withId(R.id.txt_size)).perform(scrollTo()).perform(clearText()).perform(typeText(cat.getSizeStr()))
        onView(withId(R.id.txt_weight)).perform(scrollTo()).perform(clearText()).perform(typeText(cat.getWeightStr()))
        onView(withId(R.id.seek_gender)).perform(scrollTo()).perform(callMethod<SeekBar> { it.progress = cat.gender!! })
        onView(withId(R.id.btn_save)).perform(scrollTo()).perform(click())
        Mockito.verify(dbHelper).writeNewCat(cat)
    }

    @Test
    fun weightUnitSwitch() {
        val cat = CatDataClass(name = "test", race = "liger", date_of_birth = "0", size = 3.5, weight = 3.5, gender = GenderSeeker.MALE)
        val inLbs = Util.convertKgToLbs(cat.weight)

        // switch to imperial system
        ActivityScenario.launch(SettingsActivity::class.java)
        onView(withId(R.id.settings_unit_of_measurement_lbs)).perform(click())
        onView(withId(R.id.setting_btn_save)).perform(scrollTo()).perform(click())
        val intent = Intent(ApplicationProvider.getApplicationContext(), AddcatActivity::class.java)
        intent.putExtras(bundleOf("Cat" to cat))
        ActivityScenario.launch<AddcatActivity>(intent)
        onView(withId(R.id.txt_weight)).perform(scrollTo()).check(matches(withText(inLbs.toString())))

        // switch back to metric system
        ActivityScenario.launch(SettingsActivity::class.java)
        onView(withId(R.id.settings_unit_of_measurement_kg)).perform(click())
        onView(withId(R.id.setting_btn_save)).perform(scrollTo()).perform(click())
        intent.putExtras(bundleOf("Cat" to cat))
        ActivityScenario.launch<AddcatActivity>(intent)
        onView(withId(R.id.txt_weight)).perform(scrollTo()).check(matches(withText(cat.getWeightStr())))
    }

    // after changing size from Int do Double adjust Test
    @Test
    fun sizeUnitSwitch() {
        val cat = CatDataClass(name = "test", race = "liger", date_of_birth = "0", size = 3.5, weight = 3.5, gender = GenderSeeker.MALE)
        val inInch = Util.convertCmToInch(cat.size.toDouble())

        // switch to imperial system
        ActivityScenario.launch(SettingsActivity::class.java)
        onView(withId(R.id.settings_unit_of_measurement_lbs)).perform(click())
        onView(withId(R.id.setting_btn_save)).perform(scrollTo()).perform(click())
        val intent = Intent(ApplicationProvider.getApplicationContext(), AddcatActivity::class.java)
        intent.putExtras(bundleOf("Cat" to cat))
        ActivityScenario.launch<AddcatActivity>(intent)
        onView(withId(R.id.txt_size)).perform(scrollTo()).check(matches(withText(inInch.toInt().toString())))

        // switch back to metric system
        ActivityScenario.launch(SettingsActivity::class.java)
        onView(withId(R.id.settings_unit_of_measurement_kg)).perform(click())
        onView(withId(R.id.setting_btn_save)).perform(scrollTo()).perform(click())
        intent.putExtras(bundleOf("Cat" to cat))
        ActivityScenario.launch<AddcatActivity>(intent)
        onView(withId(R.id.txt_size)).perform(scrollTo()).check(matches(withText(cat.getSizeStr())))
    }


}