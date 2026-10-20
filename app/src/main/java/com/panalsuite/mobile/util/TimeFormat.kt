package com.panalsuite.mobile.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object TimeFormat {

    private const val MINUTE_MS = 60_000L
    private const val HOUR_MS = 60 * MINUTE_MS
    private const val DAY_MS = 24 * HOUR_MS

    fun relative(timestamp: Long, now: Long = System.currentTimeMillis()): String {
        val diff = (now - timestamp).coerceAtLeast(0L)
        return when {
            diff < MINUTE_MS -> "ahora"
            diff < HOUR_MS -> "hace ${diff / MINUTE_MS} min"
            diff < DAY_MS -> "hace ${diff / HOUR_MS} h"
            else -> "hace ${diff / DAY_MS} d"
        }
    }

    fun dateTime(timestamp: Long): String =
        SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.forLanguageTag("es-CL"))
            .format(Date(timestamp))
}