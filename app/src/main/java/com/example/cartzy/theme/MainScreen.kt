package com.example.cartzy.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument

import com.example.cartzy.R
import com.example.cartzy.u_i.cart.CartScreen
import com.example.cartzy.u_i.home.AllBooksScreen
import com.example.cartzy.u_i.home.BestSellingScreen
import com.example.cartzy.u_i.home.BookDetailScreen
import com.example.cartzy.u_i.home.EditorsChoiceScreen
import com.example.cartzy.u_i.home.HomeScreen
import com.example.cartzy.u_i.home.MotivationalScreen
import com.example.cartzy.u_i.product.CategoriesScreen
import com.example.cartzy.u_i.profile.ProfileScreen
import com.example.cartzy.ui.theme.PurpleLavender

@Composable
fun MainScreen(rootNavController: NavHostController) {
    val navController = rememberNavController()
    val bottomNavController = rememberNavController()

    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Categories,
        BottomNavItem.Cart,
        BottomNavItem.Profile,
    )

    val splashGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFFF8F5FF),
            Color(0xFFFFFAFC)
        ),
        start = Offset(0f, 0f),
        end = Offset(1000f, 1000f)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(splashGradient)
    ) {
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.weight(1f)
        ) {
            composable(BottomNavItem.Home.route) { HomeScreen(navController) }
            composable(BottomNavItem.Categories.route) { CategoriesScreen() }
            composable(BottomNavItem.Cart.route) { CartScreen() }
            composable(BottomNavItem.Profile.route) { ProfileScreen() }

            // Banner destinations
            composable("best_selling") { BestSellingScreen(navController) }
            composable("editors_choice") { EditorsChoiceScreen(navController) }
            composable("motivational") { MotivationalScreen(navController) }
            composable("all_books") { AllBooksScreen(navController) }

            // 📘 Book Detail Screen with bookId argument
            composable(
                route = "book_detail/{workId}",
                arguments = listOf(navArgument("workId") { type = NavType.StringType })
            ) { backStackEntry ->
                val workId = backStackEntry.arguments?.getString("workId") ?: ""
                BookDetailScreen(workId = workId, navController = navController)
            }

        }

        BottomNavigationBar(
            items = items,
            currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route,
            onItemSelected = { route ->
                navController.navigate(route) {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
    }
}

@Composable
fun BottomNavigationBar(
    items: List<BottomNavItem>,
    currentRoute: String?,
    onItemSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(Color.White),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { item ->
            val selected = currentRoute == item.route
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onItemSelected(item.route) },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(id = item.icon),
                    contentDescription = item.title,
                    tint = if (selected) PurpleLavender else Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = item.title,
                    fontSize = 12.sp,
                    color = if (selected) PurpleLavender else Color.Gray
                )
            }
        }
    }
}

sealed class BottomNavItem(val route: String, val title: String, val icon: Int) {
    object Home : BottomNavItem("home", "Home", R.drawable.ic_home)
    object Categories : BottomNavItem("categories", "Categories", R.drawable.ic_categories)
    object Cart : BottomNavItem("cart", "Cart", R.drawable.ic_cart)
    object Profile : BottomNavItem("profile", "Profile", R.drawable.ic_profile)
}
