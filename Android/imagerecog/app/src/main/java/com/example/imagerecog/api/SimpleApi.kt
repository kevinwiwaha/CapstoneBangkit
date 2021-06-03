package com.example.retrofitdemo.api

import com.example.imagerecog.model.Post
import retrofit2.Response
import retrofit2.http.*

interface SimpleApi {

    @GET("posts/2")
    suspend fun getPost(): Post

    @FormUrlEncoded
    @POST("predict")
    suspend fun pushPost(
        @Field("userId") userId:Int,
        @Field("id") id:Int,
        @Field("title") title: String,
        @Field("body") body: String,

        ): Post
}