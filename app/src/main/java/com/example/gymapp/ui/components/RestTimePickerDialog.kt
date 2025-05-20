package com.example.gymapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RestTimePickerDialog(
    initialMinutes: Int,
    initialSeconds: Int,
    onDismiss: () -> Unit,
    onConfirm: (minutes: Int, seconds: Int) -> Unit
) {
    var selectedMinutes by remember { mutableStateOf(initialMinutes) }
    var selectedSeconds by remember { mutableStateOf(initialSeconds) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Ustaw przerwę") },
        text = {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                LazyColumn(
                    modifier = Modifier.height(150.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    items(10) { minute ->
                        TextButton(onClick = { selectedMinutes = minute }) {
                            Text(
                                text = "$minute min",
                                style = if (selectedMinutes == minute)
                                    MaterialTheme.typography.titleMedium
                                else
                                    MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                LazyColumn(
                    modifier = Modifier.height(150.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    items((0..50 step 10).toList()) { second ->
                        TextButton(onClick = { selectedSeconds = second }) {
                            Text(
                                text = "$second sek",
                                style = if (selectedSeconds == second)
                                    MaterialTheme.typography.titleMedium
                                else
                                    MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirm(selectedMinutes, selectedSeconds)
            }) {
                Text("Zatwierdź")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Anuluj")
            }
        }
    )
}
