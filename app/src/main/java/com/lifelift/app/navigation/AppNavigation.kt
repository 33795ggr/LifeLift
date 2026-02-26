package com.lifelift.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.lifelift.app.features.analytics.AnalyticsScreen
import com.lifelift.app.features.home.HomeScreen
import com.lifelift.app.features.settings.SettingsScreen
import com.lifelift.app.features.vitamins.VitaminsScreen
import com.lifelift.app.features.workouts.WorkoutsScreen

@Composable
fun AppNavigation(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToWorkouts = {
                    navController.navigate(Screen.Workouts.route)
                },
                onNavigateToCardio = {
                    navController.navigate(Screen.Cardio.route)
                },
                onNavigateToVitamins = {
                    navController.navigate(Screen.Vitamins.route)
                },
                onNavigateToAnalytics = {
                    navController.navigate(Screen.Analytics.route)
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }
        
        composable(Screen.Workouts.route) {
            WorkoutsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToCreateWorkout = {
                    navController.navigate(Screen.CreateWorkout.route)
                },
                onNavigateToEditWorkout = { workoutId ->
                    navController.navigate(Screen.EditWorkout.createRoute(workoutId))
                }
            )
        }
        
        composable(Screen.Vitamins.route) {
            VitaminsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.Analytics.route) {
            AnalyticsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.Settings.route) {
            SettingsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.CreateWorkout.route) {
            com.lifelift.app.features.workouts.create.CreateWorkoutScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.Cardio.route) {
            com.lifelift.app.features.cardio.CardioScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(
            route = Screen.EditWorkout.route,
            arguments = listOf(navArgument("workoutId") { type = NavType.LongType })
        ) { backStackEntry ->
             val workoutId = backStackEntry.arguments?.getLong("workoutId") ?: return@composable
             com.lifelift.app.features.workouts.edit.EditWorkoutScreen(
                 workoutId = workoutId,
                 onNavigateBack = { navController.popBackStack() }
             )
        }
    }
}
