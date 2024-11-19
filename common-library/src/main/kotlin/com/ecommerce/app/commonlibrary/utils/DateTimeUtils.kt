package com.ecommerce.app.commonlibrary.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DateTimeUtils {

    private const val DEFAULT_PATTERN = "dd-MM-yyyy_HH-mm-ss"

    @JvmStatic
    fun format(dateTime: LocalDateTime): String {
        return format(dateTime, DEFAULT_PATTERN)
    }

    @JvmStatic
    fun format(dateTime: LocalDateTime, pattern: String): String {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        return dateTime.format(formatter)
    }
}
