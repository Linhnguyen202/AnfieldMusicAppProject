package com.example.anfieldmusicapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.anfieldmusicapp.databinding.ActivityWebScreenBinding

class WebScreen : AppCompatActivity() {
    lateinit var binding : ActivityWebScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent = intent
        try {
            binding.webView.loadUrl(intent.data.toString())
        }
        catch (e : Exception){
            e.printStackTrace()
        }
    }
}