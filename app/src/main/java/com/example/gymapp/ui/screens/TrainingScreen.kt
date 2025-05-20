// ui/screens/TrainingScreen.kt
package com.example.gymapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gymapp.data.draft.RoutineExerciseDraft
import com.example.gymapp.viewmodel.RoutineViewModel
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainingScreen(
    navController: NavController,
    routineId: Int?,
    viewModel: RoutineViewModel = viewModel()
) {
    // Stoper
    var elapsedMs by remember { mutableStateOf(0L) }
    var isRunning by remember { mutableStateOf(false) }

    LaunchedEffect(isRunning) {
        if (isRunning) {
            while (true) {
                delay(1000L)
                elapsedMs += 1000L
            }
        }
    }

    // Lista ćwiczeń
    var draftExercises by remember { mutableStateOf<List<RoutineExerciseDraft>>(emptyList()) }

    LaunchedEffect(routineId) {
        routineId?.let { id ->
            viewModel.loadExercisesForRoutine(id) { loaded ->
                draftExercises = loaded
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Trening") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Wróć")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // STOPER
            Text(
                text = formatElapsed(elapsedMs),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(Modifier.height(8.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(onClick = { isRunning = true }, enabled = !isRunning) {
                    Icon(
                        painter = painterResource(android.R.drawable.ic_media_play),
                        contentDescription = "Start"
                    )
                }
                IconButton(onClick = { isRunning = false }, enabled = isRunning) {
                    Icon(
                        painter = painterResource(android.R.drawable.ic_media_pause),
                        contentDescription = "Pauza"
                    )
                }
                IconButton(onClick = {
                    isRunning = false
                    elapsedMs = 0L
                }) {
                    Icon(
                        painter = painterResource(android.R.drawable.ic_menu_revert),
                        contentDescription = "Reset"
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // LISTA ĆWICZEŃ
            Text("Ćwiczenia w rutynie:", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

            if (draftExercises.isEmpty()) {
                Text("Brak ćwiczeń w tej rutynie", style = MaterialTheme.typography.bodyMedium)
            } else {
                LazyColumn(
                    Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    items(draftExercises, key = { it.exercise.id }) { draft ->
                        var weightInput by remember { mutableStateOf("") }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(Modifier.padding(12.dp)) {
                                Text(draft.exercise.name, style = MaterialTheme.typography.titleMedium)
                                if (draft.exercise.description.isNotBlank()) {
                                    Spacer(Modifier.height(4.dp))
                                    Text(draft.exercise.description, style = MaterialTheme.typography.bodyMedium)
                                }

                                Spacer(Modifier.height(8.dp))

                                OutlinedTextField(
                                    value = weightInput,
                                    onValueChange = { weightInput = it },
                                    label = { Text("Ciężar (kg)") },
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// Format czasu
private fun formatElapsed(ms: Long): String {
    val hours = TimeUnit.MILLISECONDS.toHours(ms)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(ms) % 60
    val seconds = TimeUnit.MILLISECONDS.toSeconds(ms) % 60
    return "%02d:%02d:%02d".format(hours, minutes, seconds)
}
