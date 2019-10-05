package it.appoinkments.data

import android.app.Application
import androidx.lifecycle.ViewModel
import java.util.*

class AppointmentViewModel(application : Application)  : ViewModel(){
    val dao = AppDatabase.getAppDatabase(application).appointmentDao()

    private fun addAppointment(appointment: Appointment): Appointment {
        dao.insertAll(appointment)
        return appointment
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
}