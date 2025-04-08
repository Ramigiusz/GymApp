// ui/screens/SettingsScreen.kt
package com.example.gymapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
    Scaffold(
        topBar = { TopAppBar(title = { Text("Lista Rutyn") }) },
        bottomBar = { BottomAppBar { Text("Bottom Bar", modifier = Modifier.padding(16.dp)) } }
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
                        onStart = { /* TODO: Start routine */ },
                        onEdit = { /* TODO: Edit routine */ }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            OutlinedTextField(
                value = viewModel.newRoutineName,
                onValueChange = { viewModel.newRoutineName = it },
                label = { Text("New Routine Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { viewModel.addRoutine() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text("ADD NEW ROUTINE")
            }
        }
    }
}

