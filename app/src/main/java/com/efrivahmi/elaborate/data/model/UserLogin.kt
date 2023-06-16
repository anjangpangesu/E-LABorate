package com.efrivahmi.elaborate.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
@Parcelize
data class UserLogin (
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
): Parcelable