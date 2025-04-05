package com.example.gymapp.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartScreen(navController: NavController) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Colossus") }) }
    ) { padding ->
        Button(
            onClick = { navController.navigate("settings") },
            modifier = Modifier.padding(padding)
        ) {
            Text("Przejdź do Ustawień")
        }
    }
}