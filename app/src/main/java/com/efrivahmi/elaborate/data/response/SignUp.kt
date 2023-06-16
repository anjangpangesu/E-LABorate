package com.efrivahmi.elaborate.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
@Parcelize
data class SignUp(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String
): Parcelable