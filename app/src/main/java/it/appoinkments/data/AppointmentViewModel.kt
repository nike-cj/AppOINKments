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
        var c : Calendar = Calendar.getInstance()
        return dao.findByDay(SimpleDateFormat("dd/MM/yyyy").format(c.time))
    }
}