package com.example.newsapplication.Api

import com.google.gson.annotations.SerializedName

data class NewsApiJson(
    @SerializedName("news")
    val news: List<New>,
    @SerializedName("page")
    val page: Int,
    val status: String
)