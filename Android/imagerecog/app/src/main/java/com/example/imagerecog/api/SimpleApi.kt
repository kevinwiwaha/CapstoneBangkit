package com.example.imagerecog.api

import com.example.imagerecog.model.Post
import retrofit2.http.GET


interface SimpleApi {
    @GET("/posts/1")
    suspend fun getPost(): Post
}