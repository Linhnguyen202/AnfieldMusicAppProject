package com.example.anfieldmusicapp

import com.example.anfieldmusicapp.model.Music

data class Playlist(
    val id : String,
    val name: String,
    val songs : List<Music>
)
