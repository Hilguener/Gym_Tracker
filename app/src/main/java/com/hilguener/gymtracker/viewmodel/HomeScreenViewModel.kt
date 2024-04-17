package com.hilguener.gymtracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.hilguener.gymtracker.model.Workout
import com.hilguener.gymtracker.repository.ConnectivityRepository
import com.hilguener.gymtracker.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel
    @Inject
    constructor(
        private val workoutsRepository: Repository,
        connectivityRepository: ConnectivityRepository,
    ) : ViewModel() {
        private val _isLoading = MutableLiveData<Boolean>()
        val isLoading: LiveData<Boolean> get() = _isLoading
        val isOnline = connectivityRepository.isConnected.asLiveData()

        init {
            fetchWorkouts()
        }

        fun saveWorkout(
            name: String,
            description: String,
            date: String,
            time: String,
        ) {
            _isLoading.value = true
            workoutsRepository.saveWorkout(name, description, date, time)

            _isLoading.value = false
        }

        fun fetchWorkouts(): Flow<MutableList<Workout>> {
            _isLoading.value = true
            return workoutsRepository.fetchWorkouts().onEach {
                _isLoading.value = false
            }
        }

        fun deleteWorkout(workoutId: String) {
            _isLoading.value = true
            workoutsRepository.deleteWorkout(workoutId)

            _isLoading.value = false
        }
    }
