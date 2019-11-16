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
}