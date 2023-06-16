package com.efrivahmi.elaborate.data.api.ml

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.efrivahmi.elaborate.data.model.Diagnose
import com.efrivahmi.elaborate.data.model.UserModel
import com.efrivahmi.elaborate.data.preference.UserPreference
import com.efrivahmi.elaborate.data.repository.DataSource
import com.efrivahmi.elaborate.data.response.DResponse
import com.efrivahmi.elaborate.data.response.DiagnoseResponse
import com.efrivahmi.elaborate.utils.HelperToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DataSourceDiagnose private constructor(
    private val pref: UserPreference,
    private val apiServiceMl: ApiServiceMl
) {
    companion object {
        private const val TAG = "DataSourceMl"
        @Volatile
        private var instance: DataSourceDiagnose? = null
        fun getInstance(
            pref: UserPreference,
            apiServiceMl: ApiServiceMl
        ): DataSourceDiagnose =
            instance ?: synchronized(this) {
                instance ?: DataSourceDiagnose(pref, apiServiceMl).also { instance = it }
            }
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: MutableLiveData<Boolean> = _isLoading

    private val _toastText = MutableLiveData<HelperToast<String>>()
    val toastText: LiveData<HelperToast<String>> = _toastText

    private val _diagnose = MutableLiveData<DResponse>()
    val diagnose: LiveData<DResponse> = _diagnose

    private val _resultDiagnose = MutableLiveData<DiagnoseResponse>()
    val diagnoseResult: LiveData<DiagnoseResponse> = _resultDiagnose

    fun diagnoseClient(userId: String, diagnose: Diagnose) {
        _isLoading.value = true
        val client = apiServiceMl.submitDiagnosticForm(userId, diagnose)
        client.enqueue(object : Callback<DResponse> {
            override fun onResponse(call: Call<DResponse>, response: Response<DResponse>) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    _diagnose.value = response.body()
                    _toastText.value = HelperToast("Diagnosis successful")
                } else {
                    _toastText.value = HelperToast(response.message().toString())
                    Log.e(DataSource.TAG, "on Failure!: ${response.message()}, ${response.body()?.toString()}")
                }
            }

            override fun onFailure(call: Call<DResponse>, t: Throwable) {
                _isLoading.value = false
                _toastText.value = t.localizedMessage?.let { HelperToast(it) }
                Log.e(TAG, "Failed Upload Data: ${t.localizedMessage}")
            }
        })
    }

    fun getDiagnosticResults(userId: String, diagnosisId: String) {
        _isLoading.value = true

        val client = apiServiceMl.getDiagnosticResults(userId, diagnosisId)
        client.enqueue(object : Callback<DiagnoseResponse> {
            override fun onResponse(
                call: Call<DiagnoseResponse>,
                response: Response<DiagnoseResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    _resultDiagnose.value = response.body()
                    _toastText.value = HelperToast("Diagnosis successful")
                } else {
                    _toastText.value = HelperToast(response.message().toString())
                    Log.e(DataSource.TAG, "on Failure!: ${response.message()}, ${response.body()?.toString()}")
                }
            }

            override fun onFailure(call: Call<DiagnoseResponse>, t: Throwable) {
                _isLoading.value = false
                _toastText.value = t.localizedMessage?.let { HelperToast(it) }
                Log.e(TAG, "Failed Get Result: ${t.localizedMessage}")
            }
        })
    }

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    suspend fun saveDiagnose(dResponse: DResponse) {
        pref.saveDiagnose(dResponse)
    }

    suspend fun saveDiagnoseRequest(diagnose: Diagnose) {
        pref.saveDiagnoseRequest(diagnose)
    }

    fun getDiagnoseId(): LiveData<DResponse> {
        return pref.getDiagnose().asLiveData()
    }

    /*fun getSavedDiagnose(): LiveData<Diagnose> {
        return pref.getSavedDiagnose().asLiveData()
    }*/
}