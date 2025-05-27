package com.example.cartzy.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Repository {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://openlibrary.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(OpenLibraryApi::class.java)

    suspend fun fetchBestSellingBooks(): List<Book> {
        val bestSelling = api.searchBooksBySubject("best_selling", 20).docs
        val novels = if (bestSelling.size < 6) {
            api.searchBooksBySubject("novels", 20).docs
        } else emptyList()

        val combinedDocs = bestSelling + novels

        return combinedDocs.map { doc ->
            Book(
                title = doc.title ?: "No title",
                author = doc.author_name?.firstOrNull() ?: "Unknown author",
                coverUrl = doc.cover_i?.let { "https://covers.openlibrary.org/b/id/$it-M.jpg" }
            )
        }
    }
}

data class Book(
    val title: String,
    val author: String,
    val coverUrl: String?
)
