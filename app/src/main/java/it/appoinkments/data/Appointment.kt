package it.appoinkments.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.*

@Entity(tableName = "appointment")
@TypeConverters(DateConverter::class, AppointmentTypeConverter::class)
data class Appointment (@PrimaryKey(autoGenerate = true) var aid: Int = 0,
                        var load_id: Long,
                        var date: Date,
                        var type: AppointmentType,
                        var farmer: String,
                        var nr_pigs: Int,
                        var round: String,
                        var completed: Boolean)