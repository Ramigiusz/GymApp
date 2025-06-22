package com.example.gymapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gymapp.R
import com.example.gymapp.ui.components.VideoPlayer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoLibraryScreen(navController: NavController) {
    var selectedVideoUrl by remember { mutableStateOf<String?>(null) }

    // Definicja stałych ćwiczeń z lokalnymi plikami wideo
    val exercises = listOf(
        Exercise("Przysiad", "Podstawowe ćwiczenie na nogi", "android.resource://com.example.gymapp/" + R.raw.squat),
        Exercise("Pompka", "Ćwiczenie na klatkę i ramiona", "android.resource://com.example.gymapp/" + R.raw.push_up),
        Exercise("Podciąganie", "Ćwiczenie na plecy i ramiona", "android.resource://com.example.gymapp/" + R.raw.pull_up)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Biblioteka filmów") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Wróć")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(exercises) { exercise ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedVideoUrl = exercise.videoUrl },
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = exercise.name,
                                style = MaterialTheme.typography.titleMedium
                            )
                            if (!exercise.description.isNullOrBlank()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = exercise.description,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }

            selectedVideoUrl?.let { url ->
                Spacer(modifier = Modifier.height(16.dp))
                VideoPlayer(videoUrl = url)
                Button(
                    onClick = { selectedVideoUrl = null },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Zamknij wideo")
                }
            }
        }
    }
}

// Prosta klasa danych do reprezentacji ćwiczeń
data class Exercise(val name: String, val description: String, val videoUrl: String)