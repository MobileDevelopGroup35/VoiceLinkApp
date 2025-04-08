package com.l4kt.voicelink.util

import androidx.room.TypeConverter
import java.util.Date

/**
 * Type converters for Room database to convert between Date and Long types.
 * These methods are called by Room through reflection, so IDE might incorrectly
 * mark them as unused.
 */
class DateConverter {
    @TypeConverter
    @Suppress("unused")
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    @Suppress("unused")
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}