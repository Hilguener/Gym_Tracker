package com.hilguener.gymtracker.ui.activity.home

import android.widget.Toast
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.hilguener.gymtracker.R
import com.hilguener.gymtracker.model.Workout
import com.hilguener.gymtracker.navigation.Screens
import com.hilguener.gymtracker.ui.WorkoutDate
import com.hilguener.gymtracker.ui.WorkoutDescTextField
import com.hilguener.gymtracker.ui.WorkoutNameTextField
import com.hilguener.gymtracker.ui.WorkoutTime
import com.hilguener.gymtracker.ui.theme.GymTrackerTheme
import com.hilguener.gymtracker.viewmodel.HomeScreenViewModel
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeScreenViewModel,
    modifier: Modifier = Modifier,
) {
    var dialogVisible by remember { mutableStateOf(false) }
    val isLoading = viewModel.isLoading.value
    val isOnline = viewModel.isOnline.observeAsState()
    val workouts by viewModel.fetchWorkouts().collectAsState(emptyList())

    Scaffold(
        floatingActionButton = { FabNewWorkout(onClick = { dialogVisible = true }) },
        topBar = {
            AppBar(stringResource(id = R.string.workout))
        },
    ) { padding ->

        Box(modifier = modifier.fillMaxSize()) {
            if (workouts.isEmpty() && isOnline.value == true) {
                Text(
                    text = stringResource(R.string.no_workouts_available),
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .wrapContentSize(Alignment.Center),
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = padding, content = {
                    items(workouts) { workout ->
                        WorkoutItem(workout = workout, navController)
                    }
                })
            }

            if (isLoading == true) {
                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    LinearProgressIndicator(modifier = Modifier.width(50.dp))
                }
            }

            if (isOnline.value == false) {
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    NoInternetConnectionMessage(onRefreshClick = {
                        viewModel.fetchWorkouts()
                    })
                }
            }
        }
    }

    NewWorkoutDialog(
        isVisible = dialogVisible,
        onConfirm = { _, _, _, _ -> dialogVisible = false },
        onDismiss = { dialogVisible = false },
    )
}

@Composable
fun FabNewWorkout(onClick: () -> Unit) {
    FloatingActionButton(onClick = onClick) {
        Icon(Icons.Filled.Add, contentDescription = "")
    }
}

@Composable
fun NoInternetConnectionMessage(onRefreshClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp),
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(R.string.close),
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(24.dp),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.no_internet_connection),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onRefreshClick,
                shape = MaterialTheme.shapes.small,
            ) {
                Text(
                    text = stringResource(R.string.refresh),
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}

@Composable
fun NewWorkoutDialog(
    onConfirm: (name: String, description: String, date: LocalDate, time: LocalTime) -> Unit,
    onDismiss: () -> Unit,
    isVisible: Boolean,
) {
    val viewModel = hiltViewModel<HomeScreenViewModel>()
    val (name, setName) = remember { mutableStateOf("") }
    val (description, setDescription) = remember { mutableStateOf("") }
    var pickedDate by remember { mutableStateOf(LocalDate.now()) }
    var pickedTime by remember { mutableStateOf(LocalTime.now()) }
    val dateDialogState = rememberMaterialDialogState()
    val dateTimeState = rememberMaterialDialogState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    if (isVisible) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(stringResource(id = R.string.new_workout)) },
            text = {
                Column {
                    WorkoutNameTextField(name = name, setName)
                    WorkoutDescTextField(description = description, setDescription)
                    WorkoutDate(
                        pickedDate = pickedDate,
                        onDateSelected = { pickedDate = it },
                        dateDialogState = dateDialogState,
                    )
                    WorkoutTime(
                        pickedTime = pickedTime,
                        onTimeSelected = { pickedTime = it },
                        timeDialogState = dateTimeState,
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    scope.launch {
                        val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
                        val formattedDate = pickedDate?.format(dateFormatter) ?: ""
                        val formattedTime = pickedTime?.format(timeFormatter) ?: ""
                        if (name.isNotEmpty() && description.isNotEmpty()) {
                            onConfirm(
                                name,
                                description,
                                pickedDate,
                                pickedTime,
                            )
                            viewModel.saveWorkout(
                                name,
                                description,
                                formattedDate,
                                formattedTime,
                            )
                            Toast.makeText(
                                context,
                                context.getString(R.string.success),
                                Toast.LENGTH_LONG,
                            ).show()
                        } else {
                            Toast.makeText(
                                context,
                                context.getString(R.string.training_name_must_not_be_blank),
                                Toast.LENGTH_LONG,
                            ).show()
                        }
                    }
                }, shape = MaterialTheme.shapes.small) {
                    Text(stringResource(R.string.confirm))
                }
            },
            dismissButton = {
                Button(
                    onClick = onDismiss,
                    shape = MaterialTheme.shapes.small,
                ) {
                    Text(stringResource(R.string.cancel))
                }
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(text: String) {
    TopAppBar(title = {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
        )
    })
}

@Composable
fun DeleteDialog(
    showDialog: MutableState<Boolean>,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    if (showDialog.value) {
        AlertDialog(onDismissRequest = onDismiss, title = {
            Text(text = stringResource(id = R.string.delete_workout))
        }, text = {
            Text(text = stringResource(R.string.are_you_sure_you_want_to_delete_this_workout))
        }, confirmButton = {
            Button(onClick = {
                onConfirm()
                showDialog.value = false
            }, shape = MaterialTheme.shapes.small) {
                Text(stringResource(id = R.string.confirm))
            }
        }, dismissButton = {
            Button(onClick = {
                showDialog.value = false
                onDismiss()
            }, shape = MaterialTheme.shapes.small) {
                Text(stringResource(id = R.string.cancel))
            }
        })
    }
}

@Composable
fun WorkoutItem(
    workout: Workout,
    navController: NavController,
) {
    val viewModel = hiltViewModel<HomeScreenViewModel>()
    val showDialog = remember { mutableStateOf(false) }
    DeleteDialog(showDialog = showDialog, onConfirm = {
        viewModel.deleteWorkout(workout.workoutId)
    }, onDismiss = {})

    ElevatedCard(
        modifier =
            Modifier
                .padding(8.dp)
                .clickable {
                    val route =
                        "${Screens.ExercisesScreen.route}/${workout.workoutId}/${workout.workoutName}"
                    navController.navigate(route)
                },
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
        ) {
            Column(
                modifier =
                    Modifier
                        .padding(16.dp)
                        .weight(1f),
            ) {
                Text(text = workout.workoutName, style = MaterialTheme.typography.displaySmall)
                Text(
                    text = workout.workoutDescription,
                    style = MaterialTheme.typography.bodyLarge,
                )
                Row {
                    Text(
                        text = workout.date,
                        style = MaterialTheme.typography.bodySmall,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = workout.time,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
            Box(modifier = Modifier.padding(top = 16.dp)) {
                IconButton(
                    onClick = {
                        showDialog.value = true
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.delete_workout),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview(modifier: Modifier = Modifier) {
    GymTrackerTheme(darkTheme = false) {
        Surface {
        }
    }
}
