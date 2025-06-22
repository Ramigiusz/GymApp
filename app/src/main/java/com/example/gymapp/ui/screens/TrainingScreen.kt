package com.example.gymapp.ui.screens

import android.media.MediaPlayer
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gymapp.R
import com.example.gymapp.data.draft.RoutineExerciseDraft
import com.example.gymapp.data.model.ExerciseLog
import com.example.gymapp.data.db.AppDatabase
import com.example.gymapp.viewmodel.RoutineViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainingScreen(
    navController: NavController,
    routineId: Int?,
    viewModel: RoutineViewModel = viewModel()
) {
    val context = LocalContext.current
    val mediaPlayer = remember { MediaPlayer.create(context, R.raw.clock_alarm) }
    val coroutineScope = rememberCoroutineScope()
    val db = AppDatabase.getDatabase(context)
    val exerciseLogDao = db.exerciseLogDao()

    var elapsedMs by remember { mutableStateOf(0L) }
    var isRunning by remember { mutableStateOf(false) }
    var restTimeLeft by remember { mutableStateOf(0) }
    var isResting by remember { mutableStateOf(false) }
    var showRestEndDialog by remember { mutableStateOf(false) }
    var showWorkoutEndDialog by remember { mutableStateOf(false) }
    var isConfettiActive by remember { mutableStateOf(false) }

    LaunchedEffect(isRunning) {
        if (isRunning) {
            while (true) {
                delay(1000L)
                elapsedMs += 1000L
            }
        }
    }

    LaunchedEffect(isResting) {
        if (isResting && restTimeLeft > 0) {
            while (restTimeLeft > 0) {
                delay(1000L)
                restTimeLeft -= 1
            }
            isResting = false
            mediaPlayer.seekTo(0)
            mediaPlayer.start()
            showRestEndDialog = true
        }
    }

    var draftExercises by remember { mutableStateOf<List<RoutineExerciseDraft>>(emptyList()) }
    var lastLogs by remember { mutableStateOf<Map<Int, ExerciseLog>>(emptyMap()) }

    LaunchedEffect(routineId) {
        routineId?.let { id ->
            viewModel.loadExercisesForRoutine(id) { loaded ->
                draftExercises = loaded
                isRunning = true
                coroutineScope.launch {
                    lastLogs = loaded.mapNotNull { draft ->
                        val log = exerciseLogDao.getLastLogForExercise(draft.exercise.id)
                        log?.let { draft.exercise.id to it }
                    }.toMap()
                }
            }
        }
    }

    // Główny kontener Box
    Box {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Trening")
                            Text(
                                text = "🕒 ${formatElapsed(elapsedMs)}",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    },
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
                Spacer(Modifier.height(4.dp))

                val restColor = if (restTimeLeft <= 10 && isResting) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                Text(
                    text = "⏳ Przerwa: ${formatRestTime(restTimeLeft)}",
                    style = MaterialTheme.typography.titleMedium.copy(color = restColor),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                if (isResting && restTimeLeft > 0) {
                    val restTotal = draftExercises.firstOrNull()?.restMinutes?.times(60)?.plus(
                        draftExercises.firstOrNull()?.restSeconds ?: 0
                    ) ?: 60
                    val progress by animateFloatAsState(
                        targetValue = restTimeLeft / restTotal.toFloat(),
                        animationSpec = tween(durationMillis = 500)
                    )
                    LinearProgressIndicator(
                        progress = progress,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(MaterialTheme.shapes.extraSmall)
                    )
                }

                Spacer(Modifier.height(8.dp))
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
                        items(draftExercises.withIndex().toList(), key = { (index, item) -> "${item.exercise.id}_$index" }) { (_, draft) ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(Modifier.padding(12.dp)) {
                                    Text(draft.exercise.name, style = MaterialTheme.typography.titleMedium)
                                    lastLogs[draft.exercise.id]?.let { log ->
                                        Text(
                                            "Ostatni wynik: ${log.reps} × ${log.weight}kg (RPE ${log.rpe})",
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                    if (draft.exercise.description.isNotBlank()) {
                                        Spacer(Modifier.height(4.dp))
                                        Text(draft.exercise.description, style = MaterialTheme.typography.bodyMedium)
                                    }
                                    Spacer(Modifier.height(8.dp))

                                    draft.sets.forEachIndexed { _, set ->
                                        val lastLog = lastLogs[draft.exercise.id]
                                        val repsState = remember { mutableStateOf(if (set.reps == 10 && lastLog != null) lastLog.reps.toString() else set.reps.toString()) }
                                        val weightState = remember { mutableStateOf(if (set.weight == 0f && lastLog != null) lastLog.weight.toString() else set.weight.toString()) }
                                        val completedState = remember { mutableStateOf(set.completed) }
                                        val rowAlpha = if (completedState.value) 0.5f else 1f
                                        val textStyle = if (completedState.value) LocalTextStyle.current.copy(textDecoration = TextDecoration.LineThrough) else LocalTextStyle.current

                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp)
                                                .alpha(rowAlpha),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            OutlinedTextField(
                                                value = repsState.value,
                                                onValueChange = { repsState.value = it; set.reps = it.toIntOrNull() ?: set.reps },
                                                label = { Text("Powt.") },
                                                modifier = Modifier.weight(1f),
                                                textStyle = textStyle
                                            )
                                            Spacer(Modifier.width(8.dp))
                                            OutlinedTextField(
                                                value = weightState.value,
                                                onValueChange = { weightState.value = it; set.weight = it.toFloatOrNull() ?: set.weight },
                                                label = { Text("Ciężar (kg)") },
                                                modifier = Modifier.weight(1f),
                                                textStyle = textStyle
                                            )
                                            Spacer(Modifier.width(8.dp))
                                            Text("@ ${set.rpe}")
                                            Spacer(Modifier.width(8.dp))
                                            IconButton(
                                                onClick = {
                                                    val checked = !completedState.value
                                                    completedState.value = checked
                                                    set.completed = checked
                                                    if (checked) {
                                                        restTimeLeft = draft.restMinutes * 60 + draft.restSeconds
                                                        isResting = true
                                                        val reps = repsState.value.toIntOrNull() ?: 0
                                                        val weight = weightState.value.toFloatOrNull() ?: 0f
                                                        if (reps > 0 && weight >= 0f) {
                                                            coroutineScope.launch {
                                                                val log = ExerciseLog(
                                                                    exerciseId = draft.exercise.id,
                                                                    reps = reps,
                                                                    weight = weight,
                                                                    rpe = set.rpe
                                                                )
                                                                exerciseLogDao.insert(log)
                                                                val updatedLog = exerciseLogDao.getLastLogForExercise(draft.exercise.id)
                                                                updatedLog?.let { lastLogs = lastLogs + (draft.exercise.id to it) }
                                                            }
                                                        }
                                                    }
                                                }
                                            ) {
                                                val scale by animateFloatAsState(targetValue = if (completedState.value) 1.2f else 1f, animationSpec = tween(durationMillis = 200))
                                                Icon(
                                                    imageVector = if (completedState.value) Icons.Default.Clear else Icons.Default.Check,
                                                    contentDescription = if (completedState.value) "Wykonano" else "Niewykonane",
                                                    tint = if (completedState.value) MaterialTheme.colorScheme.primary else Color.Gray,
                                                    modifier = Modifier.scale(scale)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))

                if (draftExercises.isNotEmpty()) {
                    Button(
                        onClick = {
                            isRunning = false
                            showWorkoutEndDialog = true
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("✅ Zakończ trening")
                    }
                }
            }

            if (showRestEndDialog) {
                AlertDialog(
                    onDismissRequest = { showRestEndDialog = false },
                    title = { Text("Przerwa zakończona") },
                    text = { Text("Czas wrócić do treningu!") },
                    confirmButton = { Button(onClick = { showRestEndDialog = false }) { Text("OK") } }
                )
            }

            if (showWorkoutEndDialog) {
                AlertDialog(
                    onDismissRequest = { },
                    title = { Text("Trening zakończony: ${formatElapsed(elapsedMs)}") },
                    text = { Text("Gratulacje, świetny trening!") },
                    confirmButton = {
                        Button(onClick = {
                            showWorkoutEndDialog = false
                            isConfettiActive = true
                        }) { Text("OK") }
                    }
                )
            }
        }

        // Efekt konfetti nad wszystkim
        if (isConfettiActive) {
            LaunchedEffect(Unit) {
                delay(2000) // Czas trwania animacji
                isConfettiActive = false
                navController.navigate("start") {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                }
            }
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                val confettiCount = 16
                repeat(confettiCount) { index ->
                    val initialX = (index - 7.5f) * 40 // Dostosowanie zakresu poziomego dla 16 elementów
                    val initialY = remember { -100f - (index % 4) * 20f } // Różne wysokości początkowe (od -100 do -160)
                    val yPosition = remember { Animatable(initialY) }
                    val alpha = remember { Animatable(1f) }

                    LaunchedEffect(isConfettiActive) {
                        if (isConfettiActive) {
                            val duration = when (index % 4) { // Różne długości animacji
                                0 -> 2000
                                1 -> 2500
                                2 -> 3000
                                3 -> 2800
                                else -> 2000
                            }
                            yPosition.animateTo(
                                targetValue = 1500f + (index % 4) * 200f, // Różne długości opadania
                                animationSpec = tween(durationMillis = duration)
                            )
                            alpha.animateTo(0f, animationSpec = tween(durationMillis = duration))
                        }
                    }

                    Text(
                        text = "🎊",
                        fontSize = 36.sp,
                        modifier = Modifier
                            .offset { IntOffset(initialX.toInt(), yPosition.value.toInt()) }
                            .alpha(alpha.value)
                    )
                }
            }
        }
    }
}

private fun formatElapsed(ms: Long): String {
    val hours = TimeUnit.MILLISECONDS.toHours(ms)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(ms) % 60
    val seconds = TimeUnit.MILLISECONDS.toSeconds(ms) % 60
    return "%02d:%02d:%02d".format(hours, minutes, seconds)
}

private fun formatRestTime(seconds: Int): String {
    val min = seconds / 60
    val sec = seconds % 60
    return "%01d:%02d".format(min, sec)
}