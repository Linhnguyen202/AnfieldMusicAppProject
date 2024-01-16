package com.example.anfieldmusicapp.application

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.media.AudioAttributes
import android.os.Build
import com.google.firebase.auth.FirebaseAuth

class MyApplication : Application() {
    companion object {
        const val CHANNEL_MUSIC = "CHANNEL_MUSIC_PLAYER"
    }
    public lateinit var auth : FirebaseAuth
    override fun onCreate() {
        super.onCreate()
        createNotificationChanel()
        auth = FirebaseAuth.getInstance()
    }
    private fun createNotificationChanel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel(CHANNEL_MUSIC, "channel_name", NotificationManager.IMPORTANCE_DEFAULT)
            val atribute : AudioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)

        }
    }

}