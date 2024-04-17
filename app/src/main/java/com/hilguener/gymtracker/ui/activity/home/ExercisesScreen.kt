package com.hilguener.gymtracker.ui.activity.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hilguener.gymtracker.R
import com.hilguener.gymtracker.model.Exercise
import com.hilguener.gymtracker.ui.theme.GymTrackerTheme
import com.hilguener.gymtracker.viewmodel.ExercisesViewModel

@Composable
fun ExercisesScreen(
    viewModel: ExercisesViewModel,
    workoutId: String,
    workoutName: String,
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    var dialogVisible by remember { mutableStateOf(false) }
    var updateWorkoutNameDialog by remember { mutableStateOf(false) }
    var updateExerciseNameDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val exercises by viewModel.fetchExercises(workoutId).collectAsState(initial = emptyList())
    var workoutNameState by remember { mutableStateOf(workoutName) }
    var selectedExerciseId by remember { mutableStateOf("") }

    Scaffold(
        floatingActionButton = { FabSaveExercises(onClick = { dialogVisible = true }) },
        topBar = {
            ExerciseTopBar(title = workoutNameState, onBackPressed = {
                navController.popBackStack(
                    route = context.getString(R.string.home_screen),
                    inclusive = false,
                )
            }, onMenuClicked = { updateWorkoutNameDialog = true })
        },
    ) { padding ->
        Column(modifier = modifier.padding(padding)) {
            Text(
                text = stringResource(R.string.list_of_exercises),
                modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
            )

            Box(modifier = modifier.fillMaxSize()) {
                if (exercises.isEmpty()) {
                    Text(
                        text = stringResource(R.string.no_exercises_available),
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                                .wrapContentSize(Alignment.Center),
                    )
                } else {
                    LazyColumn(modifier, content = {
                        items(exercises) { exercise ->
                            ExerciseItem(
                                exercise = exercise,
                                navController = navController,
                                workoutId = workoutId,
                                viewModel = viewModel,
                                onEditClick = { selectedExercise ->
                                    selectedExerciseId = selectedExercise.exerciseId
                                    updateExerciseNameDialog = true
                                },
                            )
                        }
                    })
                }
            }
        }
    }

    if (updateWorkoutNameDialog) {
        EditWorkoutDialog(
            onDismissRequest = { dialogVisible = false },
            onEdit = { newName ->
                viewModel.updateWorkout(workoutId, newName)
                updateWorkoutNameDialog = false
                workoutNameState = newName
            },
            initialName = workoutName,
        )
    }

    if (updateExerciseNameDialog) {
        EditExerciseDialog(
            exerciseId = selectedExerciseId,
            initialExerciseName = exercises.find { it.exerciseId == selectedExerciseId }?.exerciseName.orEmpty(),
            onSave = { newName, newObservations ->
                viewModel.updateExercise(workoutId, selectedExerciseId, newName, newObservations)
                updateExerciseNameDialog = false
            },
            onCloseRequest = { updateExerciseNameDialog = false },
            initialObservations = exercises.find { it.exerciseId == selectedExerciseId }?.observations.orEmpty(),
        )
    }

    NewExerciseDialog(
        isVisible = dialogVisible,
        onConfirm = { name, observations ->
            dialogVisible = false
            viewModel.saveExercise(workoutId, name, observations)
        },
        onDismiss = { dialogVisible = false },
        workoutId = workoutId,
    )
}

@Composable
fun FabSaveExercises(onClick: () -> Unit) {
    FloatingActionButton(onClick = onClick) {
        Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.save_new_exercises))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseTopBar(
    title: String,
    onBackPressed: () -> Unit,
    onMenuClicked: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    TopAppBar(title = {
        Row {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = stringResource(R.string.back),
                modifier =
                    Modifier
                        .clickable(onClick = onBackPressed)
                        .padding(end = 8.dp),
            )
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )
        }
    }, actions = {
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "",
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            DropdownMenuItem(onClick = {
                expanded = false
                onMenuClicked()
            }, text = { Text(stringResource(R.string.edit_workout_routine)) })
        }
    })
}

