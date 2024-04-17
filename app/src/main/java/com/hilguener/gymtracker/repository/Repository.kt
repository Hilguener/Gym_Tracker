package com.hilguener.gymtracker.repository

import com.hilguener.gymtracker.datasouce.WorkoutDataSource
import com.hilguener.gymtracker.model.Exercise
import com.hilguener.gymtracker.model.Workout
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Repository
    @Inject
    constructor(private val dataSource: WorkoutDataSource) {
        fun saveWorkout(
            name: String,
            description: String,
            date: String,
            time: String,
        ) {
            dataSource.saveWorkout(name, description, date, time)
        }

        fun fetchWorkouts(): Flow<MutableList<Workout>> {
            return dataSource.fetchWorkouts()
        }

        fun fetchExercises(workoutId: String): Flow<MutableList<Exercise>> {
            return dataSource.fetchExercises(workoutId)
        }

        fun deleteWorkout(workoutId: String) {
            dataSource.deleteWorkout(workoutId)
        }

        fun updateWorkout(
            workoutId: String,
            workoutName: String,
        ) {
            dataSource.updateWorkout(workoutId, workoutName)
        }

        fun updateExercise(
            workoutId: String,
            exerciseId: String,
            exerciseName: String,
            observations: String,
        ) {
            dataSource.updateExercise(workoutId, exerciseId, exerciseName, observations)
        }

        fun saveExercise(
            workoutId: String,
            exerciseName: String,
            observations: String,
        ) {
            dataSource.saveExercise(workoutId, exerciseName, observations)
        }

        fun deleteExercise(
            workoutId: String,
            exerciseId: String,
        ) {
            dataSource.deleteExerciseInWorkout(workoutId, exerciseId)
        }
    }
