package com.example.anfieldmusicapp.utils

import android.content.Context
import android.net.ConnectivityManager
import com.example.anfieldmusicapp.application.MyApplication
import com.example.anfieldmusicapp.exception.NoInternetException
import okhttp3.Interceptor
import okhttp3.Response

class NetworkConnectionInterceptor () : Interceptor {
    private val applicationContext = MyApplication().applicationContext
    override fun intercept(chain: Interceptor.Chain): Response {
        if(!isInternetAvailable()){
            throw NoInternetException("No internet connection")
        }
        return chain.proceed(chain.request())
    }
    private fun isInternetAvailable(): Boolean{
        val connectivityManager = applicationContext.applicationContext.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        connectivityManager.activeNetworkInfo.also {
            return it != null && it.isConnected
        }
    }
}