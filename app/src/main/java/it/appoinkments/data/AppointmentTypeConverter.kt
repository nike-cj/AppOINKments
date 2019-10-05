package it.appoinkments.data

import androidx.room.TypeConverter

object AppointmentTypeConverter {

    @TypeConverter
    @JvmStatic
    fun toDatabase(type : AppointmentType): String {
        return when (type) {
            AppointmentType.vaccination_first -> "I vaccinazione"
            AppointmentType.vaccination_second -> "II vaccinazione"
            AppointmentType.vaccination_third -> "III vaccinazione"
            AppointmentType.vermicide_first -> "I sverminante"
            AppointmentType.vermicide_second -> "II sverminante"
        }
    }

    @TypeConverter
    @JvmStatic
    fun fromDatabase(str: String) : AppointmentType {
        return when (str) {
            "I vaccinazione" -> AppointmentType.vaccination_first
            "II vaccinazione" -> AppointmentType.vaccination_second
            "III vaccinazione" -> AppointmentType.vaccination_third
            "I sverminante" -> AppointmentType.vermicide_first
            "II sverminante" -> AppointmentType.vermicide_second
            else -> AppointmentType.vaccination_first
        }
    }
}