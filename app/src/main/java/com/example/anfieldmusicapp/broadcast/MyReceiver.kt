package com.example.anfieldmusicapp.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class MyReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?)  {
        val action = intent!!.action
        val intent = Intent("music")
        intent.putExtra("action_music",action.toString())
        context!!.sendBroadcast(intent)
    }
}