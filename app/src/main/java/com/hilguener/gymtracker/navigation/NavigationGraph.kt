package com.hilguener.gymtracker.navigation

import android.app.Activity.RESULT_OK
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.identity.Identity
import com.hilguener.gymtracker.ui.activity.GoogleAuthUIClient
import com.hilguener.gymtracker.ui.activity.home.HomeScreen
import com.hilguener.gymtracker.ui.activity.login.LoginScreen
import com.hilguener.gymtracker.ui.activity.register.RegisterScreen
import com.hilguener.gymtracker.viewmodel.SignInViewModel
import kotlinx.coroutines.launch

@Composable
fun NavigationGraph(
    navController: NavHostController = rememberNavController(),
) {
    val context = LocalContext.current
    val googleAuthUIClient by lazy {
        GoogleAuthUIClient(
            context = context,
            oneTapClient = Identity.getSignInClient(context),
        )
    }
    NavHost(navController = navController, startDestination = Screens.LoginScreen.route) {
        composable(route = Screens.LoginScreen.route) {
            val viewModel = hiltViewModel<SignInViewModel>()
            val state by viewModel.signInState.collectAsStateWithLifecycle()
            val scope = rememberCoroutineScope()
            val launcher =
                rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartIntentSenderForResult(),
                    onResult = { result ->
                        if (result.resultCode == RESULT_OK) {
                            scope.launch {
                                val signInResult =
                                    googleAuthUIClient.signInWithIntent(
                                        intent = result.data ?: return@launch,
                                    )
                                viewModel.onSignInResult(signInResult)
                            }
                        }
                    },
                )

            LaunchedEffect(key1 = state.isSignInSuccessful) {
                if (state.isSignInSuccessful) {
                    Toast.makeText(context, "Sign in successful", Toast.LENGTH_SHORT).show()
                    navController.navigate(Screens.HomeScreen.route) {
                        popUpTo(Screens.LoginScreen.route) { inclusive = true }
                    }
                }
            }
            LoginScreen(state, onSignInClick = {
                scope.launch {
                    val signInIntent = googleAuthUIClient.signIn()
                    launcher.launch(
                        IntentSenderRequest.Builder(
                            signInIntent ?: return@launch,
                        ).build(),
                    )
                }
            })
        }
        composable(route = Screens.RegisterScreen.route) {
            RegisterScreen()
        }
        composable(route = Screens.HomeScreen.route) {
            HomeScreen()
        }
    }
}
