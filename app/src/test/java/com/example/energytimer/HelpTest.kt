package com.example.energytimer

import com.example.energytimer.tools.Help.Companion.formatFromMilliseconds
import com.example.energytimer.tools.Help.Companion.formatFromSeconds
import com.example.energytimer.tools.Help.Companion.parseMillisecondsFromString
import org.junit.Assert
import org.junit.Test

class HelpTest {
	@Test
	fun format_time_from_zero_seconds() {
		val formattedTime = formatFromMilliseconds(0)
		Assert.assertEquals("00:00", formattedTime)
	}

	@Test
	fun format_time_from_ten_seconds() {
		val formattedTime = formatFromMilliseconds(10000)
		Assert.assertEquals("00:10", formattedTime)
	}

	@Test
	fun format_time_from_one_hour() {
		val formattedTime = formatFromMilliseconds(3600000)
		Assert.assertEquals("01:00:00", formattedTime)
	}

	@Test
	fun format_time_from_one_hour_in_seconds() {
		val formattedTime = formatFromSeconds(3600)
		Assert.assertEquals("01:00:00", formattedTime)
	}

	@Test
	fun parse_day_from_string() {
		val formattedTime = parseMillisecondsFromString("01:00:00:00")
		Assert.assertEquals(86400000, formattedTime)
	}
	@Test
	fun parse_hour_from_string() {
		val formattedTime = parseMillisecondsFromString("01:00:00")
		Assert.assertEquals(3600000, formattedTime)
	}

	@Test
	fun parse_minute_from_string() {
		val formattedTime = parseMillisecondsFromString("01:00")
		Assert.assertEquals(60000, formattedTime)
	}

	@Test
	fun parse_second_from_string() {
		val formattedTime = parseMillisecondsFromString("00:00:00:01")
		Assert.assertEquals(1000, formattedTime)
	}
}