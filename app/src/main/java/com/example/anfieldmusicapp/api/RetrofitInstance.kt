package com.example.anfieldmusicapp.api

import android.content.Context
import androidx.core.os.bundleOf
import com.example.anfieldmusicapp.application.MyApplication
import com.example.anfieldmusicapp.bottomView.PlaylistForm
import com.example.anfieldmusicapp.model.Music
import com.example.anfieldmusicapp.utils.Constants.Companion.BASE_URL
import com.example.anfieldmusicapp.utils.NetworkConnectionInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    companion object {
        fun newInstance(context: Context): MusicApi {
           val retrofit by lazy {
                val logging = HttpLoggingInterceptor()
               val networkConnectionInterceptor: NetworkConnectionInterceptor = NetworkConnectionInterceptor(context);
                logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                val client = OkHttpClient.Builder()
                    .addInterceptor(networkConnectionInterceptor)
                    .addInterceptor(logging)
                    .build()
                Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()
            }
            val api by lazy {
                retrofit.create(MusicApi::class.java)
            }
            return api;
        }

    }
}