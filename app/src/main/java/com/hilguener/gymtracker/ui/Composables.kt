package com.hilguener.gymtracker.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.hilguener.gymtracker.R
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun PasswordVisibilityToggleIcon(
    showPassword: Boolean,
    onTogglePasswordVisibility: () -> Unit,
) {
    val image = if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
    val contentDescription = if (showPassword) "Hide password icon" else "Show password icon"
    IconButton(onClick = onTogglePasswordVisibility) {
        Icon(imageVector = image, contentDescription = contentDescription)
    }
}

@Composable
fun WorkoutNameTextField(
    name: String,
    onNameChange: (String) -> Unit,
) {
    var nameState by remember { mutableStateOf(TextFieldValue(name)) }
    OutlinedTextField(
        value = nameState,
        onValueChange = {
            nameState = it
            onNameChange(it.text)
        },
        label = { Text(text = stringResource(R.string.workout_name)) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
    )
}

@Composable
fun WorkoutDescTextField(
    description: String,
    onDescriptionChange: (String) -> Unit,
) {
    var descriptionState by remember { mutableStateOf(TextFieldValue(description)) }
    OutlinedTextField(
        value = descriptionState,
        onValueChange = {
            descriptionState = it
            onDescriptionChange(it.text)
        },
        label = {
            Text(
                text = stringResource(R.string.workout_description),
            )
        },
        maxLines = 2,
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
    )
}

@Composable
fun WorkoutDate(
    pickedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    dateDialogState: MaterialDialogState,
) {
    val dateFormatter = remember { DateTimeFormatter.ofPattern("dd-MM-yyyy") }

    OutlinedTextField(
        value = pickedDate.format(dateFormatter),
        onValueChange = {},
        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
        readOnly = true,
        trailingIcon = {
            IconButton(onClick = { dateDialogState.show() }) {
                Icon(Icons.Filled.DateRange, contentDescription = "Select date")
            }
        },
    )
    MaterialDialog(dialogState = dateDialogState, buttons = {
        positiveButton(text = "Ok")
        negativeButton(text = "Cancel")
    }) {
        this.datepicker(initialDate = pickedDate) {
            onDateSelected(it)
            dialogState.hide()
        }
    }
}

@Composable
fun WorkoutTime(
    pickedTime: LocalTime,
    onTimeSelected: (LocalTime) -> Unit,
    timeDialogState: MaterialDialogState,
) {
    val dateFormatter = remember { DateTimeFormatter.ofPattern("HH:mm") }

    OutlinedTextField(
        value = pickedTime.format(dateFormatter),
        onValueChange = {},
        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
        readOnly = true,
        trailingIcon = {
            IconButton(onClick = { timeDialogState.show() }) {
                Icon(Icons.Filled.AccessTime, contentDescription = "Select time")
            }
        },
    )
    MaterialDialog(dialogState = timeDialogState, buttons = {
        positiveButton(text = "Ok")
        negativeButton(text = "Cancel")
    }) {
        this.timepicker(initialTime = pickedTime) {
            onTimeSelected(it)
            dialogState.hide()
        }
    }
}
