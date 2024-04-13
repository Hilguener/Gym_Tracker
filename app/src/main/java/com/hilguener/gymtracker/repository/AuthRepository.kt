package com.hilguener.gymtracker.repository

import com.google.firebase.auth.AuthResult
import com.hilguener.gymtracker.util.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun loginUser(
        email: String,
        password: String,
    ): Flow<Resource<AuthResult>>

    fun registerUser(
        email: String,
        password: String,
    ): Flow<Resource<AuthResult>>
}
