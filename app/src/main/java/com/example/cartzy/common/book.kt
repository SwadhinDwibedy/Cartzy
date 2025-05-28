package com.example.cartzy.common

data class Book(
    val title: String,
    val author: String,
    val coverUrl: String?,     // URL for the book cover image
    val rating: Float = (3..5).random() + listOf(0f, 0.5f).random(), // Random rating for look
    val price: Int = (199..799).random(), // Random price to simulate actual products
    val description: String = "", // Short description or summary
    val workId: String = "",   // Optional: useful for navigating to book details
    val publishYear: Int? = null // Optional: year of publication
)