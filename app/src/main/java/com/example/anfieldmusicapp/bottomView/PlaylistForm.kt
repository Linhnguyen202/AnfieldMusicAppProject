package com.example.anfieldmusicapp.bottomView

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.anfieldmusicapp.R
import com.example.anfieldmusicapp.databinding.FragmentPlaylistFormBinding
import com.example.anfieldmusicapp.databinding.FragmentPlaylistSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class PlaylistForm : BottomSheetDialogFragment() {
   lateinit var binding: FragmentPlaylistFormBinding
    lateinit var db : FirebaseDatabase
    lateinit var reference : DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPlaylistFormBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = FirebaseDatabase.getInstance()
        reference = db.getReference("Playlist")

    }


}