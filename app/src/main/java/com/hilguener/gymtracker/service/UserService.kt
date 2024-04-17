package com.hilguener.gymtracker.service

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class UserService
    @Inject
    constructor(private val firebaseAuth: FirebaseAuth) {
        fun getUserId(): String {
            val currentUser = firebaseAuth.currentUser
            return currentUser?.uid ?: "defaultUserId"
        }
    }
