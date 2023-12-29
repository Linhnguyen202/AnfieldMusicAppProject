package com.example.anfieldmusicapp.bottomView

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.anfieldmusicapp.MainActivity
import com.example.anfieldmusicapp.R
import com.example.anfieldmusicapp.application.MyApplication
import com.example.anfieldmusicapp.broadcast.MyReceiver
import com.example.anfieldmusicapp.databinding.FragmentMusicSettingSheetBinding
import com.example.anfieldmusicapp.model.Music
import com.example.anfieldmusicapp.repositiory.MusicRepository
import com.example.anfieldmusicapp.utils.MusicStatus
import com.example.anfieldmusicapp.utils.Resource
import com.example.anfieldmusicapp.viewModel.MusicViewModel.MusicViewModel
import com.example.anfieldmusicapp.viewModel.MusicViewModel.MusicViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class MusicSettingSheet : BottomSheetDialogFragment() {
    lateinit var binding : FragmentMusicSettingSheetBinding
    lateinit var viewModel: MusicViewModel
    lateinit var repository : MusicRepository
    lateinit var viewModelFactory : MusicViewModelFactory
    val musicId by lazy {
        (activity as MainActivity).mediaService.player!!.currentMediaItem!!.mediaId
    }
    var musicData : Music? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMusicSettingSheetBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repository = MusicRepository()
        viewModelFactory = MusicViewModelFactory(MyApplication(),repository)
        viewModel =  ViewModelProvider(this,viewModelFactory)[MusicViewModel::class.java]
        getData()
        addEvents()
        observerData()



    }

    private fun observerData() {
        viewModel.musicProfile.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success -> {
                    it.data?.let { MusicResponse ->
                        musicData = MusicResponse.data
                    }
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(),it.message, Toast.LENGTH_LONG).show()
                }
                is Resource.Loading -> {

                }

            }
        }
    }

    private fun addEvents() {
        binding.addPlaylistBtn.setOnClickListener {
            val playlistSheet = PlaylistSheet.newInstance(musicData!!)
            playlistSheet.show(parentFragmentManager,"playListSheet")
            this.dismiss()
        }
        binding.watchMvBtn.setOnClickListener {
            val link = musicData!!.link_mv
            val intent : Intent = Intent(Intent.ACTION_VIEW,Uri.parse("https://www.youtube.com/watch?v=$link"))
            startActivity(intent)
            val intent1 = Intent(requireContext(), MyReceiver::class.java)
            intent1.setAction(MusicStatus.PAUSE_ACTION.toString())
            requireContext().sendBroadcast(intent1)
        }
    }

    private fun getData() {
        viewModel.getMusicProfile(musicId)
    }

}