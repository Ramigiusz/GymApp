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
import androidx.compose.runtime.*
import androidx.navigation.NavController
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    var notificationsEnabled by remember { mutableStateOf(true) }
    var darkThemeEnabled by remember { mutableStateOf(false) }

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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
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
            // Sekcja ogólne
            Text("Ogólne", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Powiadomienia", modifier = Modifier.weight(1f))
                Switch(
                    checked = notificationsEnabled,
                    onCheckedChange = { notificationsEnabled = it },
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            // Sekcja nawigacji
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
                onClick = { navController.navigate("videoLibrary") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(10),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                Text("Biblioteka filmów")
            }

            // Sekcja zarządzania danymi
            Text("Zarządzanie aplikacją", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = {
                    // Instrukcja dla użytkownika
                    println("Proszę kliknąć ikonę książki pod wiadomością, aby zapomnieć czat.")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(10),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                Text("Preferencje")
            }

            OutlinedButton(
                onClick = {
                    // Instrukcja dla użytkownika
                    println("Proszę przejść do 'Data Controls' w ustawieniach, aby zresetować pamięć.")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(10),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                Text("Resetuj pamięć")
            }

            // Dodatkowe opcje (zastępują "Opcje")
            OutlinedButton(
                onClick = { /* TODO: Zaimplementuj edytowanie profilu */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(10),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                Text("Edytuj profil")
            }

            OutlinedButton(
                onClick = { /* TODO: Zaimplementuj informacje o aplikacji */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(10),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                Text("Informacje o aplikacji")
            }

            // Wypełniacze usunięte, aby uniknąć nadmiaru pustych opcji
        }
    }
}

