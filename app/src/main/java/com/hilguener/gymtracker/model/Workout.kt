package com.hilguener.gymtracker.model

data class Workout(
    val workoutId: String,
    val workoutName: String,
    val workoutDescription: String,
    val date: String,
    val time: String,
    val exercises: List<Exercise> = emptyList(),
)
