package com.hilguener.gymtracker.ui.login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.hilguener.gymtracker.R
import com.hilguener.gymtracker.ui.theme.GymTrackerTheme

@SuppressLint("CustomSplashScreen")
class SplashScreen() : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            GymTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    SplashScreenContent()
                }
            }
        }

        Thread {
            Thread.sleep(2000)
            startActivity(Intent(this@SplashScreen, LoginActivity::class.java))
            finish()
        }.start()
    }
}

@Composable
fun SplashScreenContent() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center, content = {
        Surface {
            Image(
                painter = painterResource(id = R.drawable.weightlifter),
                contentDescription = "Countries",
                modifier = Modifier.size(200.dp),
                contentScale = ContentScale.Fit,
            )
        }
    })
}