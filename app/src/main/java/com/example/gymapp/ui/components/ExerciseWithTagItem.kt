// ui/components/ExerciseWithTagItem.kt
package com.example.gymapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gymapp.data.model.ExerciseWithTags
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseWithTagItem(
    ewt: ExerciseWithTags,
    onDelete: (exercise: com.example.gymapp.data.model.Exercise) -> Unit,
    onEdit: (exercise: com.example.gymapp.data.model.Exercise) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(Modifier.padding(12.dp)) {
            // góra karty: nazwa + opis + przycisk usuń
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(Modifier.weight(1f)) {
                    Text(ewt.exercise.name, style = MaterialTheme.typography.titleMedium)
                    if (ewt.exercise.description.isNotBlank()) {
                        Spacer(Modifier.height(4.dp))
                        Text(ewt.exercise.description, style = MaterialTheme.typography.bodyMedium)
                    }
                }
                IconButton(onClick = { onDelete(ewt.exercise) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Usuń")
                }
            }

            Spacer(Modifier.height(8.dp))

            // dolna część: tagi
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                ewt.tags.forEach { tag ->
                    AssistChip(
                        onClick = { /* możesz otworzyć dialog edycji */ },
                        label = { Text(tag.name) }
                    )
                }
            }
        }
    }
}
