package com.example.anfieldmusicapp.repositiory

import android.content.Context
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

class MusicRepository (val context : Context) {
    suspend fun getMusic(type: String) = RetrofitInstance.newInstance(context).getMusic(type)

    suspend fun getSearching(query: String) =RetrofitInstance.newInstance(context).searchMusic(query)

    suspend fun getMusicProfile(id : String) = RetrofitInstance.newInstance(context).getMusicProfile(id)

}