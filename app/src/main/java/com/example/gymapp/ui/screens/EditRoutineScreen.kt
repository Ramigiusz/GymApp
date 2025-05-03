package com.example.gymapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gymapp.viewmodel.RoutineViewModel
import androidx.lifecycle.viewmodel.compose.viewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditRoutineScreen(
    navController: NavController,
    routineId: Int?,
    viewModel: RoutineViewModel = viewModel()
) {
    var routineName by remember { mutableStateOf("") }
    val routines = viewModel.routines

    // Jeśli edytujemy istniejącą rutynę, znajdź ją
    LaunchedEffect(routineId) {
        if (routineId != null && routineId > 0) {
            val existingRoutine = viewModel.routines.find { it.id == routineId }
            routineName = existingRoutine?.name ?: ""
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
            TextField(
                value = routineName,
                onValueChange = { routineName = it },
                label = { Text("Routine Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (routineId == 0) {
                        // Nowa rutyna
                        viewModel.addRoutine(routineName)
                    } else {
                        // Edycja istniejącej
                        viewModel.updateRoutine(routineId!!, routineName)
                    }
                    navController.popBackStack() // Wróć po zapisaniu
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save")
            }
        }
    }
}