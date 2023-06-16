package com.efrivahmi.elaborate.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.efrivahmi.elaborate.data.repository.DataSource
import com.efrivahmi.elaborate.ui.login.LoginViewModel
import com.efrivahmi.elaborate.ui.login.editpassword.EditPasswordViewModel
import com.efrivahmi.elaborate.ui.login.forget.ForgetPasswordViewModel
import com.efrivahmi.elaborate.ui.main.MainViewModel
import com.efrivahmi.elaborate.ui.register.RegisterViewModel
import java.lang.IllegalArgumentException

class ViewModelFactory(private val pref: DataSource) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(pref) as T
            }
            modelClass.isAssignableFrom(ForgetPasswordViewModel::class.java) -> {
                ForgetPasswordViewModel(pref) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(pref) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(pref) as T
            }
            modelClass.isAssignableFrom(EditPasswordViewModel::class.java) -> {
                EditPasswordViewModel(pref) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory {
            return instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.labRepository(context))
            }.also { instance = it }
        }
    }
}