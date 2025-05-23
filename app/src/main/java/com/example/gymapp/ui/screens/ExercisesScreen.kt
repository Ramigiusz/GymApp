// ui/screens/ExercisesScreen.kt
package com.example.gymapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymapp.ui.components.ExerciseWithTagItem
import com.example.gymapp.viewmodel.ExerciseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExercisesScreen(viewModel: ExerciseViewModel = viewModel()) {
    // —— formularz dodawania ćwiczenia ——
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    // —— pobieramy ćwiczenia z tagami ——
    val exercisesWithTags by remember { derivedStateOf { viewModel.allExercisesWithTags } }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Baza ćwiczeń") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nazwa ćwiczenia") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Opis ćwiczenia") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        viewModel.addExercise(name.trim(), description.trim())
                        name = ""
                        description = ""
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Dodaj ćwiczenie")
            }
            Spacer(Modifier.height(16.dp))

            Text("Lista ćwiczeń:", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

            // —— lista ćwiczeń z tagami, przewijalna ——
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(exercisesWithTags, key = { it.exercise.id }) { ewt ->
                    ExerciseWithTagItem(
                        ewt = ewt,
                        onDelete = { viewModel.deleteExercise(it) },
                        onEdit = { viewModel.updateExercise(it) }
                    )
                }
            }
        }
    }
}
