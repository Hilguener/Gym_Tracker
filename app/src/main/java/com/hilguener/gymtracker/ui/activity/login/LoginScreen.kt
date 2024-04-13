package com.hilguener.gymtracker.ui.activity.login

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hilguener.gymtracker.R
import com.hilguener.gymtracker.ui.PasswordVisibilityToggleIcon
import com.hilguener.gymtracker.viewmodel.SignInViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginScreen(
    state: SignInState,
    modifier: Modifier = Modifier,
    viewModel: SignInViewModel = hiltViewModel(),
    onSignInClick: () -> Unit,
) {
    val (email, setEmail) = remember { mutableStateOf("") }
    val (password, setPassword) = remember { mutableStateOf("") }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
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
            Text(text = stringResource(R.string.login_to_your_account))
            Spacer(modifier = Modifier.height(16.dp))
            LoginEmailField(email, setEmail)
            Spacer(modifier = Modifier.height(16.dp))
            LoginPassword(password, setPassword)
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { viewModel.loginUser(email, password) },
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.width(280.dp),
            ) {
                Text(
                    text = stringResource(id = R.string.login),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.forgot_password),
                modifier = Modifier.clickable { },
                textAlign = TextAlign.End,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.or_sign_in_with),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(12.dp))
            ElevatedCard(
                modifier =
                    Modifier
                        .padding(8.dp)
                        .width(280.dp),
                onClick = onSignInClick,
                shape = MaterialTheme.shapes.small,
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(modifier = Modifier.size(25.dp)) {
                        Image(
                            painter = painterResource(id = R.drawable.google),
                            contentDescription = "google icon",
                        )
                    }
                    Text(
                        text = stringResource(R.string.google),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                    )
                }
            }
            Spacer(modifier = modifier.height(16.dp))
            Text(
                text = stringResource(id = R.string.don_t_have_an_account_sign_up),
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .align(Alignment.End),
                textAlign = TextAlign.Center,
            )

            LaunchedEffect(key1 = state.isSuccess) {
                scope.launch {
                    if (state.isSuccess?.isNotEmpty() == true) {
                        val success = state.isSuccess
                        Toast.makeText(context, "$success", Toast.LENGTH_LONG).show()
                    }
                }
            }
            LaunchedEffect(key1 = state.isError) {
                scope.launch {
                    if (state.isError?.isNotBlank() == true) {
                        val error = state.isError
                        Toast.makeText(context, "$error", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}

@Composable
fun LoginEmailField(
    email: String,
    onEmailChange: (String) -> Unit,
) {
    var emailState by remember { mutableStateOf(TextFieldValue(email)) }
    OutlinedTextField(
        value = emailState,
        onValueChange = {
            emailState = it
            onEmailChange(it.text)
        },
        label = { Text(text = stringResource(R.string.email_address)) },
        singleLine = true,
        placeholder = { Text("example@gmail.com") },
    )
}

@Composable
fun LoginPassword(
    password: String,
    onPasswordChange: (String) -> Unit,
) {
    var passwordState by remember { mutableStateOf(TextFieldValue(password)) }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    OutlinedTextField(
        value = passwordState,
        onValueChange = {
            passwordState = it
            onPasswordChange(it.text)
        },
        singleLine = true,
        label = { Text("Enter your password") },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            PasswordVisibilityToggleIcon(
                showPassword = passwordVisible,
                onTogglePasswordVisibility = { passwordVisible = !passwordVisible },
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
    )
}
