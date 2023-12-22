package com.example.anfieldmusicapp.viewModel.MusicViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.anfieldmusicapp.application.MyApplication
import com.example.anfieldmusicapp.repositiory.MusicRepository

class MusicViewModelFactory  (val app : MyApplication, val musicRepository: MusicRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MusicViewModel(app,musicRepository) as T
    }
}