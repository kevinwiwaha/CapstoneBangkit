package com.example.imagerecog.repository

import com.example.retrofitdemo.api.RetrofitInstance
import com.example.imagerecog.model.Post
import retrofit2.Response

class Repository {

    suspend fun getPost(): Post {
        return RetrofitInstance.api.getPost()
    }



}