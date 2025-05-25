// ui/components/ExerciseItem.kt
package com.example.gymapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Dialog
import com.example.gymapp.data.model.Exercise

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseItem(
    exercise: Exercise,
    onDelete: (Exercise) -> Unit,
    onEdit: (Exercise) -> Unit
) {
    // stan dialogu edycji
    var showEditDialog by remember { mutableStateOf(false) }
    // stan dialogu obrazu
    var showImageDialog by remember { mutableStateOf(false) }

    // pola do edycji tekstu
    var editName by remember { mutableStateOf(exercise.name) }
    var editDesc by remember { mutableStateOf(exercise.description) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
        ) {
            // Tekst po lewej
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .align(Alignment.TopStart)
                    .fillMaxWidth(0.8f)
            ) {
                Text(text = exercise.name, style = MaterialTheme.typography.titleMedium)
                if (exercise.description.isNotBlank()) {
                    Spacer(Modifier.height(4.dp))
                    Text(text = exercise.description, style = MaterialTheme.typography.bodyMedium)
                }
            }

            // Przyciski edytuj / usuń
            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                IconButton(
                    onClick = { showEditDialog = true },
                    modifier = Modifier
                        .size(32.dp)
                ) {
                    Icon(Icons.Default.Create, contentDescription = "Edytuj", modifier = Modifier.size(20.dp))
                }
                IconButton(
                    onClick = { onDelete(exercise) },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Usuń", modifier = Modifier.size(20.dp))
                }
            }

            // Miniaturka obrazka w prawym dolnym rogu
            exercise.drawableRes?.let { resId ->
                Image(
                    painter = painterResource(id = resId),
                    contentDescription = null,
                    modifier = Modifier
                        .size(70.dp)
                        .align(Alignment.BottomEnd)
                        .clip(RoundedCornerShape(6.dp))
                        .clickable { showImageDialog = true },
//                    contentScale = ContentScale.Crop
                )
            }
        }
    }

    // ——— Dialog edycji ćwiczenia ———
    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
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
                    onEdit(exercise.copy(name = editName, description = editDesc))
                    showEditDialog = false
                }) {
                    Text("Zapisz")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Anuluj")
                }
            }
        )
    }

    // ——— Pełnoekranowy dialog z obrazkiem ———
    if (showImageDialog) {
        Dialog(onDismissRequest = { showImageDialog = false }) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.8f))
                    .clickable { showImageDialog = false },
                contentAlignment = Alignment.Center
            ) {
                exercise.drawableRes?.let { resId ->
                    Image(
                        painter = painterResource(id = resId),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }
    }
}
