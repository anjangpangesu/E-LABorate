package com.efrivahmi.elaborate.data.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EditProfileResponse (
    val code: Int,
    val error: Boolean,
    val message: String,
    val userData: UserProfileData
): Parcelable
@Parcelize
data class UserProfileData (
    val userId: String,
    val username: String,
    val email: String,
    val phone: String,
    val address: String
): Parcelable