package com.example.thecatapi.Api

import com.example.thecatapi.Api.Model.Cat
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteRepository : Repository {

    init {
        createApi()
    }

    companion object {
        private var repo: RemoteRepository? = null

        fun getInstance(): RemoteRepository {
            if (repo == null) {
                repo = RemoteRepository()
            }
            return repo!!
        }
    }

    private lateinit var api: ApiInterface
    private val token = "c26dfb95-d5ac-424f-bdd8-a303af965953"
    private val limit = 100

    override fun getCatList(): Call<List<Cat>> = api.getCatsList(
        token,
        limit
    )

    private fun createApi() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().apply {
                addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
            }
                .build())
            .build()

        api = retrofit.create(ApiInterface::class.java)
    }
}