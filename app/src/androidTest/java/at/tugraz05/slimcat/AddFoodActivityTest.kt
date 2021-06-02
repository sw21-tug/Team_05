package at.tugraz05.slimcat


import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import junit.framework.TestCase
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddFoodActivityTest : TestCase() {
    private val inputfields = arrayOf(R.id.addFood_name, R.id.addFood_protein, R.id.addFood_fat, R.id.addFood_ash, R.id.addFood_fiber, R.id.addFood_water)

    @Test
    fun hasAllInputFields() {
        ActivityScenario.launch(AddFoodActivity::class.java)
        inputfields.forEach { onView(withId(it)) }
    }

    @Test
    fun inputFieldsEditable() {
        ActivityScenario.launch(AddFoodActivity::class.java)
        (inputfields).forEach { onView(withId(it)).check(matches(isClickable())) }
    }
}