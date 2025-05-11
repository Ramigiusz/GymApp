package com.example.gymapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymapp.viewmodel.ExerciseViewModel
import com.example.gymapp.data.model.Exercise
import com.example.gymapp.ui.components.ExerciseItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExercisesScreen(viewModel: ExerciseViewModel = viewModel()) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Baza ćwiczeń") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nazwa ćwiczenia") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Opis ćwiczenia") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        viewModel.addExercise(name, description)
                        name = ""
                        description = ""
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Dodaj ćwiczenie")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Lista ćwiczeń:", style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn {
                items(viewModel.exercises) { exercise ->
                    ExerciseItem(exercise, onDelete = { viewModel.deleteExercise(it) })
                }
            }
        }
    }
}
