package com.yusufvural.kaloritakip.ui

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.yusufvural.kaloritakip.model.MealType
import com.yusufvural.kaloritakip.ui.analysis.AnalysisScreen
import com.yusufvural.kaloritakip.ui.auth.login.LoginScreen
import com.yusufvural.kaloritakip.ui.auth.register.RegisterScreen
import com.yusufvural.kaloritakip.ui.dashboard.DashboardScreen
import com.yusufvural.kaloritakip.ui.dashboard.DashboardViewModel
import com.yusufvural.kaloritakip.ui.dashboard.MealDetailScreen
import com.yusufvural.kaloritakip.ui.library.LibraryScreen
import com.yusufvural.kaloritakip.ui.profile.ProfileScreen
import com.yusufvural.kaloritakip.ui.profile.TargetCalculatorScreen
import com.yusufvural.kaloritakip.ui.stats.StatsScreen
import com.yusufvural.kaloritakip.ui.stats.StatsViewModel
import kotlinx.coroutines.delay
import com.google.firebase.auth.FirebaseAuth 

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "splash",
        enterTransition = { fadeIn(animationSpec = tween(300)) },
        exitTransition = { fadeOut(animationSpec = tween(300)) }
    ) {
        composable("splash") {
            com.yusufvural.kaloritakip.ui.splash.SplashScreen(
                onNavigateToDashboard = {
                    val currentUser = FirebaseAuth.getInstance().currentUser
                    val targetRoute = if (currentUser != null) "dashboard" else "auth"
                    navController.navigate(targetRoute) {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        navigation(startDestination = "login", route = "auth") {
            composable("login") {
                LoginScreen(
                    onNavigateToDashboard = {
                        navController.navigate("dashboard") {
                            popUpTo("auth") { inclusive = true }
                        }
                    },
                    onNavigateToRegister = {
                        navController.navigate("register")
                    }
                )
            }
            composable("register") {
                RegisterScreen(
                    onNavigateToDashboard = {
                        navController.navigate("dashboard") {
                            popUpTo("auth") { inclusive = true }
                        }
                    },
                    onNavigateToLogin = {
                        navController.popBackStack()
                    }
                )
            }
        }

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
            LibraryScreen(
                onNavigateToDetail = { name, calories, prot, fat, carb ->
                    val route = "food_detail/$name/$calories/$prot/$fat/$carb" + 
                                if (mealTypeStr != null) "?mealType=$mealTypeStr" else ""
                    navController.navigate(route)
                }
            ) 
        }

        composable("profile") { 
            ProfileScreen(onNavigate = { route -> navController.navigate(route) }) 
        }
        composable("target_calculator") {
            TargetCalculatorScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(
            route = "meal_detail/{mealType}",
            arguments = listOf(navArgument("mealType") { type = NavType.StringType })
        ) { backStackEntry ->
            val mealTypeStr = backStackEntry.arguments?.getString("mealType") ?: "LUNCH"
            val mealType = MealType.valueOf(mealTypeStr)
            val dashboardViewModel: DashboardViewModel = hiltViewModel()
            
            MealDetailScreen(
                mealType = mealType,
                viewModel = dashboardViewModel,
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
            val initialMealType = mealTypeStr?.let { MealType.valueOf(it) }

            val dashboardViewModel: DashboardViewModel = hiltViewModel()

            com.yusufvural.kaloritakip.ui.addfood.FoodDetailScreen(
                foodName = foodName,
                baseCalories = baseCalories,
                protein = protein,
                fat = fat,
                carbs = carbs,
                initialMealType = initialMealType,
                onNavigateBack = { navController.popBackStack() },
                onAddComplete = { name, cal, p, f, c, selectedMealType ->
                    dashboardViewModel.addFood(name, cal, p, c, f, selectedMealType)
                    navController.popBackStack("dashboard", inclusive = false)
                }
            )
        }
    }
}
