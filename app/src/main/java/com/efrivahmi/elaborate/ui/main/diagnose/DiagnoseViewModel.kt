package com.efrivahmi.elaborate.ui.main.diagnose

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.efrivahmi.elaborate.data.api.ml.DataSourceDiagnose
import com.efrivahmi.elaborate.data.model.Diagnose
import com.efrivahmi.elaborate.data.model.UserModel
import com.efrivahmi.elaborate.data.repository.DataSource
import com.efrivahmi.elaborate.data.response.DResponse
import com.efrivahmi.elaborate.data.response.DiagnoseResponse
import com.efrivahmi.elaborate.utils.HelperToast
import kotlinx.coroutines.launch

class DiagnoseViewModel(private val dataSourceMl: DataSourceDiagnose) : ViewModel() {
    val isLoading: LiveData<Boolean> = dataSourceMl.isLoading
    val diagnoseResult: LiveData<DResponse> = dataSourceMl.diagnose
    val toastText: LiveData<HelperToast<String>> = dataSourceMl.toastText

    fun diagnoseClient(userId: String, diagnose: Diagnose) {
        viewModelScope.launch {
            dataSourceMl.diagnoseClient(userId, diagnose)
        }
    }

    fun getPatient(): LiveData<UserModel>{
        return dataSourceMl.getUser()
    }

    fun saveDiagnose(dResponse: DResponse) {
        viewModelScope.launch {
            dataSourceMl.saveDiagnose(dResponse)
        }
    }

    fun saveDiagnoseRequest(diagnose: Diagnose) {
        viewModelScope.launch {
            dataSourceMl.saveDiagnoseRequest(diagnose)
        }
    }
}