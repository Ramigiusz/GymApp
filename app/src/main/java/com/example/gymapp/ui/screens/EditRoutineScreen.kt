package com.example.gymapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gymapp.viewmodel.ExerciseViewModel
import com.example.gymapp.viewmodel.RoutineViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymapp.data.draft.RoutineExerciseDraft
import com.example.gymapp.data.draft.ExerciseSetDraft
import com.example.gymapp.ui.components.RestTimePickerDialog


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditRoutineScreen(
    navController: NavController,
    routineId: Int?,
    viewModel: RoutineViewModel = viewModel(),
    viewModelExercise: ExerciseViewModel = viewModel()
) {
    var routineName by remember { mutableStateOf("") }
    val allExercises = viewModelExercise.exercises
    var draftExercises = remember { mutableStateListOf<RoutineExerciseDraft>() }
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(routineId) {
        if (routineId != null && routineId > 0) {
            val existingRoutine = viewModel.routines.find { it.id == routineId }
            routineName = existingRoutine?.name ?: ""

            viewModel.loadExercisesForRoutine(routineId) { loaded ->
                draftExercises.clear()
                draftExercises.addAll(loaded)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(if (routineId == 0) "New Routine" else "Edit Routine") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = routineName,
                onValueChange = { routineName = it },
                label = { Text("Routine Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { expanded = true }) {
                Text("Dodaj ćwiczenie")
            }

            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                allExercises.forEach { exercise ->
                    DropdownMenuItem(
                        text = { Text(exercise.name) },
                        onClick = {
                            draftExercises.add(RoutineExerciseDraft(exercise))
                            expanded = false
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text("Ćwiczenia w rutynie:", style = MaterialTheme.typography.titleMedium)

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(draftExercises, key = { it.exercise.id }) { item ->
                    var showDialog by remember { mutableStateOf(false) }
                    var minutesText by remember { mutableStateOf(TextFieldValue(item.restMinutes.toString())) }
                    var secondsText by remember { mutableStateOf(TextFieldValue(item.restSeconds.toString())) }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(item.exercise.name, style = MaterialTheme.typography.titleMedium)
                                Row {
                                    IconButton(onClick = { showDialog = true }) {
                                        Icon(Icons.Outlined.Lock, contentDescription = "Ustaw przerwę")
                                    }
                                    IconButton(onClick = { draftExercises.remove(item) }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Usuń ćwiczenie")
                                    }
                                }
                            }
                            if (showDialog) {
                                    RestTimePickerDialog(
                                        initialMinutes = item.restMinutes,
                                        initialSeconds = item.restSeconds,
                                        onDismiss = { showDialog = false },
                                        onConfirm = { min, sec ->
                                            item.restMinutes = min
                                            item.restSeconds = sec
                                            showDialog = false
                                        }
                                    )
                                }


                                Spacer(modifier = Modifier.height(8.dp))
                            Text("Serie:", style = MaterialTheme.typography.labelLarge)

                            item.sets.forEachIndexed { index, set ->
                                Row(Modifier.fillMaxWidth()) {
                                    OutlinedTextField(
                                        value = set.reps.toString(),
                                        onValueChange = {
                                            set.reps = it.toIntOrNull() ?: set.reps
                                        },
                                        label = { Text("Powt.") },
                                        modifier = Modifier.weight(1f)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    OutlinedTextField(
                                        value = set.rpe.toString(),
                                        onValueChange = {
                                            set.rpe = it.toFloatOrNull() ?: set.rpe
                                        },
                                        label = { Text("RPE") },
                                        modifier = Modifier.weight(1f)
                                    )
                                    IconButton(onClick = {
                                        item.sets.removeAt(index)
                                    }) {
                                        Icon(Icons.Default.Close, contentDescription = "Usuń serię")
                                    }
                                }

                                Spacer(modifier = Modifier.height(4.dp))
                            }

                            Spacer(modifier = Modifier.height(4.dp))
                            Button(onClick = {
                                item.sets.add(ExerciseSetDraft())
                            }) {
                                Text("Dodaj serię")
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.saveRoutineWithExercises(routineName, routineId, draftExercises)
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Zapisz rutynę")
            }
        }
    }
}
