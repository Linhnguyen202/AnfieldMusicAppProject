package com.example.anfieldmusicapp.bottomView

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.anfieldmusicapp.R
import com.example.anfieldmusicapp.databinding.FragmentMusicSettingSheetBinding
import com.example.anfieldmusicapp.databinding.FragmentPlaylistSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PlaylistSheet : BottomSheetDialogFragment() {
    lateinit var binding : FragmentPlaylistSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPlaylistSheetBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addPlaylistBtn.setOnClickListener{
            PlaylistForm().show(parentFragmentManager,"ADD_FORM")
            this.dismiss()
        }
    }


}