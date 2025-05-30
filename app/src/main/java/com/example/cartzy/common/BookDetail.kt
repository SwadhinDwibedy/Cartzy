package com.example.cartzy.common

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BookDetail(
    val id: String,
    val title: String,
    val author: String,
    val rating: Float,
    val discountPercent: Int,
    val price: Int,
    val mrp: Int,
    val coverUrl: String,
    val description: String?,
    val authors: List<String>,
    val coverId: Int?,
    val workId: String,
    val publishYear: Int? = null,      // Year of publication if available
) : Parcelable

// Helper to generate a float rating between 3.0 and 5.0
fun generateRandomRating(): Float {
    val base = (3..5).random()
    val decimal = listOf(0f, 0.5f).random()
    return base + decimal
}

// Helper to generate a mock price
fun generateRandomPrice(): Int = (199..799).random()

// Helper to simulate discounts
fun generateRandomDiscount(): Int = listOf(10, 15, 20, 25, 30, 35, 40, 50, 60).random()

// Used for extracting author keys from JSON
data class AuthorReference(val author: KeyWrapper)
data class KeyWrapper(val key: String)
