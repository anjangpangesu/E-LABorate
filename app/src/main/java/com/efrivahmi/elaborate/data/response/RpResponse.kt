package com.efrivahmi.elaborate.data.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RpResponse(
    val error: Boolean,
    val message: String,
    val userId: String,
    val username: String,
    val email: String,
    val tokenRevoked: Boolean
): Parcelable
