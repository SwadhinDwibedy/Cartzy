package com.example.cartzy.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cartzy.common.BookDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

interface BookRepository {
    suspend fun fetchBookDetail(workId: String): BookDetail?
}

object RepositoryBookDetail : BookRepository {
    private val client = OkHttpClient()

    override suspend fun fetchBookDetail(workId: String): BookDetail? = withContext(Dispatchers.IO) {
        try {
            val workUrl = "https://openlibrary.org/works/$workId.json"
            val workRequest = Request.Builder().url(workUrl).build()
            val workResponse = client.newCall(workRequest).execute()

            if (!workResponse.isSuccessful) return@withContext null

            val workBody = workResponse.body?.string() ?: return@withContext null
            val workJson = JSONObject(workBody)

            val title = workJson.optString("title", "No Title")

            val description = when (val desc = workJson.opt("description")) {
                is JSONObject -> desc.optString("value", null)
                is String -> desc
                else -> null
            }

            val authorsJsonArray = workJson.optJSONArray("authors")
            val authors = mutableListOf<String>()
            var mainAuthor = "Unknown Author"

            if (authorsJsonArray != null) {
                for (i in 0 until authorsJsonArray.length()) {
                    val authorObj = authorsJsonArray.getJSONObject(i)
                    val authorKey = authorObj.getJSONObject("author").optString("key") ?: continue
                    val authorUrl = "https://openlibrary.org$authorKey.json"

                    val authorRequest = Request.Builder().url(authorUrl).build()
                    val authorResponse = client.newCall(authorRequest).execute()

                    if (authorResponse.isSuccessful) {
                        authorResponse.body?.string()?.let { authorBody ->
                            val authorJson = JSONObject(authorBody)
                            val authorName = authorJson.optString("name", "Unknown Author")
                            authors.add(authorName)
                        }
                    }
                }
                if (authors.isNotEmpty()) mainAuthor = authors[0]
            }

            val coversArray = workJson.optJSONArray("covers")
            val coverId = coversArray?.optInt(0)
            val coverUrl = coverId?.let { "https://covers.openlibrary.org/b/id/$it-L.jpg" } ?: ""

            val rating = generateRandomRating()
            val discountPercent = listOf(10, 15, 20, 25, 30).random()
            val mrp = (499..999).random()
            val price = mrp - (mrp * discountPercent / 100)

            return@withContext BookDetail(
                id = workId,
                title = title,
                author = mainAuthor,
                rating = rating,
                discountPercent = discountPercent,
                price = price,
                mrp = mrp,
                coverUrl = coverUrl,
                description = description,
                authors = authors,
                coverId = coverId,
                workId = workId
            )
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext null
        }
    }

    private fun generateRandomRating(): Float {
        val base = (3..5).random()
        val decimal = listOf(0f, 0.5f).random()
        return base + decimal
    }
}


class BookDetailViewModel(
    private val repository: BookRepository = RepositoryBookDetail
) : ViewModel() {

    var bookDetail by mutableStateOf<BookDetail?>(null)
        private set

    fun loadBookDetail(workId: String) {
        viewModelScope.launch {
            bookDetail = repository.fetchBookDetail(workId)
        }
    }
}

