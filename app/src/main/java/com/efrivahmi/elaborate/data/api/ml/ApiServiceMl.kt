package com.efrivahmi.elaborate.data.api.ml

import com.efrivahmi.elaborate.data.model.Diagnose
import com.efrivahmi.elaborate.data.response.DResponse
import com.efrivahmi.elaborate.data.response.DiagnoseResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServiceMl {
    @POST("/{userId}/predict")
    fun submitDiagnosticForm(
        @Path("userId") userId: String,
        @Body diagnose: Diagnose
    ): Call<DResponse>

    @GET("/{userId}/diagnosis/{diagnosisId}")
    fun getDiagnosticResults(
        @Path("userId") userId: String,
        @Path("diagnosisId") diagnosisId: String
    ): Call<DiagnoseResponse>
}