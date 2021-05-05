package at.tugraz05.slimcat

import android.util.Log
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

    @Test
    fun checkThatMockWorks() {
        val cats = listOf(CatDataClass(name = "Jeffrey", age = 5, weight = 2.5), CatDataClass(name = "Johnny", age = 7, weight = 10.0))
        val mock = Mockito.mock(DatabaseHelper::class.java)

        Mockito.doReturn(cats).`when`(mock).readUserCats()
        DatabaseHelper.mock(mock)

        assertEquals(cats, mock.readUserCats())
    }

    @Test
    fun checkThatMockWorksWithView() {
        val cats = listOf(CatDataClass(name = "Jeffrey", age = 5, weight = 2.5), CatDataClass(name = "Johnny", age = 7, weight = 10.0))
        val mock = Mockito.mock(DatabaseHelper::class.java)
        val snap = Mockito.mock(DataSnapshot::class.java)

        Mockito.doAnswer { (it.arguments[1] as (DataSnapshot) -> Unit)(snap) }.`when`(mock).addValueEventListener(any(), any())
        Mockito.doReturn(cats).`when`(mock).readUserCats()
        DatabaseHelper.mock(mock)

        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.scroll_content)).perform(waitFor<LinearLayout> {  it.childCount == cats.size })
    }
}