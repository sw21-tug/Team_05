package at.tugraz05.slimcat

import android.view.View
import androidx.test.espresso.*
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.util.HumanReadables
import org.hamcrest.CoreMatchers
import org.hamcrest.Matcher
import java.util.concurrent.TimeoutException
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

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

fun <T>waitFor(timeout:Long = 100, someMethod: (view: T) -> Boolean): ViewAction {
    return object:ViewAction{
        override fun getConstraints(): Matcher<View> {
            return isEnabled()
        }

        override fun getDescription(): String {
            return "wait up to $timeout milliseconds"
        }

        override fun perform(uiController: UiController, view: View) {
            val endTime = System.currentTimeMillis() + timeout

            do {
                if (someMethod(view as T)) return
                uiController.loopMainThreadForAtLeast(50)
            } while (System.currentTimeMillis() < endTime)

            throw PerformException.Builder()
                .withActionDescription(description)
                .withCause(TimeoutException("Waited $timeout milliseconds"))
                .withViewDescription(HumanReadables.describe(view))
                .build()
        }
    }

}