package com.efrivahmi.elaborate.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Verify(
    @SerializedName("email")
    val email: String,
    @SerializedName("verificationCode")
    val verificationCode: String
): Parcelable
