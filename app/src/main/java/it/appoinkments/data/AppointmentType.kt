package it.appoinkments.data

import androidx.room.TypeConverter


enum class AppointmentType {
    vaccination_first,
    vaccination_second,
    vaccination_third,

    vermicide_first,
    vermicide_second;

    override fun toString(): String {
        return when (this) {
            vaccination_first -> "I vaccinazione"
            vaccination_second -> "II vaccinazione"
            vaccination_third -> "III vaccinazione"
            vermicide_first -> "Ivomec"
            vermicide_second -> "Pigfen"
        }
    }
}
