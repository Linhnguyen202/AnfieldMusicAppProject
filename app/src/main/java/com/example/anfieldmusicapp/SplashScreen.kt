package com.example.anfieldmusicapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.anfieldmusicapp.databinding.ActivitySplashScreenBinding
import com.example.anfieldmusicapp.share.sharePreferenceUtils

class SplashScreen : AppCompatActivity() {
    lateinit var binding : ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Handler(Looper.myLooper()!!).postDelayed({
            if(sharePreferenceUtils.isSharedPreferencesExist(this,"USER","USER_VALUE")){
                startActivity(Intent(this,MainActivity::class.java))
            }
            else{
                startActivity(Intent(this,LoginScreen::class.java))
            }

        },2000)
    }
}