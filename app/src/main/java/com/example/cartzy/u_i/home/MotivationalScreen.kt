package com.example.cartzy.u_i.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.cartzy.R
import com.example.cartzy.common.Book
import com.example.cartzy.common.BookListItem
import com.example.cartzy.data.Repository3

@Composable
fun MotivationalScreen(navController: NavController) {
    var books by remember { mutableStateOf<List<Book>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

    LaunchedEffect(Unit) {
        try {
            books = Repository3.fetchMotivationalBooks()
            isLoading = false
        } catch (e: Exception) {
            errorMessage = e.localizedMessage ?: "Unknown error"
            isLoading = false
        }
    }

    when {
        isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        errorMessage != null -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Error: $errorMessage")
            }
        }
        else -> {
            val filteredBooks = books.filter {
                it.title.contains(searchQuery.text, ignoreCase = true) ||
                        it.author.contains(searchQuery.text, ignoreCase = true)
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 40.dp, start = 12.dp, end = 12.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = {
                        Text("Search books...", fontSize = 14.sp, color = Color.Gray)
                    },
                    textStyle = LocalTextStyle.current.copy(
                        color = if (searchQuery.text.isNotEmpty()) Color(0xFF7B4ABD) else Color.Black
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    singleLine = true,
                    shape = MaterialTheme.shapes.large,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White.copy(alpha = 0.95f),
                        focusedContainerColor = Color.White,
                        focusedBorderColor = Color(0xFF7B4ABD),
                        unfocusedBorderColor = Color(0xFF9F79D1)
                    ),
                    trailingIcon = {
                        IconButton(onClick = { /* Optionally trigger action */ }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_search),
                                contentDescription = "Search",
                                tint = if (searchQuery.text.isNotEmpty()) Color(0xFF7B4ABD) else Color.Gray
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),  // fills remaining space in column
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(filteredBooks) { book ->
                        BookListItem(book = book)
                    }
                }
            }
        }
    }
}
