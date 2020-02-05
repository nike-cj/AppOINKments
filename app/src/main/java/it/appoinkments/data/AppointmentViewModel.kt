package it.appoinkments.data

import android.app.Application
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.*

class AppointmentViewModel(application : Application)  : ViewModel(){
    val dao = AppDatabase.getAppDatabase(application).appointmentDao()

    fun addAppointment(appointment: Appointment): Long {
        return dao.insert(appointment)
    }

    fun populateWithTestData() {
        var loads = Array(50) { i ->
            //var a = Appointmen(
            //    load_id = ,
            //    var date: Date,
            //    var type: AppointmentType,
            //    var farmer: String,
            //    var nr_pigs: Int,
            //    var round: String,
            //    var completed: Boolean
            //)
            //addAppointment(a)
        }
    }

    fun countAppointments() = dao.countAppointments()

//    fun updateAppointment(name: String, id: Int) = dao.updateName(name, id)

    fun getAppointmentsList() = dao.all

    fun getSortedList() = dao.sorted()

    fun getNotCompleted() = dao.not_completed()

    fun getByLoad(load_id: Long) = dao.getByLoad(load_id)

    fun getByLoad(load_id: Long, type: String) = dao.getByLoad(load_id, type)

    fun getTodayAppointments() : List<Appointment> {
        var date_init: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            add(Calendar.DATE, -1)  // the day before
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
        }
        var date_end: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
        }
        return dao.findByDay(date_init.timeInMillis, date_end.timeInMillis)
    }

    fun isCompleted(load_id: Long, appointment_type: AppointmentType) = dao.isCompleted(load_id, AppointmentTypeConverter.toDatabase(appointment_type))

    fun invertCompletion(load_id: Long, appointment_type: AppointmentType) {
        var type_str: String = AppointmentTypeConverter.toDatabase(appointment_type)
        if (dao.isCompleted(load_id, type_str)) {
            dao.markNotCompleted(load_id, type_str)
        } else {
            dao.markCompleted(load_id, type_str)
        }
    }

    fun markCompleted(load_id: Long, appointment_type: AppointmentType) = dao.markCompleted(load_id, AppointmentTypeConverter.toDatabase(appointment_type))

    fun markNotCompleted(load_id: Long, appointment_type: AppointmentType) = dao.markNotCompleted(load_id, AppointmentTypeConverter.toDatabase(appointment_type))

    fun update(appointment: Appointment) = dao.update(appointment)

    fun delete(load_id: Long) {
        for (appointment in getByLoad(load_id))
            dao.delete(appointment)
    }
}