package com.hilguener.gymtracker.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.hilguener.gymtracker.model.Exercise
import com.hilguener.gymtracker.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ExercisesViewModel
    @Inject
    constructor(
        private val repository: Repository,
    ) : ViewModel() {
        private val _selectedExerciseId = mutableStateOf("")
        val selectedExerciseId: State<String> = _selectedExerciseId

        private val _selectedExerciseName = mutableStateOf("")
        val selectedExerciseName: State<String> = _selectedExerciseName

        fun fetchExercises(workoutId: String): Flow<MutableList<Exercise>> {
            return repository.fetchExercises(workoutId).onEach {}
        }

        fun saveExercise(
            workoutId: String,
            exerciseName: String,
            observations: String,
        ) {
            repository.saveExercise(workoutId, exerciseName, observations)
        }

        fun deleteExercise(
            workoutId: String,
            exerciseId: String,
        ) {
            repository.deleteExercise(workoutId, exerciseId)
        }

        fun updateExercise(
            workoutId: String,
            exerciseId: String,
            exerciseName: String,
            observations: String,
        ) {
            repository.updateExercise(workoutId, exerciseId, exerciseName, observations)
        }

        fun updateWorkout(
            workoutId: String,
            workoutName: String,
        ) {
            repository.updateWorkout(workoutId, workoutName)
        }
    }
