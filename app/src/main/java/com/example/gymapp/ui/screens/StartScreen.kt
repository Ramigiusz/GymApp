package com.example.gymapp.ui.screens

import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymapp.viewmodel.RoutineViewModel
import com.example.gymapp.data.model.Routine
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gymapp.ui.components.RoutineCard
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartScreen(navController: NavController) {
    // ⏰ Aktualizowana data i godzina
    var currentDate by remember { mutableStateOf(getCurrentDate()) }
    var currentTime by remember { mutableStateOf(getCurrentTime()) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(60_000) // aktualizacja co minutę
            currentDate = getCurrentDate()
            currentTime = getCurrentTime()
        }
    }

    Scaffold(
        bottomBar = {
            BottomAppBar {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    BottomNavItem(
                        icon = Icons.Default.List,
                        label = "Rutyny",
                        onClick = { navController.navigate("routines") }
                    )
                    BottomNavItem(
                        icon = Icons.Default.Home,
                        label = "Start",
                        onClick = { navController.navigate("start") }
                    )
                    BottomNavItem(
                        icon = Icons.Default.Settings,
                        label = "Ustawienia",
                        onClick = { navController.navigate("settings") }
                    )
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Colossus", fontSize = 32.sp, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(8.dp))

            Text(currentDate, fontSize = 18.sp, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(32.dp)) // przerwa między datą a godziną
            Text(currentTime, fontSize = 64.sp, style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(32.dp))

            Text("Ostatnie treningi", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            // Ostatnia rutyna
            // pod warunkiem @OptIn i Scaffold, wewnątrz Column (tam gdzie były stare karty)
            val routineViewModel: RoutineViewModel = viewModel()
            val routines: List<Routine> by remember {
                derivedStateOf { routineViewModel.routines }
            }
// obetnij do trzech
            val displayRoutines = routines.take(2)

            if (displayRoutines.isEmpty()) {
                Text(
                    text = "0",
                    style = MaterialTheme.typography.headlineLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                displayRoutines.forEach { r ->
                    RoutineCard(
                        name = r.name,
                        onStart = { navController.navigate("training_screen/${r.id}") },
                        onEdit  = { navController.navigate("edit_routine_screen/${r.id}") },
                        onDelete= { routineViewModel.deleteRoutine(r) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

        }
    }
}

// Pomocnicza funkcja do formatu daty i godziny
fun getCurrentDate(): String {
    val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    return formatter.format(Date())
}

fun getCurrentTime(): String {
    val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    return formatter.format(Date())
}

// 🔽 Pomocniczy composable dla przycisków w dolnym pasku
@Composable
fun BottomNavItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .clickable(onClick = onClick)
    ) {
        Icon(
            icon,
            contentDescription = label,
            modifier = Modifier.size(36.dp) // zwiększony rozmiar ikon
        )
        Text(
            text = label,
            textAlign = TextAlign.Center,
            fontSize = 12.sp
        )
    }
}


