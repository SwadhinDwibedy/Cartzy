package com.example.cartzy.u_i.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.example.cartzy.R

@Composable
fun ProfileScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF1ECFF)) // Light lavender-purple background
            .padding(horizontal = 16.dp, vertical = 32.dp)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Swadhin Dwibedy", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text("swadhin@example.com", fontSize = 14.sp, color = Color.Gray)
            }
            IconButton(onClick = { navController.navigate("edit_profile") }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_edit),
                    contentDescription = "Edit Profile",
                    tint = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Profile Stats
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ProfileStatCard(R.drawable.ic_orders, 12, "Orders", Modifier.weight(1f))
            ProfileStatCard(R.drawable.ic_save, 7, "Saved", Modifier.weight(1f))
            ProfileStatCard(R.drawable.ic_wishlist, 15, "Wishlist", Modifier.weight(1f))
            ProfileStatCard(R.drawable.ic_address, 3, "Address", Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Shortcuts Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier.fillMaxWidth(),
            content = {
                items(shortcutItems) { item ->
                    Column(
                        modifier = Modifier
                            .clickable { navController.navigate(item.route) }
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painter = painterResource(id = item.iconRes),
                            contentDescription = item.label,
                            tint = Color(0xFF6A4DAE),
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(item.label, fontSize = 12.sp)
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Settings List
        settingsOptions.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate(item.route) }
                    .padding(vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = item.iconRes),
                    contentDescription = item.label,
                    tint = Color(0xFF6A4DAE),
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(item.label, fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Logout Button
        Button(
            onClick = {
                // TODO: FirebaseAuth.getInstance().signOut()
                navController.navigate("login") {
                    popUpTo("profile") { inclusive = true }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Log Out", color = Color.White)
        }
    }
}

@Composable
fun ProfileStatCard(iconRes: Int, count: Int, label: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(110.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(12.dp)
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = label,
                modifier = Modifier.size(28.dp),
                tint = Color(0xFF6A4DAE)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "$count",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF6A4DAE)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                fontSize = 13.sp,
                color = Color.Gray
            )
        }
    }
}

data class ShortcutItem(val label: String, val iconRes: Int, val route: String)

val shortcutItems = listOf(
    ShortcutItem("Orders", R.drawable.ic_orders, "orders"),
    ShortcutItem("Saved", R.drawable.ic_save, "saved"),
    ShortcutItem("Wishlist", R.drawable.ic_wishlist, "wishlist"),
    ShortcutItem("Address", R.drawable.ic_address, "address")
)

data class SettingItem(val label: String, val iconRes: Int, val route: String)

val settingsOptions = listOf(
    SettingItem("Settings", R.drawable.ic_settings, "settings"),
    SettingItem("Dark Mode", R.drawable.ic_dark_mode, "settings"),
    SettingItem("Help Center", R.drawable.ic_help, "help_center"),
    SettingItem("Privacy Policy", R.drawable.ic_privacy, "privacy_policy"),
    SettingItem("About Us", R.drawable.ic_about_us, "about_us")
)
