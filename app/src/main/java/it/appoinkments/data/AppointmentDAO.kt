package it.appoinkments.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AppointmentDAO {
    @get:Query("SELECT * FROM appointment")
    val all: List<Appointment>

    @Query("SELECT * FROM appointment ORDER BY date, type")
    fun sorted(): List<Appointment>

    @Query("SELECT * FROM appointment WHERE completed = 0 ORDER BY date, type")
    fun not_completed(): List<Appointment>

    @Query("SELECT * FROM appointment where farmer LIKE :name")
    fun findByFarmer(name: String): List<Appointment>

    @Query("SELECT * FROM appointment where date BETWEEN :dayst AND :dayet")
    fun findByDay(dayst: Long, dayet: Long): List<Appointment>

    @Query("SELECT * FROM appointment where date BETWEEN :dayst AND :dayet AND completed = 0")
    fun findToDo(dayst: Long, dayet: Long): List<Appointment>

    @Query("SELECT COUNT(*) from appointment")
    fun countAppointments(): Int

    @Query("UPDATE appointment SET completed = 1 WHERE aid = :appointment_id")
    fun markCompleted(appointment_id: Int)

    @Query("UPDATE appointment SET completed = 1 where load_id = :load_id AND type LIKE :appointment_type")
    fun markCompleted(load_id: Long, appointment_type: String)

    @Query("UPDATE appointment SET completed = 0 where load_id = :load_id AND type LIKE :appointment_type")
    fun markNotCompleted(load_id: Long, appointment_type: String)

    @Query("SELECT completed FROM appointment where load_id = :load_id AND type LIKE :appointment_type")
    fun isCompleted(load_id: Long, appointment_type: String): Boolean

//    @Query("SELECT * FROM appointment WHERE farmer = :farmer and date = :date and type = :appointment_type and nr_pigs = :nr_pigs and round = :round ")
//    fun getByDetails(farmer: String, date: String, appointment_type: String, nr_pigs: Int, round: String)

    @Insert
    fun insert(appointment: Appointment) : Long

    @Insert
    fun insertAll(vararg appointments: Appointment)

    @Delete
    fun delete(user: Appointment)
}