package com.efrivahmi.elaborate.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.efrivahmi.elaborate.data.model.UserRegister
import com.efrivahmi.elaborate.data.repository.DataSource
import com.efrivahmi.elaborate.data.response.SignUp
import kotlinx.coroutines.launch

class RegisterViewModel(private val dataSource: DataSource) : ViewModel() {
    val regis: LiveData<SignUp> = dataSource.registerResult
    val isLoading: MutableLiveData<Boolean> = dataSource.isLoading

    fun uploadRegisData(username: String, email: String, password: String, confirmPassword: String) {
        val user = UserRegister(username, email, password, confirmPassword)
        viewModelScope.launch {
            dataSource.registerClient(user)
        }
    }
}