package com.example.gymapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gymapp.data.model.Exercise

@Composable
fun ExerciseItem(
    exercise: Exercise,
    onDelete: (Exercise) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = exercise.name, style = MaterialTheme.typography.titleMedium)
                if (exercise.description.isNotBlank()) {
                    Text(text = exercise.description, style = MaterialTheme.typography.bodyMedium)
                }
            }
            IconButton(onClick = { onDelete(exercise) }) {
                Icon(Icons.Default.Delete, contentDescription = "Usuń ćwiczenie")
            }
        }
    }
}
