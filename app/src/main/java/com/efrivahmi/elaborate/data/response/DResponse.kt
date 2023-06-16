package com.efrivahmi.elaborate.data.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DResponse (
    val diagnosisId: String,
    val prediction: Int
): Parcelable