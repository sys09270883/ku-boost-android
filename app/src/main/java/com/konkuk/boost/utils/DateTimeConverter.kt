package com.konkuk.boost.utils

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

object DateTimeConverter {
    fun currentTime(): String = formattedTime("yyyyMMddHHmmss")

    fun currentYear(): String = formattedTime("yyyy")

    fun today(): String = formattedTime("yyyyMMdd")

    private fun formattedTime(pattern: String): String {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val formatter = DateTimeFormatter.ofPattern(pattern)
            val instant = Instant.ofEpochMilli(System.currentTimeMillis())
            val date = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
            return formatter.format(date)
        }

        val formatter = SimpleDateFormat(pattern, Locale.KOREA)
        val calendar = Calendar.getInstance()
        return formatter.format(calendar.time)
    }
}