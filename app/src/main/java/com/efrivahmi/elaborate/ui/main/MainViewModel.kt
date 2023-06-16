package com.efrivahmi.elaborate.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.efrivahmi.elaborate.data.model.EditProfile
import com.efrivahmi.elaborate.data.model.UserModel
import com.efrivahmi.elaborate.data.model.UserRegister
import com.efrivahmi.elaborate.data.repository.DataSource
import com.efrivahmi.elaborate.data.response.EditProfileResponse
import com.efrivahmi.elaborate.data.response.SignUp
import com.efrivahmi.elaborate.data.response.VerifyCode
import com.efrivahmi.elaborate.utils.HelperToast
import kotlinx.coroutines.launch

class MainViewModel(private val dataSource: DataSource): ViewModel() {
    val editProfile: LiveData<EditProfileResponse> = dataSource.editProfileResult
    val isLoading: MutableLiveData<Boolean> = dataSource.isLoading
    val toastText: LiveData<HelperToast<String>> = dataSource.toastText

    fun uploadEditData(userId: String, username: String, email: String, phone: String, address: String) {
        val user = EditProfile(username, email, phone, address)
        viewModelScope.launch {
            dataSource.uploadEditProfile(userId, user)
        }
    }
    
    fun getUser(): LiveData<UserModel> {
        return dataSource.getUser()
    }

    fun logout() {
        viewModelScope.launch {
            dataSource.logout()
        }
    }
}