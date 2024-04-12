package com.hilguener.gymtracker.ui.login

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hilguener.gymtracker.R
import com.hilguener.gymtracker.ui.PasswordVisibilityToggleIcon
import com.hilguener.gymtracker.ui.register.RegisterActivity
import com.hilguener.gymtracker.ui.theme.GymTrackerTheme

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GymTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    LoginScreen()
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val registerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { }
    Scaffold(modifier = modifier.padding(36.dp)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.titleLarge,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = stringResource(R.string.login_to_your_account))
            Spacer(modifier = Modifier.height(16.dp))
            LoginEmailField()
            Spacer(modifier = Modifier.height(16.dp))
            LoginPassword("Enter password")
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { }, shape = MaterialTheme.shapes.small, modifier = Modifier.width(280.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.login),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.forgot_password),
                modifier = Modifier.clickable { },
                textAlign = TextAlign.End
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.or_sign_in_with),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            ElevatedCard(modifier = Modifier
                .padding(8.dp)
                .clickable { }
                .width(280.dp),
                shape = MaterialTheme.shapes.small) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.size(25.dp)) {
                        Image(
                            painter = painterResource(id = R.drawable.google),
                            contentDescription = "google icon"
                        )
                    }
                    Text(
                        text = stringResource(R.string.google),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
            Spacer(modifier = modifier.height(16.dp))
            Text(text = stringResource(id = R.string.don_t_have_an_account_sign_up),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        val intent = Intent(context, RegisterActivity::class.java)
                        registerLauncher.launch(intent)
                        (context as? Activity)?.finish()
                    }
                    .align(Alignment.End),
                textAlign = TextAlign.Center)
        }
    }

}


@Composable
fun LoginEmailField() {
    var email by rememberSaveable { mutableStateOf("") }

    OutlinedTextField(value = email, onValueChange = { email = it }, label = {
        Text(text = stringResource(R.string.email_address))
    }, singleLine = true, placeholder = { Text("example@gmail.com") })
}

@Composable
fun LoginPassword(text: String) {
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    OutlinedTextField(value = password,
        onValueChange = { password = it },
        label = { Text(text) },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            PasswordVisibilityToggleIcon(showPassword = passwordVisible,
                onTogglePasswordVisibility = { passwordVisible = !passwordVisible })
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
    )
}


@Preview
@Composable
fun LoginScreenPreview(modifier: Modifier = Modifier) {
    GymTrackerTheme(darkTheme = false) {
        Surface {
            LoginScreen()
        }
    }
}

@Preview
@Composable
fun LoginScreenDarkPreview(modifier: Modifier = Modifier) {

    GymTrackerTheme(darkTheme = true) {
        Surface {
            LoginScreen()
        }
    }
}