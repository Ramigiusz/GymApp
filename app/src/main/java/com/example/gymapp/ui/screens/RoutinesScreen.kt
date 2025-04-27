// ui/screens/SettingsScreen.kt
package com.example.gymapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gymapp.ui.components.RoutineCard
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymapp.viewmodel.RoutineViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutinesScreen(
    navController: NavController,
    viewModel: RoutineViewModel = viewModel()
) {
    var showDialog by remember { mutableStateOf(false) }
    var routineToDelete by remember { mutableStateOf<com.example.gymapp.data.model.Routine?>(null) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Lista Rutyn") }) },
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
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("edit_routine_screen/0") }, // 0 = nowa rutyna
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Routine")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(viewModel.routines.size) { index ->
                    val routine = viewModel.routines[index]
                    RoutineCard(
                        name = routine.name,
                        onStart = { navController.navigate("training_screen/${routine.id}") },
                        onEdit = { navController.navigate("edit_routine_screen/${routine.id}") },
                        onDelete = {
                            routineToDelete = routine
                            showDialog = true
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        // Popup dialog
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Delete Routine") },
                text = { Text("Are you sure you want to delete this routine?") },
                confirmButton = {
                    TextButton(onClick = {
                        routineToDelete?.let { viewModel.deleteRoutine(it) }
                        showDialog = false
                    }) {
                        Text("Yes")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("No")
                    }
                }
            )
        }
    }
}

