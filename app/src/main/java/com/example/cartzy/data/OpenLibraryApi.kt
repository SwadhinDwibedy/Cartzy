package com.example.cartzy.data

import retrofit2.http.GET
import retrofit2.http.Query

interface OpenLibraryApi {

    @GET("search.json")
    suspend fun searchBooksBySubject(
        @Query("subject") subject: String,
        @Query("limit") limit: Int = 20
    ): SearchResponse
}

data class SearchResponse(
    val docs: List<BookDoc>
)

data class BookDoc(
    val title: String?,
    val author_name: List<String>?,
    val cover_i: Int? // cover id for URL construction
)

