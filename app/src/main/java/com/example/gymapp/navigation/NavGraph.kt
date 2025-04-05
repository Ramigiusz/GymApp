package com.example.gymapp.navigation

// navigation/NavGraph.kt

import androidx.compose.runtime.Composable
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.gymapp.ui.screens.RoutinesScreen
import com.example.gymapp.ui.screens.SettingsScreen
import com.example.gymapp.ui.screens.StartScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = "start") {
        composable("start") { StartScreen(navController) }
        composable("settings") { SettingsScreen(navController) }
        composable("routines") { RoutinesScreen(navController) }
    }
}
