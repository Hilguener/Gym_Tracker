package com.hilguener.gymtracker.ui.activity.register

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.hilguener.gymtracker.R
import com.hilguener.gymtracker.ui.PasswordVisibilityToggleIcon
import com.hilguener.gymtracker.ui.theme.GymTrackerTheme
import com.hilguener.gymtracker.viewmodel.SignUpViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RegisterScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val state = viewModel.signUpState.collectAsState(initial = null)
    Scaffold(modifier = modifier.padding(36.dp)) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.titleLarge,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(4.dp))

            Text(text = "Create an account")

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(value = email, onValueChange = { email = it }, label = {
                Text(text = stringResource(R.string.email_address))
            }, singleLine = true, placeholder = { Text("example@gmail.com") })

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                singleLine = true,
                value = password,
                onValueChange = { password = it },
                label = { Text(text = stringResource(R.string.enter_password)) },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    PasswordVisibilityToggleIcon(
                        showPassword = passwordVisible,
                        onTogglePasswordVisibility = { passwordVisible = !passwordVisible },
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    scope.launch {
                        viewModel.registerUser(email, password)
                    }
                },
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.width(280.dp),
            ) {
                Text(
                    text = "Create",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                )
            }
            Spacer(modifier = modifier.height(16.dp))

            Text(
                text = stringResource(R.string.already_have_an_account_login),
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.popBackStack(
                                route = context.getString(R.string.login_screen),
                                inclusive = false,
                            )
                        }
                        .align(Alignment.End),
                textAlign = TextAlign.Center,
            )

            LaunchedEffect(key1 = state.value?.isSuccess) {
                scope.launch {
                    if (state.value?.isSuccess?.isNotEmpty() == true) {
                        val success = state.value?.isSuccess
                        Toast.makeText(context, "$success", Toast.LENGTH_LONG).show()
                        navController.navigate(context.getString(R.string.home_screen))
                    }
                }
            }
            LaunchedEffect(key1 = state.value?.isError) {
                scope.launch {
                    if (state.value?.isError?.isNotBlank() == true) {
                        val error = state.value?.isError
                        Toast.makeText(context, "$error", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun RegisterScreenPreview(modifier: Modifier = Modifier) {
    GymTrackerTheme(darkTheme = false) {
        Surface {
//            RegisterScreen()
        }
    }
}

@Preview
@Composable
fun RegisterScreenDarkPreview(modifier: Modifier = Modifier) {
    GymTrackerTheme(darkTheme = true) {
        Surface {
//            RegisterScreen()
        }
    }
}
