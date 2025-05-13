package com.example.gymapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gymapp.data.model.Exercise

@Composable
fun ExerciseItem(
    exercise: Exercise,
    onDelete: (Exercise) -> Unit,
    onEdit: (Exercise) -> Unit
) {
    // stan dialogu edycji
    var showDialog by remember { mutableStateOf(false) }
    // stan pól edycji
    var editName by remember { mutableStateOf(exercise.name) }
    var editDesc by remember { mutableStateOf(exercise.description) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Tekst po lewej
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .align(Alignment.TopStart)
                    .fillMaxWidth(0.8f)  // zostaw miejsce na ikonki
            ) {
                Text(text = exercise.name, style = MaterialTheme.typography.titleMedium)
                if (exercise.description.isNotBlank()) {
                    Spacer(Modifier.height(4.dp))
                    Text(text = exercise.description, style = MaterialTheme.typography.bodyMedium)
                }
            }

            // Ikonki w prawym górnym rogu
            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                IconButton(
                    onClick = { showDialog = true },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Create,
                        contentDescription = "Edytuj ćwiczenie",
                        modifier = Modifier.size(20.dp)
                    )
                }
                IconButton(
                    onClick = { onDelete(exercise) },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Usuń ćwiczenie",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }

    // Dialog edycji
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Edytuj ćwiczenie") },
            text = {
                Column {
                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("Nazwa") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = editDesc,
                        onValueChange = { editDesc = it },
                        label = { Text("Opis") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    // gdy zatwierdzamy, przekażemy nową instancję Exercise
                    onEdit(exercise.copy(name = editName, description = editDesc))
                    showDialog = false
                }) {
                    Text("Zapisz")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Anuluj")
                }
            }
        )
    }
}
