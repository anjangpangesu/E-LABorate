package com.efrivahmi.elaborate.ui.login.forget

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.efrivahmi.elaborate.data.model.ForgotPassword
import com.efrivahmi.elaborate.data.model.UserModel
import com.efrivahmi.elaborate.data.model.Verify
import com.efrivahmi.elaborate.data.repository.DataSource
import com.efrivahmi.elaborate.data.response.FpResponse
import com.efrivahmi.elaborate.data.response.VerifyCode
import com.efrivahmi.elaborate.utils.HelperToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ForgetPasswordViewModel(private val dataSource: DataSource) : ViewModel() {
    val isLoading: LiveData<Boolean> = dataSource.isLoading
    val forgot: LiveData<FpResponse> = dataSource.forgot
    val verifyCode: LiveData<VerifyCode> = dataSource.verify
    val toastText: LiveData<HelperToast<String>> = dataSource.toastText

    fun forgotPassword(email: String) {
        val user = ForgotPassword(email)
        viewModelScope.launch(Dispatchers.IO) {
            dataSource.forgotPassword(user)
        }
    }

    fun sendVerificationCode(email: String, verificationCode: String) {
        val user = Verify(email, verificationCode)
        viewModelScope.launch(Dispatchers.IO) {
            dataSource.verifyCode(user)
        }
    }

    fun getPatient(): LiveData<UserModel>{
        return dataSource.getUser()
    }

    fun saveSession(verifyCode: VerifyCode) {
        viewModelScope.launch {
            dataSource.saveResetToken(verifyCode)
        }
    }
}
