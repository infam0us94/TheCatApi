package com.exemple.thecatapi.Api

import com.exemple.thecatapi.Api.Model.Cat
import retrofit2.Call

interface Repository {
    fun getCatList(): Call<List<Cat>>
}