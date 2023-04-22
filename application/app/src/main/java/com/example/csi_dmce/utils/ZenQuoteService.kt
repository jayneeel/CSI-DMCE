package com.example.csi_dmce.utils

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

data class ZenQuote(
    // The quote text
    val q: String,
    // Quote's author
    val a: String,
    // HTML formatted quote
    val h: String
)

interface ZenQuoteService {
    @GET("api/today")
    fun getQuote(): Call<List<ZenQuote>>
}

class ZenQuoteHelpers {
}