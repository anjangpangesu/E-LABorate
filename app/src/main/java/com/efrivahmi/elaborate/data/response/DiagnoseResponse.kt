package com.efrivahmi.elaborate.data.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class DiagnoseResponse(
    val diagnosisId: String,
    val input_data: InputData,
    val prediction: Int
)
@Parcelize
data class InputData(
    val age: Int,
    val rdw_cv: Double,
    val neu: Double,
    val wbc: Double,
    val sex: Int,
    val ba: Double,
    val hct: Double,
    val mchc: Double,
    val mo: Double,
    val lym: Double,
    val hgb: Double,
    val rbc: Double,
    val mch: Double,
    val eos: Double,
    val mcv: Double
): Parcelable