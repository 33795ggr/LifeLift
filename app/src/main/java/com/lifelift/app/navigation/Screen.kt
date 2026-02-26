package com.lifelift.app.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Workouts : Screen("workouts")
    object Vitamins : Screen("vitamins")
    object Analytics : Screen("analytics")
    object Settings : Screen("settings")
    object CreateWorkout : Screen("create_workout")
    object EditWorkout : Screen("edit_workout/{workoutId}") {
        fun createRoute(workoutId: Long) = "edit_workout/$workoutId"
    }
    object Cardio : Screen("cardio")
}
