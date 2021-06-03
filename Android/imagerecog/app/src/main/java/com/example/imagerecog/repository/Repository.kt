package com.example.imagerecog.repository

import com.example.retrofitdemo.api.RetrofitInstance
import com.example.imagerecog.model.Post
import retrofit2.Response

class Repository {

    suspend fun getPost(): Post {
        return RetrofitInstance.api.getPost()
    }

    suspend fun pushPost(useId:Int,id:Int,title:String,body:String): Post{
        return RetrofitInstance.api.pushPost(useId,id,title,body)
    }


}