package com.example.anfieldmusicapp.application

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.media.AudioAttributes
import android.os.Build

class MyApplication : Application() {
    companion object {
        const val CHANNEL_MUSIC = "CHANNEL_MUSIC_ID"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChanel()
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