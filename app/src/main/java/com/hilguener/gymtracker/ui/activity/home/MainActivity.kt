package com.hilguener.gymtracker.ui.activity.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.hilguener.gymtracker.navigation.NavigationGraph
import com.hilguener.gymtracker.ui.theme.GymTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GymTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    NavigationGraph()
                }
            }
        }
    }
}
