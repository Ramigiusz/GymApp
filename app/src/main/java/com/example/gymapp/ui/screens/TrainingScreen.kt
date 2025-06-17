package com.example.gymapp.ui.screens

import android.app.Activity
import android.media.MediaPlayer
import android.net.Uri
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.compose.animation.core.*
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.IntOffset
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
    var showCannon by remember { mutableStateOf(false) }
    var showConfetti1 by remember { mutableStateOf(false) }
    var showConfetti2 by remember { mutableStateOf(false) }
    var showConfetti3 by remember { mutableStateOf(false) }
    var showConfetti4 by remember { mutableStateOf(false) }
    var showConfetti5 by remember { mutableStateOf(false) }
    var showConfetti6 by remember { mutableStateOf(false) }
    var showConfetti7 by remember { mutableStateOf(false) }
    var showConfetti8 by remember { mutableStateOf(false) }
    var showConfetti9 by remember { mutableStateOf(false) }
    var showConfetti10 by remember { mutableStateOf(false) }

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

            val restColor =
                if (restTimeLeft <= 10 && isResting) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
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
                    items(
                        draftExercises.withIndex().toList(),
                        key = { (index, item) -> "${item.exercise.id}_$index" }) { (_, draft) ->
                        AnimatedVisibility(
                            visible = true,
                            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                            exit = fadeOut()
                        ) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(Modifier.padding(12.dp)) {
                                    Text(
                                        draft.exercise.name,
                                        style = MaterialTheme.typography.titleMedium
                                    )

                                    lastLogs[draft.exercise.id]?.let { log ->
                                        Text(
                                            "Ostatni wynik: ${log.reps} × ${log.weight}kg (RPE ${log.rpe})",
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }

                                    if (draft.exercise.description.isNotBlank()) {
                                        Spacer(Modifier.height(4.dp))
                                        Text(
                                            draft.exercise.description,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }

                                    Spacer(Modifier.height(8.dp))

                                    draft.sets.forEachIndexed { index, set ->
                                        val lastLog = lastLogs[draft.exercise.id]

                                        val repsState = remember {
                                            mutableStateOf(
                                                if (set.reps == 10 && lastLog != null) {
                                                    lastLog.reps.toString()
                                                } else set.reps.toString()
                                            )
                                        }

                                        val weightState = remember {
                                            mutableStateOf(
                                                if (set.weight == 0f && lastLog != null) {
                                                    lastLog.weight.toString()
                                                } else set.weight.toString()
                                            )
                                        }

                                        val completedState =
                                            remember { mutableStateOf(set.completed) }

                                        val rowAlpha = if (completedState.value) 0.5f else 1f
                                        val textStyle = if (completedState.value)
                                            LocalTextStyle.current.copy(textDecoration = TextDecoration.LineThrough)
                                        else LocalTextStyle.current

                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp)
                                                .alpha(rowAlpha),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            OutlinedTextField(
                                                value = repsState.value,
                                                onValueChange = {
                                                    repsState.value = it
                                                    set.reps = it.toIntOrNull() ?: set.reps
                                                },
                                                label = { Text("Powt.") },
                                                modifier = Modifier.weight(1f),
                                                textStyle = textStyle
                                            )
                                            Spacer(Modifier.width(8.dp))
                                            OutlinedTextField(
                                                value = weightState.value,
                                                onValueChange = {
                                                    weightState.value = it
                                                    set.weight = it.toFloatOrNull() ?: set.weight
                                                },
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
                                                        restTimeLeft =
                                                            draft.restMinutes * 60 + draft.restSeconds
                                                        isResting = true

                                                        // Zapisz log do ExerciseLog
                                                        val reps =
                                                            repsState.value.toIntOrNull() ?: 0
                                                        val weight =
                                                            weightState.value.toFloatOrNull() ?: 0f

                                                        if (reps > 0 && weight >= 0f) {
                                                            coroutineScope.launch {
                                                                val log = ExerciseLog(
                                                                    exerciseId = draft.exercise.id,
                                                                    reps = reps,
                                                                    weight = weight,
                                                                    rpe = set.rpe
                                                                )
                                                                exerciseLogDao.insert(log)
                                                                println("Inserted log: $log")
                                                                val updatedLog =
                                                                    exerciseLogDao.getLastLogForExercise(
                                                                        draft.exercise.id
                                                                    )
                                                                updatedLog?.let {
                                                                    lastLogs =
                                                                        lastLogs + (draft.exercise.id to it)
                                                                }
                                                            }
                                                        } else {
                                                            println("Invalid reps or weight: reps=$reps, weight=$weight")
                                                        }
                                                    }
                                                }
                                            ) {
                                                val scale by animateFloatAsState(
                                                    targetValue = if (completedState.value) 1.2f else 1f,
                                                    animationSpec = tween(durationMillis = 200)
                                                )

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
                            showCannon = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text("✅ Zakończ trening")
                    }
                }
            }

            // Animacja confetti nad dialogiem
            if (showWorkoutEndDialog || showCannon) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center)
                ) {
                    // Confetti 1
                    val offsetY1 by animateIntOffsetAsState(
                        targetValue = if (showConfetti1) IntOffset(-180, 1500) else IntOffset(
                            -180,
                            30
                        ),
                        animationSpec = tween(
                            durationMillis = 2000,
                            easing = LinearEasing
                        ),
                        finishedListener = { showConfetti1 = false }
                    )
                    val alpha1 by animateFloatAsState(
                        targetValue = if (showConfetti1) 0f else 1f,
                        animationSpec = tween(
                            durationMillis = 2000,
                            easing = LinearEasing
                        )
                    )
                    if (showConfetti1) {
                        Text(
                            text = "🎊",
                            fontSize = 36.sp,
                            modifier = Modifier
                                .offset { offsetY1 }
                                .alpha(alpha1)
                                .align(Alignment.TopCenter)
                        )
                    }

                    // Confetti 2
                    val offsetY2 by animateIntOffsetAsState(
                        targetValue = if (showConfetti2) IntOffset(-140, 2000) else IntOffset(
                            -140,
                            60
                        ),
                        animationSpec = tween(
                            durationMillis = 2000,
                            easing = LinearEasing
                        ),
                        finishedListener = { showConfetti2 = false }
                    )
                    val alpha2 by animateFloatAsState(
                        targetValue = if (showConfetti2) 0f else 1f,
                        animationSpec = tween(
                            durationMillis = 2000,
                            easing = LinearEasing
                        )
                    )
                    if (showConfetti2) {
                        Text(
                            text = "🎊",
                            fontSize = 36.sp,
                            modifier = Modifier
                                .offset { offsetY2 }
                                .alpha(alpha2)
                                .align(Alignment.TopCenter)
                        )
                    }

                    // Confetti 3
                    val offsetY3 by animateIntOffsetAsState(
                        targetValue = if (showConfetti3) IntOffset(-100, 1800) else IntOffset(
                            -100,
                            0
                        ),
                        animationSpec = tween(
                            durationMillis = 2000,
                            easing = LinearEasing
                        ),
                        finishedListener = { showConfetti3 = false }
                    )
                    val alpha3 by animateFloatAsState(
                        targetValue = if (showConfetti3) 0f else 1f,
                        animationSpec = tween(
                            durationMillis = 2000,
                            easing = LinearEasing
                        )
                    )
                    if (showConfetti3) {
                        Text(
                            text = "🎊",
                            fontSize = 36.sp,
                            modifier = Modifier
                                .offset { offsetY3 }
                                .alpha(alpha3)
                                .align(Alignment.TopCenter)
                        )
                    }

                    // Confetti 4
                    val offsetY4 by animateIntOffsetAsState(
                        targetValue = if (showConfetti4) IntOffset(-60, 1000) else IntOffset(
                            -60,
                            -30
                        ),
                        animationSpec = tween(
                            durationMillis = 2000,
                            easing = LinearEasing
                        ),
                        finishedListener = { showConfetti4 = false }
                    )
                    val alpha4 by animateFloatAsState(
                        targetValue = if (showConfetti4) 0f else 1f,
                        animationSpec = tween(
                            durationMillis = 2000,
                            easing = LinearEasing
                        )
                    )
                    if (showConfetti4) {
                        Text(
                            text = "🎊",
                            fontSize = 36.sp,
                            modifier = Modifier
                                .offset { offsetY4 }
                                .alpha(alpha4)
                                .align(Alignment.TopCenter)
                        )
                    }

                    // Confetti 5
                    val offsetY5 by animateIntOffsetAsState(
                        targetValue = if (showConfetti5) IntOffset(-20, 1700) else IntOffset(
                            -20,
                            30
                        ),
                        animationSpec = tween(
                            durationMillis = 2000,
                            easing = LinearEasing
                        ),
                        finishedListener = { showConfetti5 = false }
                    )
                    val alpha5 by animateFloatAsState(
                        targetValue = if (showConfetti5) 0f else 1f,
                        animationSpec = tween(
                            durationMillis = 2000,
                            easing = LinearEasing
                        )
                    )
                    if (showConfetti5) {
                        Text(
                            text = "🎊",
                            fontSize = 36.sp,
                            modifier = Modifier
                                .offset { offsetY5 }
                                .alpha(alpha5)
                                .align(Alignment.TopCenter)
                        )
                    }

                    // Confetti 6
                    val offsetY6 by animateIntOffsetAsState(
                        targetValue = if (showConfetti6) IntOffset(20, 1300) else IntOffset(20, 60),
                        animationSpec = tween(
                            durationMillis = 2000,
                            easing = LinearEasing
                        ),
                        finishedListener = { showConfetti6 = false }
                    )
                    val alpha6 by animateFloatAsState(
                        targetValue = if (showConfetti6) 0f else 1f,
                        animationSpec = tween(
                            durationMillis = 2000,
                            easing = LinearEasing
                        )
                    )
                    if (showConfetti6) {
                        Text(
                            text = "🎊",
                            fontSize = 36.sp,
                            modifier = Modifier
                                .offset { offsetY6 }
                                .alpha(alpha6)
                                .align(Alignment.TopCenter)
                        )
                    }

                    // Confetti 7
                    val offsetY7 by animateIntOffsetAsState(
                        targetValue = if (showConfetti7) IntOffset(60, 1200) else IntOffset(60, 0),
                        animationSpec = tween(
                            durationMillis = 2000,
                            easing = LinearEasing
                        ),
                        finishedListener = { showConfetti7 = false }
                    )
                    val alpha7 by animateFloatAsState(
                        targetValue = if (showConfetti7) 0f else 1f,
                        animationSpec = tween(
                            durationMillis = 2000,
                            easing = LinearEasing
                        )
                    )
                    if (showConfetti7) {
                        Text(
                            text = "🎊",
                            fontSize = 36.sp,
                            modifier = Modifier
                                .offset { offsetY7 }
                                .alpha(alpha7)
                                .align(Alignment.TopCenter)
                        )
                    }

                    // Confetti 8
                    val offsetY8 by animateIntOffsetAsState(
                        targetValue = if (showConfetti8) IntOffset(100, 2000) else IntOffset(
                            100,
                            -50
                        ),
                        animationSpec = tween(
                            durationMillis = 2000,
                            easing = LinearEasing
                        ),
                        finishedListener = { showConfetti8 = false }
                    )
                    val alpha8 by animateFloatAsState(
                        targetValue = if (showConfetti8) 0f else 1f,
                        animationSpec = tween(
                            durationMillis = 2000,
                            easing = LinearEasing
                        )
                    )
                    if (showConfetti8) {
                        Text(
                            text = "🎊",
                            fontSize = 36.sp,
                            modifier = Modifier
                                .offset { offsetY8 }
                                .alpha(alpha8)
                                .align(Alignment.TopCenter)
                        )
                    }

                    // Confetti 9
                    val offsetY9 by animateIntOffsetAsState(
                        targetValue = if (showConfetti9) IntOffset(140, 1900) else IntOffset(
                            140,
                            90
                        ),
                        animationSpec = tween(
                            durationMillis = 2000,
                            easing = LinearEasing
                        ),
                        finishedListener = { showConfetti9 = false }
                    )
                    val alpha9 by animateFloatAsState(
                        targetValue = if (showConfetti9) 0f else 1f,
                        animationSpec = tween(
                            durationMillis = 2000,
                            easing = LinearEasing
                        )
                    )
                    if (showConfetti9) {
                        Text(
                            text = "🎊",
                            fontSize = 36.sp,
                            modifier = Modifier
                                .offset { offsetY9 }
                                .alpha(alpha9)
                                .align(Alignment.TopCenter)
                        )
                    }

                    // Confetti 10
                    val offsetY10 by animateIntOffsetAsState(
                        targetValue = if (showConfetti10) IntOffset(180, 1000) else IntOffset(
                            180,
                            0
                        ),
                        animationSpec = tween(
                            durationMillis = 2000,
                            easing = LinearEasing
                        ),
                        finishedListener = { showConfetti10 = false }
                    )
                    val alpha10 by animateFloatAsState(
                        targetValue = if (showConfetti10) 0f else 1f,
                        animationSpec = tween(
                            durationMillis = 2000,
                            easing = LinearEasing
                        )
                    )
                    if (showConfetti10) {
                        Text(
                            text = "🎊",
                            fontSize = 36.sp,
                            modifier = Modifier
                                .offset { offsetY10 }
                                .alpha(alpha10)
                                .align(Alignment.TopCenter)
                        )
                    }
                }
            }
        }

        if (showRestEndDialog) {
            AlertDialog(
                onDismissRequest = { showRestEndDialog = false },
                title = { Text("Przerwa zakończona") },
                text = { Text("Czas wrócić do treningu!") },
                confirmButton = {
                    Button(onClick = { showRestEndDialog = false }) {
                        Text("OK")
                    }
                }
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
                        coroutineScope.launch {
                            delay(500)
                            showConfetti1 = true
                            showConfetti2 = true
                            showConfetti3 = true
                            showConfetti4 = true
                            showConfetti5 = true
                            showConfetti6 = true
                            showConfetti7 = true
                            showConfetti8 = true
                            showConfetti9 = true
                            showConfetti10 = true
                            delay(4100) // Całkowity czas animacji: 4 sekundy + 100 ms bufor
                            showCannon = false
                            navController.navigate("start") {
                                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            }
                        }
                    }) {
                        Text("OK")
                    }
                }
            )
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