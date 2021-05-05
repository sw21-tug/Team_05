package at.tugraz05.slimcat

import android.widget.LinearLayout
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import junit.framework.TestCase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest : TestCase() {
    @Test
    fun appLaunchesSuccessfully() {
        ActivityScenario.launch(MainActivity::class.java)

    }

    @Test
    fun addCatButtonIsDisplayed() {
        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.btn_addcat)).check(matches(isDisplayed()))
    }

    @Test
    fun addCatsWithList() {
        val scenario = ActivityScenario.launch(MainActivity::class.java)
        val cats = listOf(CatDataClass(name = "Jeffrey", age = 5, weight = 2.5), CatDataClass(name = "Johnny", age = 7, weight = 10.0), CatDataClass(name = "Katze", age = 2, weight = 1.0))
        scenario.onActivity {
            it.displayCats(cats)
        }
        onView(withId(R.id.scroll_content)).perform(waitFor<LinearLayout> {  it.childCount == cats.size })
    }
}