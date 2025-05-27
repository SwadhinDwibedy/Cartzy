package com.example.cartzy.u_i.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.cartzy.data.Book
import com.example.cartzy.data.Repository

@Composable
fun BestSellingScreen(navController: NavController) {
    var books by remember { mutableStateOf<List<Book>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            books = Repository.fetchBestSellingBooks()
            isLoading = false
        } catch (e: Exception) {
            errorMessage = e.localizedMessage ?: "Unknown error"
            isLoading = false
        }
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (errorMessage != null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Error: $errorMessage")
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            items(books) { book ->
                BookListItem(book)
                Divider()
            }
        }
    }
}

@Composable
fun BookListItem(book: Book) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        if (book.coverUrl != null) {
            Image(
                painter = rememberAsyncImagePainter(book.coverUrl),
                contentDescription = book.title,
                modifier = Modifier.size(80.dp)
            )
        } else {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text("No Image", style = MaterialTheme.typography.bodySmall)
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(book.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(book.author, style = MaterialTheme.typography.bodySmall)
        }
    }
}
