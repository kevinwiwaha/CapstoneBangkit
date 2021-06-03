package com.example.imagerecog

import  androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imagerecog.model.Post
import com.example.imagerecog.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(private val repository: Repository): ViewModel() {

    var myResponse: MutableLiveData<Post> = MutableLiveData()

    fun pushPost(useId:Int,id:Int,title:String,body:String){
        viewModelScope.launch {
            val response =  repository.pushPost(useId,id,title,body)
            myResponse.value = response

        }
    }
    fun getPost(){
        viewModelScope.launch {
            val response = repository.getPost()
            myResponse.value = response
        }
    }


}