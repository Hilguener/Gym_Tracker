package com.hilguener.gymtracker.ui.activity.login

data class SignInState(
    val isLoading: Boolean = false,
    val isSuccess: String? = "",
    val isError: String? = "",
    val isSignInSuccessful: Boolean = false,
)
