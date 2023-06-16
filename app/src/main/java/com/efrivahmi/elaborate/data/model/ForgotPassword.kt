package com.efrivahmi.elaborate.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ForgotPassword (
    @SerializedName("email")
    val email: String
): Parcelable
