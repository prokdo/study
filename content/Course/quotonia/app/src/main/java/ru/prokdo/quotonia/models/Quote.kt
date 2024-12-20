package ru.prokdo.quotonia.models

data class Quote(
    val _id: String,
    val content: String,
    val author: String,
    val tags: List<String>
)

data class QuoteResponse(
    val count: Int,
    val totalCount: Int,
    val page: Int,
    val totalPages: Int,
    val results: List<Quote>
)
