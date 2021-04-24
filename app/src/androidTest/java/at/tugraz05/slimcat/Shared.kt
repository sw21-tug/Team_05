package at.tugraz05.slimcat

import android.view.View
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import org.hamcrest.CoreMatchers
import org.hamcrest.Matcher

fun <T>callMethod(message: String = "", someMethod: (view: T) -> Unit): ViewAction {
    return object: ViewAction {
        override fun getDescription(): String {
            return if(message.isBlank()) someMethod.toString() else message
        }

        override fun getConstraints(): Matcher<View> {
            return isEnabled()
        }

        override fun perform(uiController: UiController?, view: View?) {
            someMethod(view!! as T)
        }
    }
}

fun <T>assertView(someMethod: (view: T) -> Unit): ViewAssertion {
    return ViewAssertion { view, noViewFoundException ->
        if (noViewFoundException != null)
            throw noViewFoundException
        someMethod(view!! as T)
    }
}

fun isVisibility(vis: Int): ViewAssertion {
    return ViewAssertion { view, noViewFoundException ->
        if (noViewFoundException != null)
            throw noViewFoundException
        assertThat("Visibility matches", view?.visibility, CoreMatchers.equalTo(vis))
    }
}