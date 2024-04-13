package com.hilguener.gymtracker.ui.activity.previews

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hilguener.gymtracker.ui.activity.register.RegisterScreen
import com.hilguener.gymtracker.ui.theme.GymTrackerTheme

@Preview
@Composable
fun RegisterScreenPreview(modifier: Modifier = Modifier) {
    GymTrackerTheme(darkTheme = false) {
        Surface {
            RegisterScreen()
        }
    }
}

@Preview
@Composable
fun RegisterScreenDarkPreview(modifier: Modifier = Modifier) {
    GymTrackerTheme(darkTheme = true) {
        Surface {
            RegisterScreen()
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview(modifier: Modifier = Modifier) {
    GymTrackerTheme(darkTheme = false) {
        Surface {
//            LoginScreen()
        }
    }
}

@Preview
@Composable
fun LoginScreenDarkPreview(modifier: Modifier = Modifier) {
    GymTrackerTheme(darkTheme = true) {
        Surface {
//            LoginScreen()
        }
    }
}
