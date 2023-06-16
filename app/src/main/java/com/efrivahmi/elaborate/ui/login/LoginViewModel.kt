package com.efrivahmi.elaborate.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.efrivahmi.elaborate.data.model.UserLogin
import com.efrivahmi.elaborate.data.model.UserModel
import com.efrivahmi.elaborate.data.repository.DataSource
import com.efrivahmi.elaborate.data.response.SignIn
import com.efrivahmi.elaborate.utils.HelperToast
import kotlinx.coroutines.launch

class LoginViewModel(private val dataSource: DataSource) : ViewModel() {
    val login: LiveData<SignIn> = dataSource.login
    val isLoading: MutableLiveData<Boolean> = dataSource.isLoading
    val toast: LiveData<HelperToast<String>> = dataSource.toastText

    fun uploadLoginData(email: String, password: String) {
        val user = UserLogin(email,  password)
        dataSource.uploadLogin(user)
    }

    fun saveSession(session: UserModel) {
        viewModelScope.launch {
            dataSource.saveUser(session)
        }
    }

    fun login() {
        viewModelScope.launch {
            dataSource.login()
        }
    }
}
