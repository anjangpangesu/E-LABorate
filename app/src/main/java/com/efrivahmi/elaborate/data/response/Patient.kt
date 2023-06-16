package com.efrivahmi.elaborate.data.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Patient(
    val error: Boolean,
    val message: String,
    val userData: UserData
): Parcelable {
    @Parcelize
    data class UserData(
        val address: String,
        val email: String,
        val phone: String,
        val token: String,
        val userId: String,
        val username: String
    ): Parcelable
}