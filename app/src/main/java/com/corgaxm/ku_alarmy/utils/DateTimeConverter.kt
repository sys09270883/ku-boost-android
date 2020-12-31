package com.corgaxm.ku_alarmy.utils

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object DateTimeConverter {
    fun convert(time: Long): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")
        val instant = Instant.ofEpochMilli(time)
        val date = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        return formatter.format(date)
    }

    fun currentYear(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy")
        val instant = Instant.ofEpochMilli(System.currentTimeMillis())
        val date = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        return formatter.format(date)
    }

    fun today(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val instant = Instant.ofEpochMilli(System.currentTimeMillis())
        val date = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        return formatter.format(date)
    }
}