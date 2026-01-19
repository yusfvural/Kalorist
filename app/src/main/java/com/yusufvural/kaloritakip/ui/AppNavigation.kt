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
import com.yusufvural.kaloritakip.ui.stats.StatsViewModel
import com.yusufvural.kaloritakip.ui.analysis.AnalysisScreen
import com.yusufvural.kaloritakip.ui.profile.ProfileScreen
import androidx.hilt.navigation.compose.hiltViewModel
import com.yusufvural.kaloritakip.ui.dashboard.DashboardViewModel
import com.yusufvural.kaloritakip.model.MealType
import com.yusufvural.kaloritakip.ui.library.LibraryScreen
import androidx.navigation.NavType
import androidx.navigation.navArgument

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
        composable("stats") { 
            val statsViewModel: StatsViewModel = hiltViewModel()
            StatsScreen(viewModel = statsViewModel) 
        }
        composable("analysis") { AnalysisScreen() }
        composable(
            route = "add_food?mealType={mealType}",
            arguments = listOf(navArgument("mealType") { nullable = true })
        ) { backStackEntry ->
            val mealTypeStr = backStackEntry.arguments?.getString("mealType")
            LibraryScreen(onNavigateToDetail = { name, calories, prot, fat, carb ->
                val route = "food_detail/$name/$calories/$prot/$fat/$carb" + 
                            if (mealTypeStr != null) "?mealType=$mealTypeStr" else ""
                navController.navigate(route)
            }) 
        }
        composable("profile") { 
            ProfileScreen(onNavigate = { route -> navController.navigate(route) }) 
        }
        composable("target_calculator") {
            com.yusufvural.kaloritakip.ui.profile.TargetCalculatorScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(
            route = "food_detail/{foodName}/{baseCalories}/{protein}/{fat}/{carbs}?mealType={mealType}",
            arguments = listOf(
                navArgument("foodName") { type = NavType.StringType },
                navArgument("baseCalories") { type = NavType.IntType },
                navArgument("protein") { type = NavType.FloatType },
                navArgument("fat") { type = NavType.FloatType },
                navArgument("carbs") { type = NavType.FloatType },
                navArgument("mealType") { nullable = true }
            )
        ) { backStackEntry ->
            val foodName = backStackEntry.arguments?.getString("foodName") ?: ""
            val baseCalories = backStackEntry.arguments?.getInt("baseCalories") ?: 0
            val protein = backStackEntry.arguments?.getFloat("protein")?.toDouble() ?: 0.0
            val fat = backStackEntry.arguments?.getFloat("fat")?.toDouble() ?: 0.0
            val carbs = backStackEntry.arguments?.getFloat("carbs")?.toDouble() ?: 0.0
            val mealTypeStr = backStackEntry.arguments?.getString("mealType")
            
            val dashboardViewModel: DashboardViewModel = hiltViewModel()

            com.yusufvural.kaloritakip.ui.addfood.FoodDetailScreen(
                foodName = foodName,
                baseCalories = baseCalories,
                protein = protein,
                fat = fat,
                carbs = carbs,
                onNavigateBack = { navController.popBackStack() },
                onAddComplete = { name, cal, p, f, c ->
                    val mealType = mealTypeStr?.let { MealType.valueOf(it) } ?: MealType.LUNCH
                    dashboardViewModel.addFood(name, cal, p, c, f, mealType)
                    navController.popBackStack("dashboard", inclusive = false)
                }
            )
        }
    }
}
