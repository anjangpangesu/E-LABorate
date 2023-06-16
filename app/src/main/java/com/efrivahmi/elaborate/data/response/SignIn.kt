package com.efrivahmi.elaborate.data.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SignIn(
    val code: Int,
    val error: Boolean,
    val message: String,
    val userData: UserData
): Parcelable
@Parcelize
data class UserData (
    val userId: String,
    val username: String,
    val email: String,
    val token: String
): Parcelable