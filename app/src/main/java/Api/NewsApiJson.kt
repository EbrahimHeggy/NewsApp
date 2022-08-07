package com.example.newsapplication.Api

data class NewsApiJson(
    val news: List<New>,
    val page: Int,
    val status: String
)