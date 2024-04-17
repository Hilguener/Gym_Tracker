package com.hilguener.gymtracker.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.hilguener.gymtracker.datasouce.WorkoutDataSource
import com.hilguener.gymtracker.repository.AuthRepository
import com.hilguener.gymtracker.repository.AuthRepositoryImpl
import com.hilguener.gymtracker.repository.ConnectivityRepository
import com.hilguener.gymtracker.repository.Repository
import com.hilguener.gymtracker.service.UserService
import com.hilguener.gymtracker.viewmodel.ExercisesViewModel
import com.hilguener.gymtracker.viewmodel.HomeScreenViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providesFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun providesRepositoryImpl(firebaseAuth: FirebaseAuth): AuthRepository {
        return AuthRepositoryImpl(firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideWorkoutDataSource(userService: UserService): WorkoutDataSource {
        val userId = userService.getUserId()
        return WorkoutDataSource(userId)
    }

    @Provides
    @Singleton
    fun provideConnectivityRepository(
        @ApplicationContext context: Context,
    ): ConnectivityRepository {
        return ConnectivityRepository(context)
    }

    @Provides
    @Singleton
    fun provideWorkoutsRepository(dataSource: WorkoutDataSource): Repository {
        return Repository(dataSource)
    }

    @Provides
    @Singleton
    fun provideHomeScreenViewModel(
        workoutsRepository: Repository,
        connectivityRepository: ConnectivityRepository,
    ): HomeScreenViewModel {
        return HomeScreenViewModel(workoutsRepository, connectivityRepository)
    }

    @Provides
    @Singleton
    fun provideExerciseScreenViewModel(
        workoutsRepository: Repository,
    ): ExercisesViewModel {
        return ExercisesViewModel(workoutsRepository)
    }
}
