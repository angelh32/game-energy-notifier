package com.example.energytimer

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragment
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.energytimer.fragment.ShowTypeFragment
import com.example.energytimer.tools.Help
import com.example.energytimer.tools.Help.Companion.printLog
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ShowTypeFragmentTest {
	@Test
	fun testEventFragment() {
		val fragmentArgs = bundleOf("selectedListItem" to 0)
		val scenario = launchFragmentInContainer<ShowTypeFragment>()
		with(launchFragment<ShowTypeFragment>()) {
			onFragment { fragment ->
				assertThat(fragment.dialog).isNotNull()
				assertThat(fragment.requireDialog().isShowing).isTrue()
				fragment.dismiss()
				fragment.parentFragmentManager.executePendingTransactions()
				assertThat(fragment.dialog).isNull()
				printLog("test", "${fragment.id}")
			}
		}

		// Assumes that the dialog had a button
		// containing the text "Cancel".
//		printLog("test", onView(withId(R.id.timer_name).toString()))
	}
}