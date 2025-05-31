package com.example.cartzy.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavController
import androidx.navigation.compose.composable
import com.example.cartzy.u_i.profile.*

fun NavGraphBuilder.profileNavGraph(navController: NavController) {
    composable("profile") { ProfileScreen(navController) }
//    composable("orders") { OrdersScreen(navController) }
//    composable("saved") { SavedScreen(navController) }
//    composable("wishlist") { WishlistScreen(navController) }
//    composable("address") { AddressScreen(navController) }
//    composable("settings") { SettingsScreen(navController) }
//    composable("help_center") { HelpScreen(navController) }
//    composable("privacy_policy") { PrivacyPolicyScreen(navController) }
//    composable("about_us") { AboutUsScreen(navController) }
//    composable("edit_profile") { EditProfileScreen(navController) }
}
