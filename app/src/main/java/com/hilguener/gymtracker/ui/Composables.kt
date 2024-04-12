package com.hilguener.gymtracker.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable

@Composable
fun PasswordVisibilityToggleIcon(
    showPassword: Boolean, onTogglePasswordVisibility: () -> Unit
) {
    val image = if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
    val contentDescription = if (showPassword) "Hide password icon" else "Show password icon"
    IconButton(onClick = onTogglePasswordVisibility) {
        Icon(imageVector = image, contentDescription = contentDescription)
    }
}

