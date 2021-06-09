package at.tugraz05.slimcat

import androidx.preference.PreferenceManager
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.TestCase
import org.hamcrest.core.IsNot
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

    @Test
    fun buttonWeekdaysAreDisplayed() {
        ActivityScenario.launch(NotificationsActivity::class.java)
        Espresso.onView(ViewMatchers.withId(R.id.chk_Monday))
        Espresso.onView(ViewMatchers.withId(R.id.chk_Tuesday))
        Espresso.onView(ViewMatchers.withId(R.id.chk_Wednesday))
        Espresso.onView(ViewMatchers.withId(R.id.chk_Thursday))
        Espresso.onView(ViewMatchers.withId(R.id.chk_Friday))
        Espresso.onView(ViewMatchers.withId(R.id.chk_Saturday))
        Espresso.onView(ViewMatchers.withId(R.id.chk_Sunday))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun btnWeekdaysAreClickable() {
        ActivityScenario.launch(NotificationsActivity::class.java)
        Espresso.onView(ViewMatchers.withId(R.id.chk_Monday))
        Espresso.onView(ViewMatchers.withId(R.id.chk_Tuesday))
        Espresso.onView(ViewMatchers.withId(R.id.chk_Wednesday))
        Espresso.onView(ViewMatchers.withId(R.id.chk_Thursday))
        Espresso.onView(ViewMatchers.withId(R.id.chk_Friday))
        Espresso.onView(ViewMatchers.withId(R.id.chk_Saturday))
        Espresso.onView(ViewMatchers.withId(R.id.chk_Sunday))
            .perform(ViewActions.scrollTo()).perform(ViewActions.click())
    }

    @Test
    fun notificationButtonIsDisplayed() {
        val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
        val preferencesEditor = PreferenceManager.getDefaultSharedPreferences(targetContext).edit()
        preferencesEditor.clear()
        ActivityScenario.launch(NotificationsActivity::class.java)
        Espresso.onView(ViewMatchers.withId(R.id.action_button_notification))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun notificationButtonTest() {
        val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
        val preferencesEditor = PreferenceManager.getDefaultSharedPreferences(targetContext).edit()
        preferencesEditor.clear()
        ActivityScenario.launch(NotificationsActivity::class.java)
        Espresso.onView(ViewMatchers.withId(R.id.action_button_notification))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.txt_timePicker))
            .check(ViewAssertions.matches(IsNot.not(ViewMatchers.isDisplayed())))
        Espresso.onView(ViewMatchers.withId(R.id.txt_notifications))
            .check(ViewAssertions.matches(IsNot.not(ViewMatchers.isDisplayed())))
        Espresso.onView(ViewMatchers.withId(R.id.btn_timePicker))
            .check(ViewAssertions.matches(IsNot.not(ViewMatchers.isDisplayed())))
        Espresso.onView(ViewMatchers.withId(R.id.chk_Monday))
            .check(ViewAssertions.matches(IsNot.not(ViewMatchers.isDisplayed())))
        Espresso.onView(ViewMatchers.withId(R.id.chk_Tuesday))
            .check(ViewAssertions.matches(IsNot.not(ViewMatchers.isDisplayed())))
        Espresso.onView(ViewMatchers.withId(R.id.chk_Wednesday))
            .check(ViewAssertions.matches(IsNot.not(ViewMatchers.isDisplayed())))
        Espresso.onView(ViewMatchers.withId(R.id.chk_Thursday))
            .check(ViewAssertions.matches(IsNot.not(ViewMatchers.isDisplayed())))
        Espresso.onView(ViewMatchers.withId(R.id.chk_Friday))
            .check(ViewAssertions.matches(IsNot.not(ViewMatchers.isDisplayed())))
        Espresso.onView(ViewMatchers.withId(R.id.chk_Saturday))
            .check(ViewAssertions.matches(IsNot.not(ViewMatchers.isDisplayed())))
        Espresso.onView(ViewMatchers.withId(R.id.chk_Sunday))
            .check(ViewAssertions.matches(IsNot.not(ViewMatchers.isDisplayed())))
    }
}