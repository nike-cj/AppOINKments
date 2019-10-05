package it.appoinkments.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AppointmentDAO {
    @get:Query("SELECT * FROM appointment")
    val all: List<Appointment>

    @Query("SELECT * FROM appointment ORDER BY date")
    fun sorted(): List<Appointment>

    @Query("SELECT * FROM appointment where farmer LIKE :name")
    fun findByFarmer(name: String): List<Appointment>

    @Query("SELECT COUNT(*) from appointment")
    fun countAppointments(): Int

    @Insert
    fun insertAll(vararg appointments: Appointment)

    @Delete
    fun delete(user: Appointment)
}