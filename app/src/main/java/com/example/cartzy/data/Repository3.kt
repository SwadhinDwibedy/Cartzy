package com.example.cartzy.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

object Repository3 {
    private val client = OkHttpClient()

    suspend fun fetchMotivationalBooks(): List<Book> = withContext(Dispatchers.IO) {
        val url = "https://openlibrary.org/subjects/self_help.json"

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
            val coverUrl = if (coverId != 0) "https://covers.openlibrary.org/b/id/$coverId-M.jpg" else null

            Book(title = title, author = author, coverUrl = coverUrl)
        }
    }
}
