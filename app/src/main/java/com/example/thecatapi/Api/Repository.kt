package com.example.thecatapi.Api

import com.example.thecatapi.Api.Model.Cat
import retrofit2.Call

interface Repository {
    fun getCatList(): Call<List<Cat>>
}