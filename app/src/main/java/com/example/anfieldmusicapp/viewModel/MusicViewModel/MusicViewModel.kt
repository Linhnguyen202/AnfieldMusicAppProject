package com.example.anfieldmusicapp.viewModel.MusicViewModel

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.anfieldmusicapp.application.MyApplication
import com.example.anfieldmusicapp.model.MusicProfileResponse
import com.example.anfieldmusicapp.model.MusicResponse
import com.example.anfieldmusicapp.repositiory.MusicRepository
import com.example.anfieldmusicapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class MusicViewModel (val app : MyApplication, val musicRepository: MusicRepository) : ViewModel() {
    val trendingMusic : MutableLiveData<Resource<MusicResponse>> = MutableLiveData()
    val favoriteMusic : MutableLiveData<Resource<MusicResponse>> = MutableLiveData()
    val searchMusic : MutableLiveData<Resource<MusicResponse>> = MutableLiveData()

    val musicProfile : MutableLiveData<Resource<MusicProfileResponse>> = MutableLiveData()
    public fun getTrendingMusic() =viewModelScope.launch(Dispatchers.IO) {
        try{
            trendingMusic.postValue(Resource.Loading())
            val response = musicRepository.getMusic("trending")
            trendingMusic.postValue(handleMusicResponse(response))
        }
        catch (e : IOException){
            trendingMusic.postValue(Resource.Error(e.message.toString()))

        }

    }
    public fun getTopFavor() =viewModelScope.launch(Dispatchers.IO) {
        try{
            favoriteMusic.postValue(Resource.Loading())
            val response = musicRepository.getMusic("new-music")
            favoriteMusic.postValue(handleMusicResponse(response))
        }
        catch (e : IOException){
            favoriteMusic.postValue(Resource.Error(e.message.toString()))

        }

    }

    public fun getSearching(query: String) =viewModelScope.launch(Dispatchers.IO) {
        try{
            searchMusic.postValue(Resource.Loading())
            val response = musicRepository.getSearching(query)
            searchMusic.postValue(handleMusicResponse(response))
        }
        catch (e : IOException){
            searchMusic.postValue(Resource.Error(e.message.toString()))

        }

    }

    public fun getMusicProfile(id : String) = viewModelScope.launch(Dispatchers.IO) {
       try{
            musicProfile.postValue(Resource.Loading())
            val response = musicRepository.getMusicProfile(id)
            musicProfile.postValue(handleMusicResponse(response))
        }
        catch (e : IOException){
            musicProfile.postValue(Resource.Error(e.message.toString()))

        }
    }
    private fun<T>handleMusicResponse(response : Response<T>) : Resource<T> {
        if(response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }
}