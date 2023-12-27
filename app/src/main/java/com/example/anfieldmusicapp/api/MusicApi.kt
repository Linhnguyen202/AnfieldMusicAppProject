package com.example.anfieldmusicapp.api

import com.example.anfieldmusicapp.model.MusicProfileResponse
import com.example.anfieldmusicapp.model.MusicResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MusicApi {
    @GET("music/{type}")
    suspend fun getMusic(
        @Path("type") type : String
    ) : Response<MusicResponse>

    @GET("search")
    suspend fun searchMusic(
        @Query("query") query : String
    ) : Response<MusicResponse>

    @GET("music/get-by-id")
    suspend fun getMusicProfile(
        @Query("_id") id : String,
    ) : Response<MusicProfileResponse>
}