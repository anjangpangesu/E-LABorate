package com.efrivahmi.elaborate.data.api

import com.efrivahmi.elaborate.data.model.ForgotPassword
import com.efrivahmi.elaborate.data.model.ResetPassword
import com.efrivahmi.elaborate.data.model.UserLogin
import com.efrivahmi.elaborate.data.model.UserRegister
import com.efrivahmi.elaborate.data.model.Verify
import com.efrivahmi.elaborate.data.model.Diagnose
import com.efrivahmi.elaborate.data.model.EditProfile
import com.efrivahmi.elaborate.data.response.DiagnoseResponse
import com.efrivahmi.elaborate.data.response.Doctor
import com.efrivahmi.elaborate.data.response.EditProfileResponse
import com.efrivahmi.elaborate.data.response.FpResponse
import com.efrivahmi.elaborate.data.response.RpResponse
import com.efrivahmi.elaborate.data.response.SignIn
import com.efrivahmi.elaborate.data.response.SignUp
import com.efrivahmi.elaborate.data.response.VerifyCode
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST("/signup")
    fun signUp(
        @Body user: UserRegister
    ): Call<SignUp>

    @POST("/signin")
    fun signIn(
        @Body user: UserLogin
    ): Call<SignIn>

    @POST("/forgot-password")
    fun forgotPassword(
        @Body user: ForgotPassword
    ): Call<FpResponse>

    @POST("/verify-code")
    fun verifyCode(
        @Body user: Verify
    ): Call<VerifyCode>

    @POST("/reset-password")
    fun resetPassword(
        @Body request: ResetPassword
    ): Call<RpResponse>

    @POST("/{userId}/profile/edit")
    fun editProfile(
        @Path("userId") userId: String,
        @Body user: EditProfile
    ): Call<EditProfileResponse>

    @GET("/{userId}/doctor-list")
    fun getDoctorList(
        @Path("userId") userId: String
    ): Call<ResponseBody>

    @GET("/{userId}/workout-list")
    fun getWorkoutList(
        @Path("userId") userId: String
    ): Call<ResponseBody>
}