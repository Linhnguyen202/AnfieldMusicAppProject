package com.example.anfieldmusicapp.model

data class MusicResponse(
    val pagination : Pagination,
    val data : ArrayList<Music>
)
