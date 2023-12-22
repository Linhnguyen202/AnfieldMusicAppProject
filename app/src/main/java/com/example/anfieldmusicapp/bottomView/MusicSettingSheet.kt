package com.example.anfieldmusicapp.bottomView

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.anfieldmusicapp.R
import com.example.anfieldmusicapp.databinding.FragmentMusicSettingSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class MusicSettingSheet : BottomSheetDialogFragment() {
    lateinit var binding : FragmentMusicSettingSheetBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMusicSettingSheetBinding.inflate(layoutInflater)
        return binding.root
    }


}