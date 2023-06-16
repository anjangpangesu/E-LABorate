package com.efrivahmi.elaborate.data.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Workout(
    val error: Boolean,
    val message: String,
    val token: String,
    val userId: String,
    val username: String,
    val workouts: List<Workout>
): Parcelable {
    @Parcelize
    data class Workout(
        val title: String,
        val video_link: String,
        val workoutId: String
    ): Parcelable
}