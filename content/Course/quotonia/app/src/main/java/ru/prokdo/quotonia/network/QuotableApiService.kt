package ru.prokdo.quotonia.network

import retrofit2.http.GET
import retrofit2.http.Query
import ru.prokdo.quotonia.models.QuoteResponse
import ru.prokdo.quotonia.models.Quote
import retrofit2.http.Path

interface QuotableApiService {
    @GET("quotes")
    suspend fun searchQuotesByAuthor(
        @Query("author") author: String,
        @Query("limit") limit: Int = 20
    ): QuoteResponse

    @GET("/search/quotes")
    suspend fun searchQuotesByQuery(
        @Query("query") query: String,
        @Query("limit") limit: Int = 20
    ): QuoteResponse

    @GET("quotes/random")
    suspend fun getRandomQuotes(
        @Query("limit") limit: Int = 20
    ): List<Quote>

    @GET("quotes/{id}")
    suspend fun getQuoteDetails(@Path("id") id: String): Quote
}
