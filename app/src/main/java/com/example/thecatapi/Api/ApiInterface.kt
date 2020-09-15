package com.example.thecatapi.Api

import com.example.thecatapi.Api.Model.Cat
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiInterface {
    @GET("v1/images/search")
    fun getCatsList(
        @Header("x-api-key") header: String,
        @Query("limit") limit: Int
    ): Call<List<Cat>>
}