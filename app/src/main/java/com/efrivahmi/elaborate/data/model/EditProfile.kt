package com.efrivahmi.elaborate.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EditProfile (
    val username: String,
    val email: String,
    val phone: String,
    val address: String
): Parcelable