package com.hilguener.gymtracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilguener.gymtracker.repository.AuthRepository
import com.hilguener.gymtracker.ui.activity.login.SignInState
import com.hilguener.gymtracker.util.Resource
import com.hilguener.gymtracker.util.SignInResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel
    @Inject
    constructor(
        private val repository: AuthRepository,
    ) : ViewModel() {
        private val _signInState = MutableStateFlow(SignInState())
        val signInState = _signInState.asStateFlow()

        fun onSignInResult(result: SignInResult) {
            _signInState.update {
                it.copy(
                    isSignInSuccessful = result.data != null,
                    isError = result.errorMessage,
                )
            }
        }

        fun resetState() {
            _signInState.update { SignInState() }
        }

        fun loginUser(
            email: String,
            password: String,
        ) =
            viewModelScope.launch {
                repository.loginUser(email, password).collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            _signInState.value = SignInState(isSignInSuccessful = true, isSuccess = "Sign in Success")
                        }

                        is Resource.Loading -> {
                            _signInState.value = SignInState(isLoading = true)
                        }

                        is Resource.Error -> {
                            _signInState.value = SignInState(isError = result.message.toString())
                        }
                    }
                }
            }
    }
