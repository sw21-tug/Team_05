package at.tugraz05.slimcat

import android.widget.LinearLayout
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.google.firebase.database.DataSnapshot
import junit.framework.TestCase
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.kotlin.any


/**
 * All tests instantiating MainActivity must mock the database!
 * At least use: `DatabaseHelper.mock(Mockito.mock(DatabaseHelper::class.java))`
 * https://github.com/mockito/mockito-kotlin/wiki/Mocking-and-verifying
 */
@RunWith(AndroidJUnit4::class)
class FoodListActivityTest : TestCase() {
    @Test
    fun appLaunchesSuccessfully() {
        DatabaseHelper.mock(Mockito.mock(DatabaseHelper::class.java))
        ActivityScenario.launch(FoodListActivity::class.java)
    }

    @Test
    fun addFoodButtonIsDisplayed() {
        DatabaseHelper.mock(Mockito.mock(DatabaseHelper::class.java))
        ActivityScenario.launch(FoodListActivity::class.java)
        onView(withId(R.id.btn_addfood)).check(matches(isDisplayed()))
    }

    @Test
    fun foodsAreDisplayedFromGivenList() {
        DatabaseHelper.mock(Mockito.mock(DatabaseHelper::class.java))
        val scenario = ActivityScenario.launch(FoodListActivity::class.java)
        val foods = listOf(
            FoodDetailsDataClass("Sheba", 20.0, 20.0, 20.0, 20.0, 10.0, 1),
            FoodDetailsDataClass("Shea2", 20.0, 20.0, 20.0, 20.0, 10.0, 1)
        )
        scenario.onActivity {
            it.displayFoods(foods)
        }
        onView(withId(R.id.food_scroll_content)).perform(waitFor<LinearLayout> { it.childCount == foods.size })
    }

    @Test
    fun checkThatMockWorks() {
        val foods = listOf(
            FoodDetailsDataClass("Sheba", 20.0, 20.0, 20.0, 20.0, 10.0, 1),
            FoodDetailsDataClass("Shea2", 20.0, 20.0, 20.0, 20.0, 10.0, 1)
        )
        val mock = Mockito.mock(DatabaseHelper::class.java)

        Mockito.doReturn(foods).`when`(mock).readUserFoods()
        DatabaseHelper.mock(mock)

        assertEquals(foods, mock.readUserFoods())
    }
}