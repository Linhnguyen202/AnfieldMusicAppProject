package com.example.anfieldmusicapp.layout

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.anfieldmusicapp.MainActivity
import com.example.anfieldmusicapp.adapter.HomeMusicAdapter
import com.example.anfieldmusicapp.application.MyApplication
import com.example.anfieldmusicapp.databinding.FragmentHomeScreenBinding
import com.example.anfieldmusicapp.model.Music
import com.example.anfieldmusicapp.repositiory.MusicRepository
import com.example.anfieldmusicapp.utils.NetworkConnection
import com.example.anfieldmusicapp.utils.Resource
import com.example.anfieldmusicapp.viewModel.MusicViewModel.MusicViewModel
import com.example.anfieldmusicapp.viewModel.MusicViewModel.MusicViewModelFactory

class HomeScreen : Fragment() {
    lateinit var binding : FragmentHomeScreenBinding
    lateinit var viewModelFactory : MusicViewModelFactory

    lateinit var trendingAdapter: HomeMusicAdapter
    lateinit var topFavorAdapter: HomeMusicAdapter
    lateinit var viewModel: MusicViewModel
    lateinit var repository : MusicRepository
    val musicListBig : MutableList<Music> = mutableListOf()

    private val networkConnection: NetworkConnection by lazy {
        NetworkConnection(requireContext())
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeScreenBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUp()
        observerData()
    }

    private fun setUp() {
        repository = MusicRepository(requireContext())
        viewModelFactory = MusicViewModelFactory(MyApplication(),repository)
        viewModel =  ViewModelProvider(this,viewModelFactory)[MusicViewModel::class.java]
        trendingAdapter = HomeMusicAdapter(onClickMuic)
        topFavorAdapter = HomeMusicAdapter(onClickMuic)
        binding.trendingList.apply {
            adapter = this@HomeScreen.trendingAdapter
        }

        binding.topFavorList.apply {
            adapter = this@HomeScreen.topFavorAdapter
        }
    }
    private fun addMusicBig() {
        binding.songTitle.text = musicListBig[0].name_music
        binding.artistTitle.text = musicListBig[0].name_singer
        Glide.with(this).load(musicListBig[0].image_music).into(binding.imageSong)
        binding.playBtn.setOnClickListener {
            onClickMuic(0,musicListBig)
        }
        binding.songCard.visibility = View.VISIBLE
    }
    private fun getData() {
        viewModel.getTrendingMusic()
        viewModel.getTopFavor()
    }
    private fun observerData() {
        networkConnection.observe(viewLifecycleOwner) { isConnected ->
            if (isConnected) {
                if(topFavorAdapter.differ.currentList.size <= 0 || trendingAdapter.differ.currentList.size <= 0){
                    getData()
                }
            }
        }
        viewModel.trendingMusic.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success -> {
                    it.data?.let { MusicResponse ->
                        trendingAdapter.differ.submitList(MusicResponse.data.toList())
                        musicListBig.addAll(MusicResponse.data.toList())
                        addMusicBig()
                    }
                }
                is Resource.Error -> {
                }
                is Resource.Loading -> {

                }

            }
        }
        viewModel.favoriteMusic.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success -> {
                    it.data?.let { MusicResponse ->
                        topFavorAdapter.differ.submitList(MusicResponse.data.toList())
                    }
                }
                is Resource.Error -> {
                }
                is Resource.Loading -> {

                }

            }
        }
    }



    private val onClickMuic : (Int,MutableList<Music>) -> Unit = { pos, data ->
        (activity as MainActivity).startMusicFromService(pos, data)

    }

}