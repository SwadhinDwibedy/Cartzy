package com.example.cartzy.u_i.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.cartzy.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween

@Composable
fun HomeScreen(navController: NavController) {
    val uid = FirebaseAuth.getInstance().currentUser?.uid
    var firstName by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(uid) {
        if (uid != null) {
            try {
                val document = Firebase.firestore.collection("users").document(uid).get().await()
                val fullName = document.getString("fullName") ?: ""
                firstName = fullName.trim().split(" ").firstOrNull()?.takeIf { it.isNotBlank() } ?: "User"
            } catch (e: Exception) {
                firstName = "User"
            }
        } else {
            firstName = "User"
        }
    }

    val banners = listOf(
        R.drawable.banner1,
        R.drawable.banner2,
        R.drawable.banner3,
        R.drawable.banner4
    )

    val bannerListState = rememberLazyListState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colorStops = arrayOf(
                        0.00f to Color(0xFF9F79D1),
                        0.10f to Color(0xFFDACFF4),
                        0.50f to Color(0xFFEDE7FB),
                        0.85f to Color(0xFFF8F5FF),
                        1.00f to Color(0xFFFFFAFC)
                    )
                )
            )
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 25.dp)
        ) {
            item {
                Text(
                    text = "Hey ${firstName ?: "User"}, ready to explore?",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = 22.sp,
                        color = Color(0xFF2D1E5F)
                    )
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                var searchQuery by remember { mutableStateOf("") }

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = {
                        Text(
                            "Search books...",
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                    },
                    textStyle = LocalTextStyle.current.copy(
                        color = if (searchQuery.isNotEmpty()) Color(0xFF7B4ABD) else Color.Black
                    ),
                    modifier = Modifier
                        .fillMaxWidth(0.93f)
                        .height(52.dp),
                    singleLine = true,
                    shape = MaterialTheme.shapes.large,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White.copy(alpha = 0.95f),
                        focusedContainerColor = Color.White,
                        focusedBorderColor = Color(0xFF7B4ABD),
                        unfocusedBorderColor = Color(0xFF9F79D1)
                    ),
                    trailingIcon = {
                        IconButton(onClick = {
                            // TODO: Handle search button click here
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_search),
                                contentDescription = "Search",
                                tint = if (searchQuery.isNotEmpty()) Color(0xFF7B4ABD) else Color.Gray
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(4.dp))
            }

            // Animated Quote above banners
            item {
                AnimatedQuote()
            }

            item {
                // Banner Carousel
                val flingBehavior = rememberSnapFlingBehavior(lazyListState = bannerListState)

                LazyRow(
                    state = bannerListState,
                    flingBehavior = flingBehavior,
                    contentPadding = PaddingValues(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                ) {
                    itemsIndexed(banners) { index, bannerRes ->

                        val route = when (index) {
                            0 -> "best_selling"
                            1 -> "editors_choice"
                            2 -> "motivational"
                            3 -> "all_books"
                            else -> "home"
                        }

                        Card(
                            modifier = Modifier
                                .fillParentMaxWidth()
                                .padding(horizontal = 0.dp)
                                .height(190.dp)
                                .clickable {
                                    navController.navigate(route)
                                },
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(6.dp)
                        ) {
                            Image(
                                painter = painterResource(id = bannerRes),
                                contentDescription = "Banner ${index + 1}",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Page Indicator Dots
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    val selectedIndex = bannerListState.firstVisibleItemIndex
                    repeat(banners.size) { index ->
                        val isSelected = index == selectedIndex
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .height(8.dp)
                                .width(if (isSelected) 24.dp else 12.dp)
                                .background(
                                    color = if (isSelected) Color(0xFF7B4ABD) else Color.LightGray,
                                    shape = MaterialTheme.shapes.small
                                )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(244.dp))
            }
        }
    }
}

@Composable
fun AnimatedQuote() {
    var visible by remember { mutableStateOf(false) }

    val alphaAnim by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 800)
    )
    val scaleAnim by animateFloatAsState(
        targetValue = if (visible) 1f else 0.9f,
        animationSpec = tween(durationMillis = 800)
    )

    LaunchedEffect(Unit) {
        visible = true
    }
    Text(
        text = "Within each book, a universe awaits.",
        style = MaterialTheme.typography.bodyLarge.copy(
            fontSize = 18.sp,
            color = Color(0xFF4A3C8C),
            fontStyle = FontStyle.Italic
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .graphicsLayer {
                alpha = alphaAnim
                scaleX = scaleAnim
                scaleY = scaleAnim
            },
        textAlign = TextAlign.Center
    )
}
