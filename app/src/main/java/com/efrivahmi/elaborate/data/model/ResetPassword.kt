package com.efrivahmi.elaborate.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResetPassword(
    @SerializedName("resetToken")
    var resetToken: String,
    @SerializedName("newPassword")
    val newPassword: String
): Parcelable