@Composable
fun EditWorkoutDialog(
    onDismissRequest: () -> Unit,
    onEdit: (String) -> Unit,
    initialName: String,
) {
    var name by remember { mutableStateOf(initialName) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(stringResource(R.string.edit_workout_routine)) },
        text = {
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nome") },
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        },
        confirmButton = {
            Button(onClick = { onEdit(name) }, shape = MaterialTheme.shapes.small) {
                Text(stringResource(R.string.save))
            }
        },
        dismissButton = {
            Button(
                onClick = onDismissRequest,
                shape = MaterialTheme.shapes.small,
            ) {
                Text(stringResource(R.string.cancel))
            }
        },
    )
}

@Composable
fun NewExerciseDialog(
    onConfirm: (name: String, observations: String) -> Unit,
    onDismiss: () -> Unit,
    isVisible: Boolean,
    workoutId: String,
) {
    if (isVisible) {
        var name by remember { mutableStateOf("") }
        var observations by remember { mutableStateOf("") }
        var isFormValid by remember { mutableStateOf(false) }
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(stringResource(R.string.new_exercise)) },
            text = {
                Column {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        isError = name.isEmpty(),
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(text = stringResource(R.string.exercise_name)) },
                    )
                    OutlinedTextField(
                        value = observations,
                        onValueChange = { observations = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(text = stringResource(R.string.observations)) },
                        maxLines = 3,
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (name.isNotEmpty() && observations.isNotEmpty()) {
                            isFormValid = true
                            onConfirm(
                                name,
                                observations,
                            )
                        } else {
                            isFormValid = false
                        }
                    },
                    enabled = name.isNotEmpty() && observations.isNotEmpty(),
                    shape = MaterialTheme.shapes.small,
                ) {
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

@Composable
fun EditExerciseDialog(
    exerciseId: String,
    initialExerciseName: String,
    initialObservations: String,
    onSave: (String, String) -> Unit,
    onCloseRequest: () -> Unit,
) {
    var exerciseName by remember { mutableStateOf(initialExerciseName) }
    var observations by remember { mutableStateOf(initialObservations) }

    AlertDialog(
        onDismissRequest = { onCloseRequest() },
        title = { Text(text = stringResource(R.string.edit_exercise)) },
        text = {
            Column {
                OutlinedTextField(
                    value = exerciseName,
                    onValueChange = { exerciseName = it },
                    label = { Text(text = stringResource(R.string.exercise_name)) },
                )
                OutlinedTextField(
                    value = observations,
                    onValueChange = { observations = it },
                    label = { Text(text = stringResource(R.string.observations)) },
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                onSave(exerciseName, observations)
                onCloseRequest()
            }, shape = MaterialTheme.shapes.small) {
                Text(text = stringResource(R.string.save))
            }
        },
        dismissButton = {
            Button(onClick = onCloseRequest, shape = MaterialTheme.shapes.small) {
                Text(text = stringResource(R.string.cancel))
            }
        },
    )
}

@Composable
fun ExerciseItem(
    exercise: Exercise,
    navController: NavController,
    workoutId: String,
    viewModel: ExercisesViewModel,
    onEditClick: (Exercise) -> Unit,
) {
    ElevatedCard(
        modifier =
            Modifier
                .padding(8.dp)
                .clickable {
                    onEditClick(exercise)
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
                Text(text = exercise.exerciseName, style = MaterialTheme.typography.displaySmall)
                Text(
                    text = exercise.observations,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
            Box(modifier = Modifier.padding(top = 16.dp)) {
                IconButton(
                    onClick = {
                        viewModel.deleteExercise(workoutId, exercise.exerciseId)
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
fun ExercisesScreenPreview(modifier: Modifier = Modifier) {
    GymTrackerTheme(darkTheme = false) {
        Surface {
        }
    }
}
