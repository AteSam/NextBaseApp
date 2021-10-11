package com.example.nextbaseapp.api

import com.example.nextbaseapp.model.Journey_model
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("sampleData?page=0")
    //we will be making a network call using coroutines, hence the suspend keyword here

    suspend fun getResponse(): Response<List<Journey_model>>

}

