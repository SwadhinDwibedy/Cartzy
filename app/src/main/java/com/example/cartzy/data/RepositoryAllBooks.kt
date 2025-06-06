package com.example.cartzy.data

import com.example.cartzy.common.Book
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

object RepositoryAllBooks {
    private val client = OkHttpClient()

    suspend fun fetchAllBooks(): List<Book> = withContext(Dispatchers.IO) {
        val url = "https://openlibrary.org/subjects/fiction.json?limit=50"

        val request = Request.Builder()
            .url(url)
            .build()

        val response = client.newCall(request).execute()
        if (!response.isSuccessful) {
            throw Exception("HTTP error code: ${response.code}")
        }

        val responseBody = response.body?.string() ?: throw Exception("Empty response")
        val jsonObject = JSONObject(responseBody)
        val worksArray = jsonObject.getJSONArray("works")

        List(worksArray.length()) { i ->
            val item = worksArray.getJSONObject(i)
            val title = item.optString("title", "No Title")

            val authorsArray = item.optJSONArray("authors")
            val author = if (authorsArray != null && authorsArray.length() > 0) {
                authorsArray.getJSONObject(0).optString("name", "Unknown Author")
            } else {
                "Unknown Author"
            }

            val coverId = item.optInt("cover_id", 0)
            val coverUrl = if (coverId != 0)
                "https://covers.openlibrary.org/b/id/$coverId-M.jpg"
            else null

            val rating = (3..5).random() + listOf(0f, 0.5f).random()
            val price = (199..799).random()

            Book(
                title = title,
                author = author,
                coverUrl = coverUrl,
                rating = rating,
                price = price
            )
        }
    }
}
