package com.example.cartzy.u_i.spalsh

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import com.example.cartzy.R
import com.example.cartzy.ui.theme.CartzyTheme
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CartzyTheme {
                SplashScreen { isLoggedIn ->
                    // TODO: Navigate to appropriate screen based on isLoggedIn
                    // Example: startActivity(Intent(this, HomeActivity::class.java)) ...
                }
            }
        }
    }
}

@Composable
fun SplashScreen(onSplashFinished: (Boolean) -> Unit) {
    val lavender = Color(0xFFB39DDB)  // 💜 Lavender
    val babyBlue = Color(0xFFB3E5FC)  // 🌸 Baby Blue

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.cart_loader))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 1,
        speed = 1f
    )

    LaunchedEffect(Unit) {
        delay(2800)
        val isLoggedIn = FirebaseAuth.getInstance().currentUser != null
        onSplashFinished(isLoggedIn)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(lavender, babyBlue)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        composition?.let {
            LottieAnimation(
                composition = it,
                progress = { progress },
                modifier = Modifier.size(400.dp)
            )
        }
    }
}
