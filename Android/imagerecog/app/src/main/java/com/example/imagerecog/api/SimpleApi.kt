package com.example.retrofitdemo.api

import com.example.imagerecog.model.Post
import retrofit2.Response
import retrofit2.http.*

interface SimpleApi {

    @GET("posts/2")
    suspend fun getPost(): Post



}