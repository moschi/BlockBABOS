package com.example.blockbabos.rest

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RestController{

    var API_KEY = "f6789f8a7c4ea3cc8c3b4fc4b1e75973"
    val TOP_RATED = "/movie/top_rated"
    val LANGUAGE = "en_US"

    object ServiceBuilder {
        private const val BASE_URL = "https://api.themoviedb.org/3"
        private val client = OkHttpClient.Builder().build()
        private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        fun<T> buildService(service: Class<T>): T{
            return retrofit.create(service)
        }
    }

    fun test(){

    }


}