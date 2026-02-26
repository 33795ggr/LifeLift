package com.lifelift.app.core.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DateUtils {
    fun getCurrentDateTime(): String {
        return LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }
}
