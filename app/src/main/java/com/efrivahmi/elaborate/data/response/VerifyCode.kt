package com.efrivahmi.elaborate.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class VerifyCode(
    @SerializedName("code")
    val code: Int,
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("userId")
    val userId: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("resetToken")
    val resetToken: String,
    @SerializedName("verificationCodeDeleted")
    val verificationCodeDeleted: Boolean
): Parcelable
