package com.hilguener.gymtracker.model

data class Exercise(
    val exerciseId: String = "",
    val exerciseName: String,
    val observations: String,
    val sets: Int,
    val rests: Int,
)
