package at.tugraz05.slimcat

import android.widget.LinearLayout
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import junit.framework.TestCase
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito


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
        val listfood = arrayOf(
            FoodDetailsDataClass("Sheba", 20.0, 20.0, 20.0, 20.0, 10.0, 1),
            FoodDetailsDataClass("Shea2", 20.0, 20.0, 20.0, 20.0, 10.0, 1)
        )
        scenario.onActivity {
            it.displayFoods(listfood.toList())
        }
        onView(withId(R.id.food_scroll_content)).perform(waitFor<LinearLayout> { it.childCount == listfood.size })
    }
/*
    @Test
    fun checkThatMockWorks() {
        val cats = listOf(CatDataClass(name = "Jeffrey", age = 5, weight = 2.5), CatDataClass(name = "Johnny", age = 7, weight = 10.0))
        val mock = Mockito.mock(DatabaseHelper::class.java)

        Mockito.doReturn(cats).`when`(mock).readUserCats()
        DatabaseHelper.mock(mock)

        assertEquals(cats, mock.readUserCats())
    }

    @Test
    fun catsAreDisplayedFromDatabase() {
        val cats = listOf(CatDataClass(name = "Jeffrey", age = 5, weight = 2.5), CatDataClass(name = "Johnny", age = 7, weight = 10.0))
        val mock = Mockito.mock(DatabaseHelper::class.java)
        val snap = Mockito.mock(DataSnapshot::class.java)

        Mockito.doAnswer { (it.arguments[1] as (DataSnapshot) -> Unit)(snap) }.`when`(mock).addValueEventListener(any(), any())
        Mockito.doReturn(cats).`when`(mock).readUserCats()
        DatabaseHelper.mock(mock)

        // MainActivity uses the addValueEventListener to wait for data, then readUserCats for displaying (both mocked above)
        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.scroll_content)).perform(waitFor<LinearLayout> { it.childCount == cats.size })
    }
*/
}