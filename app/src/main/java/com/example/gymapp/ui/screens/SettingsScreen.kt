// ui/screens/SettingsScreen.kt
package com.example.gymapp.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ustawienia") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Wróć")
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar {
                Text("Bottom Bar", modifier = Modifier.padding(16.dp))
            }
        }
    ) { paddingValues ->
        Text(
            text = "Tutaj będą ustawienia",
            modifier = Modifier.padding(paddingValues)
        )
    }
}

