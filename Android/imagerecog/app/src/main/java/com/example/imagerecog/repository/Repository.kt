package com.example.imagerecog.repository

import com.example.imagerecog.api.RetrofitInstance
import com.example.imagerecog.model.Post

class Repository {
    suspend fun getPost(): Post {
        return RetrofitInstance.api.getPost()
    }
}