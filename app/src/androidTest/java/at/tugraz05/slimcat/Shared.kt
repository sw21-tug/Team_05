package at.tugraz05.slimcat

import android.view.View
import androidx.test.espresso.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.util.HumanReadables
import org.hamcrest.CoreMatchers
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import java.util.concurrent.TimeoutException
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

inline fun <reified T>callMethod(message: String = "", noinline someMethod: (view: T) -> Unit): ViewAction {
    return object: ViewAction {
        override fun getDescription(): String {
            return if(message.isBlank()) someMethod.toString() else message
        }

        override fun getConstraints(): Matcher<View> {
            return isEnabled()
        }

        override fun perform(uiController: UiController?, view: View?) {
            someMethod(cast(view!!))
        }
    }
}

inline fun <reified T>assertView(crossinline someMethod: (view: T) -> Unit): ViewAssertion {
    return ViewAssertion { view, noViewFoundException ->
        if (noViewFoundException != null)
            throw noViewFoundException
        someMethod(cast(view!!))
    }
}

fun isVisibility(vis: Int): ViewAssertion {
    return ViewAssertion { view, noViewFoundException ->
        if (noViewFoundException != null)
            throw noViewFoundException
        assertThat("Visibility matches", view?.visibility, CoreMatchers.equalTo(vis))
    }
}

inline fun <reified T>waitFor(timeout:Long = 1000, crossinline someMethod: (view: T) -> Boolean): ViewAction {
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
                if (someMethod(cast(view))) return
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

fun withPositionInParent(parentViewId: Int, position: Int): Matcher<View> {
    return allOf(withParent(withId(parentViewId)), withParentIndex(position))
}

inline fun <reified T> cast(value: Any): T {
    return when (value is T) {
        true -> value
        else -> throw IllegalArgumentException("Unsupported Cast")
    }
}