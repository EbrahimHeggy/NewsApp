package com.example.newsapplication

import com.example.newsapplication.Api.NewsApiJson
import retrofit2.http.GET

interface APIRequest {
//    https://api.currentsapi.services/v1/search?keywords=Amazon&language=en&apiKey=26aLaWOYuxlGsFSWkOgDrUFMgHrMKnGASYN0Nb73u4vfAoAe
    @GET("/v1/search?keywords=Amazon&language=en&apiKey=26aLaWOYuxlGsFSWkOgDrUFMgHrMKnGASYN0Nb73u4vfAoAe")
    suspend fun getNews():NewsApiJson //// return news
}