package com.example.anfieldmusicapp.repositiory

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.anfieldmusicapp.api.MusicApi
import com.example.anfieldmusicapp.api.RetrofitInstance
import com.example.anfieldmusicapp.application.MyApplication
import com.example.anfieldmusicapp.model.MusicResponse
import com.example.anfieldmusicapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class MusicRepository () {
    suspend fun getMusic(type: String) = RetrofitInstance.api.getMusic(type)

    suspend fun getSearching(query: String) =RetrofitInstance.api.searchMusic(query)

    suspend fun getMusicProfile(id : String) = RetrofitInstance.api.getMusicProfile(id)

}