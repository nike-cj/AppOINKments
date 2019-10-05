package it.appoinkments.data

import android.app.Application
import androidx.lifecycle.ViewModel
import java.util.*

class LoadViewModel(application : Application)  : ViewModel(){
    val dao = AppDatabase.getAppDatabase(application).loadDao()

    private fun addLoad(load: Load): Load {
        dao.insertAll(load)
        return load
    }

    fun populateWithTestData() {
        var loads = Array(50) { i ->
            var l = Load(
                farmer = "Allevatore $i",
                date_arrival = Calendar.getInstance().time,
                nr_pigs = 50 + i,
                origin = "Asti",
                round = "Capannone $i",
                date_vaccination_first = Calendar.getInstance().time,
                date_vaccination_second = Calendar.getInstance().time,
                date_vaccination_third = Calendar.getInstance().time,
                date_vermicide_first = Calendar.getInstance().time,
                date_vermicide_second = Calendar.getInstance().time
            )
            addLoad(l)
        }
    }

    fun countLoads() = dao.countLoads()

//    fun updateLoad(name: String, id: Int) = dao.updateName(name, id)

    fun getLoadsList() = dao.all
}