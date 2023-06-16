package com.efrivahmi.elaborate.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Diagnose(
    val age: Int,
    val sex: Int,
    val rbc: Double,
    val hgb: Double,
    val hct: Double,
    val mcv: Double,
    val mch: Double,
    val mchc: Double,
    val rdw_cv: Double,
    val wbc: Double,
    val neu: Double,
    val lym: Double,
    val mo: Double,
    val eos: Double,
    val ba: Double
): Parcelable
