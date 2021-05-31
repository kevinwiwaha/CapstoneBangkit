package com.example.imagerecog

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.imagerecog.model.Post
import com.example.imagerecog.repository.Repository

class MainViewModel (private val repository: Repository): ViewModel(){

    val myResponse: MutableLiveData<Post> = MutableLiveData()

    fun getPost(){
        
    }

}