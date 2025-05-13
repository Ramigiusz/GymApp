// ui/screens/SettingsScreen.kt
package com.example.gymapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically) {
                    // Rutyny
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(25))
                            .clickable { navController.navigate("routines") },
                        contentAlignment = Alignment.Center
                    ) {
                        BottomNavItem(
                            icon = Icons.Default.List,
                            label = "Rutyny",
                            isSelected = false
                        )
                    }

                    // Start
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(25))
                            .clickable { navController.navigate("start") },
                        contentAlignment = Alignment.Center
                    ) {
                        BottomNavItem(
                            icon = Icons.Default.Home,
                            label = "Start",
                            isSelected = false
                        )
                    }

                    // Ustawienia
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(25))
                            .clickable { navController.navigate("settings") },
                        contentAlignment = Alignment.Center
                    ) {
                        BottomNavItem(
                            icon = Icons.Default.Settings,
                            label = "Ustawienia",
                            isSelected = true
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())   // umożliwia przewijanie
                .padding(paddingValues)
                .padding(8.dp),
            verticalArrangement = Arrangement.Top
        ) {
            OutlinedButton(
                onClick = { navController.navigate("exercises") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(10),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                Text("Zarządzaj ćwiczeniami")
            }
            OutlinedButton(
                onClick = { /* nic nie rób */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(10),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                Text("Opcja 2")
            }
            OutlinedButton(
                onClick = { /* nic nie rób */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(10),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                Text("Opcja 3")
            }
            OutlinedButton(
                onClick = { /* nic nie rób */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(10),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                Text("Opcja 4")
            }
            OutlinedButton(
                onClick = { /* nic nie rób */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(10),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                Text("Opcja 5")
            }
            OutlinedButton(
                onClick = { /* nic nie rób */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(10),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                Text("Opcja 6")
            }
            OutlinedButton(
                onClick = { /* nic nie rób */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(10),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                Text("Opcja 7")
            }
            OutlinedButton(
                onClick = { /* nic nie rób */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(10),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                Text("Opcja 8")
            }
            OutlinedButton(
                onClick = { /* nic nie rób */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(10),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                Text("Opcja 9")
            }
            OutlinedButton(
                onClick = { /* nic nie rób */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(10),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                Text("Opcja 10")
            }
            OutlinedButton(
                onClick = { /* nic nie rób */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(10),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                Text("Opcja 11")
            }

        }
    }
}
