package com.efrivahmi.elaborate.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.efrivahmi.elaborate.data.api.ml.DataSourceDiagnose
import com.efrivahmi.elaborate.ui.main.diagnose.DiagnoseViewModel
import com.efrivahmi.elaborate.ui.main.result.ResultViewModel
import java.lang.IllegalArgumentException

class ViewModelFactoryMl(private val pref: DataSourceDiagnose) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(DiagnoseViewModel::class.java) -> {
                DiagnoseViewModel(pref) as T
            }
            modelClass.isAssignableFrom(ResultViewModel::class.java) -> {
                ResultViewModel(pref) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactoryMl? = null
        fun getInstance(context: Context): ViewModelFactoryMl {
            return instance ?: synchronized(this) {
                instance ?: ViewModelFactoryMl(Injection.labDiagnose(context))
            }.also { instance = it }
        }
    }
}