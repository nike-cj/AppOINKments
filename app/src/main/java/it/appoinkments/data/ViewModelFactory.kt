package it.appoinkments.data

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(private val mApplication: Application) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass == AppointmentViewModel::class.java) {
            return AppointmentViewModel(mApplication) as T
        }
        else if (modelClass == LoadViewModel::class.java) {
            return LoadViewModel(mApplication) as T
        }

        // to suppress warnings
        return LoadViewModel(mApplication) as T
    }
}