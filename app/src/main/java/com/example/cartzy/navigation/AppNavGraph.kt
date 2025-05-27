package com.example.cartzy.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cartzy.u_i.auth.LoginScreen
import com.example.cartzy.u_i.auth.SignupScreen
import com.example.cartzy.theme.MainScreen
import com.example.cartzy.u_i.home.AllBooksScreen
import com.example.cartzy.u_i.home.BestSellingScreen
import com.example.cartzy.u_i.home.EditorsChoiceScreen
import com.example.cartzy.u_i.home.HomeScreen
import com.example.cartzy.u_i.home.MotivationalScreen
import com.example.cartzy.u_i.spalsh.SplashScreen



@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        // Splash Screen
        composable(Screen.Splash.route) {
            SplashScreen { isLoggedIn ->
                navController.navigate(
                    if (isLoggedIn) Screen.Main.route else Screen.Login.route
                ) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            }
        }

        // Login Screen
        composable(Screen.Login.route) {
            LoginScreen(navController)
        }

        // Signup Screen
        composable(Screen.Signup.route) {
            SignupScreen(navController)
        }

        // Main screen with bottom nav
        composable(Screen.Main.route) {
            MainScreen(navController)
        }

        composable("home") {
            HomeScreen(navController)
        }

        // Full-screen destinations from banners
        composable("best_selling") { BestSellingScreen(navController) }
        composable("editors_choice") { EditorsChoiceScreen(navController) }
        composable("motivational") { MotivationalScreen(navController) }
        composable("all_books") { AllBooksScreen(navController) }
    }
}




// Sealed class for route management
sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Signup : Screen("signup")
     object Home : Screen("home")
     object Main : Screen("main")
    // object OTP : Screen("otp")
}