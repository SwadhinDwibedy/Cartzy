package com.example.cartzy.u_i.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.cartzy.data.BookDetailViewModel
import kotlin.random.Random

@Composable
fun BookDetailScreen(
    workId: String,
    navController: NavController,
    viewModel: BookDetailViewModel = viewModel()
) {
    // Trigger loading when bookId changes
    LaunchedEffect(workId) {
        viewModel.loadBookDetail(workId)
    }

    // Observe book detail state directly (no need for derivedStateOf)
    val book = viewModel.bookDetail

    if (book == null) {
        // Show loading indicator while fetching
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    // Once book is loaded, render details
    val deliveryFee = if (book.rating >= 5f) {
        "₹${Random.nextInt(20, 100)}"
    } else {
        "Free"
    }

    Scaffold(
        bottomBar = {
            BottomAppBar(
                containerColor = Color.White,
                modifier = Modifier
                    .height(56.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { /* TODO: Add to cart logic */ },
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A3C8C))
                    ) {
                        Text("Add to Cart", color = Color.White)
                    }

                    Button(
                        onClick = { /* TODO: Buy now logic */ },
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C56F9))
                    ) {
                        Text("Buy This Now", color = Color.White)
                    }
                }
            }
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                // Book cover image full width with rounded bottom corners
                Image(
                    painter = rememberAsyncImagePainter(book.coverUrl),
                    contentDescription = book.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp)
                        .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))

                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Text(
                        text = book.title,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Author: ${book.author}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 16.sp,
                            color = Color.DarkGray
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Rating stars + label row
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        repeat(5) { index ->
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = "Star",
                                tint = if (index < book.rating.toInt()) Color(0xFFFFC107) else Color(0xFFE0E0E0),
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = when (book.rating.toInt()) {
                                5 -> "Best"
                                4 -> "Very Good"
                                3 -> "Good"
                                2 -> "Fair"
                                else -> "Average"
                            },
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp,
                                color = Color(0xFF4A3C8C)
                            )
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = "Arrow Down",
                            tint = Color.Gray,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Discount: -${book.discountPercent}%",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color(0xFFD32F2F)
                        )
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Delivery: $deliveryFee",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp,
                            color = Color(0xFF388E3C)
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Price: ₹${book.price}  (MRP: ₹${book.mrp}, ${book.discountPercent}% OFF)",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = Color(0xFF4A3C8C)
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Bank Offers:",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp,
                            color = Color.Black
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Sample bank offers list
                    Column {
                        Text("- 10% off with HDFC Credit Cards", style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("- EMI options available", style = MaterialTheme.typography.bodyMedium)
                    }

                    Spacer(modifier = Modifier.height(60.dp)) // Extra bottom padding for bottom bar
                }
            }
        }
    }
}
