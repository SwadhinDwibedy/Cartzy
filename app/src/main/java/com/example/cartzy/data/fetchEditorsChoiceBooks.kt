package com.example.cartzy.data

import com.example.cartzy.common.Book
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

object Repository1 {
    private val client = OkHttpClient()

    suspend fun fetchEditorsChoiceBooks(): List<Book> = withContext(Dispatchers.IO) {
        val url = "https://openlibrary.org/subjects/fantasy.json"

        val request = Request.Builder()
            .url(url)
            .build()

        val response = client.newCall(request).execute()
        if (!response.isSuccessful) throw Exception("HTTP error ${response.code}")

        val responseBody = response.body?.string() ?: throw Exception("Empty response")

        val jsonObject = JSONObject(responseBody)

        if (!jsonObject.has("works")) throw Exception("No works found in response")

        val worksArray = jsonObject.getJSONArray("works")
        if (worksArray.length() == 0) throw Exception("Works array is empty")

        List(worksArray.length()) { i ->
            val item = worksArray.getJSONObject(i)
            val title = item.getString("title")
            val authorArray = item.optJSONArray("authors")
            val author = authorArray?.optJSONObject(0)?.optString("name") ?: "Unknown"
            val coverId = item.optInt("cover_id", 0)
            val coverUrl = if (coverId != 0) "https://covers.openlibrary.org/b/id/$coverId-M.jpg" else null

            Book(title = title, author = author, coverUrl = coverUrl)
        }
    }
}
