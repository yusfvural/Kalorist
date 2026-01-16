package com.yusufvural.kaloritakip.ui

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.yusufvural.kaloritakip.ui.dashboard.DashboardScreen
import com.yusufvural.kaloritakip.ui.stats.StatsScreen
import com.yusufvural.kaloritakip.ui.addfood.AddFoodScreen
import com.yusufvural.kaloritakip.ui.library.LibraryScreen
import com.yusufvural.kaloritakip.ui.profile.ProfileScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "dashboard",
        enterTransition = { fadeIn(animationSpec = tween(300)) },
        exitTransition = { fadeOut(animationSpec = tween(300)) }
    ) {
        composable("dashboard") { 
            DashboardScreen(onNavigate = { route -> navController.navigate(route) }) 
        }
        composable("stats") { StatsScreen() }
        composable("add_food") { AddFoodScreen(onNavigate = { route -> navController.navigate(route) }) }
        composable("library") { LibraryScreen() }
        composable("profile") { 
            ProfileScreen(onNavigate = { route -> navController.navigate(route) }) 
        }
        composable("target_calculator") {
            com.yusufvural.kaloritakip.ui.profile.TargetCalculatorScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(
            route = "food_detail/{foodName}/{baseCalories}",
            arguments = listOf(
                androidx.navigation.navArgument("foodName") { type = androidx.navigation.NavType.StringType },
                androidx.navigation.navArgument("baseCalories") { type = androidx.navigation.NavType.IntType }
            )
        ) { backStackEntry ->
            val foodName = backStackEntry.arguments?.getString("foodName") ?: ""
            val baseCalories = backStackEntry.arguments?.getInt("baseCalories") ?: 0
            com.yusufvural.kaloritakip.ui.addfood.FoodDetailScreen(
                foodName = foodName,
                baseCalories = baseCalories,
                onNavigateBack = { navController.popBackStack() },
                onAddComplete = { 
                    navController.popBackStack("dashboard", inclusive = false)
                }
            )
        }
    }
}
