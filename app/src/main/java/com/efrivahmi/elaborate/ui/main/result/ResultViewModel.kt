package com.efrivahmi.elaborate.ui.main.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.efrivahmi.elaborate.data.api.ml.DataSourceDiagnose
import com.efrivahmi.elaborate.data.model.UserModel
import com.efrivahmi.elaborate.data.response.DResponse
import com.efrivahmi.elaborate.data.response.DiagnoseResponse
import com.efrivahmi.elaborate.utils.HelperToast
import kotlinx.coroutines.launch

class ResultViewModel(private val dataSourceMl: DataSourceDiagnose) : ViewModel() {
    val isLoading: LiveData<Boolean> = dataSourceMl.isLoading
    val diagnoseResult: LiveData<DiagnoseResponse> = dataSourceMl.diagnoseResult
    val toastText: LiveData<HelperToast<String>> = dataSourceMl.toastText

    fun getResult(userId: String, diagnosisId:String){
        viewModelScope.launch {
            dataSourceMl.getDiagnosticResults(userId, diagnosisId)
        }
    }

    fun getUserId(): LiveData<UserModel> {
        return dataSourceMl.getUser()
    }

    fun getDiagnosisId(): LiveData<DResponse> {
        return dataSourceMl.getDiagnoseId()
    }

    /*fun getDataDiagnosis(): LiveData<Diagnose> {
        return dataSourceMl.getSavedDiagnose()
    }*/
}