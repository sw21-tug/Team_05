package at.tugraz05.slimcat

import android.content.Intent
import android.util.Log
import android.widget.SeekBar
import android.widget.TableLayout
import androidx.core.os.bundleOf
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase
import org.hamcrest.Matchers.allOf
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.notNull
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class TrackFoodTest : TestCase() {
    @Test
    fun trackFood() {
        val cat = CatDataClass(name = "test", race = "liger", date_of_birth = "2019-5-12", size = 12.0, weight = 3.5, gender = GenderSeeker.MALE)
        val dbHelper = Mockito.mock(DatabaseHelper::class.java)
        DatabaseHelper.mock(dbHelper)
        Mockito.doAnswer {
            val cat = it.arguments[1] as CatDataClass
            Log.d("trackFood", "${it.arguments[0] as String}: $cat ${cat.calorieRecommendation}")
        }.`when`(dbHelper).editUser(
            any(), any()
        )
        cat.age = Util.calculateAge(date_of_birth = LocalDate.of(2019, 5, 12), LocalDate.now())
        cat.calorieRecommendation = Util.calculateCalories(cat)

        val intent = Intent(ApplicationProvider.getApplicationContext(), TrackFoodActivity::class.java)
        intent.putExtras(bundleOf("Cat" to cat))
        ActivityScenario.launch<TrackFoodActivity>(intent)

        cat.calorieRecommendation -= Food.foods[0].kcalPer100G*20/100 + Food.foods[1].kcalPer100G*10/100

        onView(withId(R.id.scroll_track_food)).perform(waitFor<TableLayout> { it.childCount != 0 })
        onView(allOf(withParent(withPositionInParent(R.id.scroll_track_food, 0)), withId(R.id.text_food_eaten))).perform(scrollTo()).perform(typeText("20"))
        onView(allOf(withParent(withPositionInParent(R.id.scroll_track_food, 1)), withId(R.id.text_food_eaten))).perform(scrollTo()).perform(typeText("10"))

        onView(withId(R.id.trackfood_btn_save)).perform(scrollTo()).perform(click())
        Log.d("trackFood", "$cat ${cat.calorieRecommendation}")
        Mockito.verify(dbHelper).editUser("test", cat)
    }
}