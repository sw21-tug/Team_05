package at.tugraz05.slimcat

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NotificationActivityTest : TestCase() {
    @Test
    fun buttonTimepickerIsDisplayed() {
        ActivityScenario.launch(NotificationsActivity::class.java)
        Espresso.onView(ViewMatchers.withId(R.id.btn_timePicker))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun buttonTimepickerIsClickable() {
        ActivityScenario.launch(NotificationsActivity::class.java)
        Espresso.onView(ViewMatchers.withId(R.id.btn_timePicker))
            .perform(ViewActions.scrollTo()).perform(ViewActions.click())
    }
}