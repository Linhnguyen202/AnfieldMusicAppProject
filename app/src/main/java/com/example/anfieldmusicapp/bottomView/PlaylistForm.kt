package com.example.anfieldmusicapp.bottomView

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.anfieldmusicapp.MainActivity
import com.example.anfieldmusicapp.R
import com.example.anfieldmusicapp.application.MyApplication
import com.example.anfieldmusicapp.databinding.FragmentPlaylistFormBinding
import com.example.anfieldmusicapp.databinding.FragmentPlaylistSheetBinding
import com.example.anfieldmusicapp.model.Music
import com.example.anfieldmusicapp.model.Playlist
import com.example.anfieldmusicapp.repositiory.MusicRepository
import com.example.anfieldmusicapp.share.sharePreferenceUtils
import com.example.anfieldmusicapp.utils.Resource
import com.example.anfieldmusicapp.viewModel.MusicViewModel.MusicViewModel
import com.example.anfieldmusicapp.viewModel.MusicViewModel.MusicViewModelFactory
import com.google.android.gms.common.util.SharedPreferencesUtils
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class PlaylistForm : BottomSheetDialogFragment() {
   lateinit var binding: FragmentPlaylistFormBinding
    lateinit var db : FirebaseDatabase
    lateinit var reference : DatabaseReference

    lateinit var viewModel: MusicViewModel
    lateinit var repository : MusicRepository
    lateinit var viewModelFactory : MusicViewModelFactory
    var musicData : Music? = null
    var musicArray : ArrayList<Music> = ArrayList()
    companion object {
        const val ARG_DATA = "key"
        fun newInstance(data: String): PlaylistForm {
            val fragment = PlaylistForm()
            val args = Bundle()
            args.putString(ARG_DATA, data)
            fragment.arguments = args
            return fragment
        }
    }
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
        repository = MusicRepository()
        viewModelFactory = MusicViewModelFactory(MyApplication(),repository)
        viewModel =  ViewModelProvider(this,viewModelFactory)[MusicViewModel::class.java]
        getData()
        observerData()
        addEvents()

    }

    private fun observerData() {
        viewModel.musicProfile.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success -> {
                    it.data?.let { MusicResponse ->
                        musicData = MusicResponse.data
                        musicArray.add(musicData!!)
                    }
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(),it.message, Toast.LENGTH_LONG).show()
                }
                is Resource.Loading -> {
                    Toast.makeText(requireContext(),"Loading", Toast.LENGTH_LONG).show()
                }

            }
        }
    }

    private fun getData() {
        val data = (activity as MainActivity).mediaService.player!!.currentMediaItem!!.mediaId
        viewModel.getMusicProfile(data)
    }

    private fun addEvents() {
        val data = (activity as MainActivity).mediaService.player!!.currentMediaItem!!.mediaId

        val user = sharePreferenceUtils.getUser(requireContext()).id

        binding.submitBtn.setOnClickListener{
            val playlist = binding.playListEdt.text.toString()
            val playlistData = Playlist(playlist,musicArray.toList())
            reference.child(user.toString()).push().setValue(playlistData).addOnCompleteListener {
                if(it.isSuccessful){
                    Toast.makeText(requireContext(),"Successfully", Toast.LENGTH_LONG).show()
                }
                else{

                }
            }
        }

    }


}