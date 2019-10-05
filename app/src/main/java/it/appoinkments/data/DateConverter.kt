package it.appoinkments.data

import androidx.room.TypeConverter
import java.util.*


object DateConverter {
    @TypeConverter
    @JvmStatic
    fun fromMillisToDate(millis: Long?): Date? {
        return millis?.let {
            return Date(millis)
        }
    }

    @TypeConverter
    @JvmStatic
    fun fromDateToMillis(date: Date?): Long? {
        return date?.time
    }
}