package com.example.gymapp.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.gymapp.viewmodel.RoutineViewModel                    // <-- dodaj ten import
import com.example.gymapp.ui.screens.RoutinesScreen
import com.example.gymapp.ui.screens.StartScreen
import com.example.gymapp.ui.screens.SettingsScreen
import com.example.gymapp.ui.screens.TrainingScreen
import com.example.gymapp.ui.screens.EditRoutineScreen

@Composable
fun NavGraph(navController: NavHostController) {
    // jawna deklaracja typu generycznego
    val routinesViewModel: RoutineViewModel = viewModel<RoutineViewModel>()

    NavHost(navController, startDestination = "start") {
        composable("start") {
            StartScreen(navController)
        }

        // tylko jedna trasa "routines", korzystająca z twojego VM
        composable("routines") {
            RoutinesScreen(navController, routinesViewModel)
        }

        composable("settings") {
            SettingsScreen(navController)
        }

        composable("training_screen/{routineId}") { backStackEntry ->
            val routineId = backStackEntry.arguments
                ?.getString("routineId")
                ?.toIntOrNull()
            TrainingScreen(navController, routineId)
        }

        composable("edit_routine_screen/{routineId}") { backStackEntry ->
            val routineId = backStackEntry.arguments
                ?.getString("routineId")
                ?.toIntOrNull()
            // tu podajesz ten sam VM
            EditRoutineScreen(navController, routineId, routinesViewModel)
        }
    }
}
