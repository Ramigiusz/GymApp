package com.example.gymapp.ui.screens

import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymapp.viewmodel.RoutineViewModel
import com.example.gymapp.data.model.Routine
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.clip
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
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically) {
                    // Rutyny
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(25))
                            .clickable { navController.navigate("routines") },
                        contentAlignment = Alignment.Center
                    ) {
                        BottomNavItem(
                            icon = Icons.Default.List,
                            label = "Rutyny",
                            isSelected = false
                        )
                    }

                    // Start
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(25))
                            .clickable { navController.navigate("start") },
                        contentAlignment = Alignment.Center
                    ) {
                        BottomNavItem(
                            icon = Icons.Default.Home,
                            label = "Start",
                            isSelected = true
                        )
                    }

                    // Ustawienia
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(25))
                            .clickable { navController.navigate("settings") },
                        contentAlignment = Alignment.Center
                    ) {
                        BottomNavItem(
                            icon = Icons.Default.Settings,
                            label = "Ustawienia",
                            isSelected = false
                        )
                    }
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
// obetnij do dwóch pierwszych wpisów
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
fun BottomNavItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isSelected: Boolean = false
) {
    // kolor ikony/napisu
    val contentColor = if (isSelected) MaterialTheme.colorScheme.primary else LocalContentColor.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(vertical = 2.dp)
    ) {
        Icon(
            icon,
            contentDescription = label,
            modifier = Modifier.size(36.dp), // zwiększony rozmiar ikon
            tint = contentColor
        )
        Text(
            text = label,
            textAlign = TextAlign.Center,
            fontSize = 12.sp,
            color = contentColor
        )
    }
}


