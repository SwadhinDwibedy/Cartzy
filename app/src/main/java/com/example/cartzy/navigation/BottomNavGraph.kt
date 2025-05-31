package com.example.cartzy.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cartzy.theme.BottomNavItem
import com.example.cartzy.u_i.cart.CartScreen
import com.example.cartzy.u_i.product.CategoriesScreen
import com.example.cartzy.u_i.home.HomeScreen
import com.example.cartzy.u_i.profile.ProfileScreen

@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = BottomNavItem.Home.route) {
        composable(BottomNavItem.Home.route) { HomeScreen(navController) }
        composable(BottomNavItem.Categories.route) { CategoriesScreen() }
        composable(BottomNavItem.Cart.route) { CartScreen() }
        composable(BottomNavItem.Profile.route) { ProfileScreen(navController) }
    }
}

