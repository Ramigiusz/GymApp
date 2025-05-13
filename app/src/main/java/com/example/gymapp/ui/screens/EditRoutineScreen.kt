package com.example.gymapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gymapp.data.model.Exercise
import com.example.gymapp.viewmodel.ExerciseViewModel
import com.example.gymapp.viewmodel.RoutineViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

data class RoutineExerciseDraft(
    val exercise: Exercise,
    var sets: Int = 3,
    var reps: Int = 10,
    var rpe: Float = 8f,
    var restMinutes: Int = 1,
    var restSeconds: Int = 0
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditRoutineScreen(
    navController: NavController,
    routineId: Int?,
    viewModel: RoutineViewModel = viewModel(),
    viewModelExercise: ExerciseViewModel = viewModel()
) {
    var routineName by remember { mutableStateOf("") }
    val routines = viewModel.routines
    val allExercises = viewModelExercise.exercises

    var expanded by remember { mutableStateOf(false) }
    var draftExercises by remember { mutableStateOf<List<RoutineExerciseDraft>>(emptyList()) }

    LaunchedEffect(routineId) {
        if (routineId != null && routineId > 0) {
            val existingRoutine = viewModel.routines.find { it.id == routineId }
            routineName = existingRoutine?.name ?: ""

            viewModel.loadExercisesForRoutine(routineId) { loaded ->
                draftExercises = loaded
            }
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (routineId == 0) "New Routine" else "Edit Routine") }
            )
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

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                allExercises.forEach { exercise ->
                    DropdownMenuItem(
                        text = { Text(exercise.name) },
                        onClick = {
                            draftExercises = draftExercises + RoutineExerciseDraft(exercise)
                            expanded = false
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text("Ćwiczenia w rutynie:", style = MaterialTheme.typography.titleMedium)

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(draftExercises) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(item.exercise.name, style = MaterialTheme.typography.titleMedium)

                            Row(Modifier.fillMaxWidth()) {
                                OutlinedTextField(
                                    value = item.sets.toString(),
                                    onValueChange = {
                                        item.sets = it.toIntOrNull() ?: item.sets
                                    },
                                    label = { Text("Serie") },
                                    modifier = Modifier.weight(1f)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                OutlinedTextField(
                                    value = item.reps.toString(),
                                    onValueChange = {
                                        item.reps = it.toIntOrNull() ?: item.reps
                                    },
                                    label = { Text("Powt.") },
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(Modifier.fillMaxWidth()) {
                                OutlinedTextField(
                                    value = item.rpe.toString(),
                                    onValueChange = {
                                        item.rpe = it.toFloatOrNull() ?: item.rpe
                                    },
                                    label = { Text("RPE") },
                                    modifier = Modifier.weight(1f)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                OutlinedTextField(
                                    value = item.restMinutes.toString(),
                                    onValueChange = {
                                        item.restMinutes = it.toIntOrNull() ?: item.restMinutes
                                    },
                                    label = { Text("Min.") },
                                    modifier = Modifier.weight(1f)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                OutlinedTextField(
                                    value = item.restSeconds.toString(),
                                    onValueChange = {
                                        item.restSeconds = it.toIntOrNull() ?: item.restSeconds
                                    },
                                    label = { Text("Sek.") },
                                    modifier = Modifier.weight(1f)
                                )
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
