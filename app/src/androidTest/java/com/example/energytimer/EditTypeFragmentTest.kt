package com.example.energytimer

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.energytimer.fragment.EditTypeFragment
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EditTypeFragmentTest {

	@Test
	fun testEventFragment() {
		val fragmentArgs = Bundle().apply {
			putInt("selectedListItem", 0)
		}
		val scenario = launchFragmentInContainer<EditTypeFragment>(fragmentArgs)
		onView(withId(R.id.max_value)).check(matches(withText("Hello World!")))
	}
}