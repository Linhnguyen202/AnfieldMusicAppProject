package com.example.anfieldmusicapp.layout

import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.anfieldmusicapp.MainActivity
import com.example.anfieldmusicapp.R
import com.example.anfieldmusicapp.adapter.HomeMusicAdapter
import com.example.anfieldmusicapp.application.MyApplication
import com.example.anfieldmusicapp.databinding.FragmentHomeScreenBinding
import com.example.anfieldmusicapp.model.Music
import com.example.anfieldmusicapp.repositiory.MusicRepository
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
        getData()
        observerData()
    }




    private fun setUp() {
        repository = MusicRepository()
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
    private fun getData() {
        viewModel.getTrendingMusic()
        viewModel.getTopFavor()
    }
    private fun observerData() {
        viewModel.trendingMusic.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success -> {
                    it.data?.let { MusicResponse ->
                        trendingAdapter.differ.submitList(MusicResponse.data.toList())
                       Log.d("music",MusicResponse.data.toString())
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
        viewModel.favoriteMusic.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success -> {
                    it.data?.let { MusicResponse ->
                        topFavorAdapter.differ.submitList(MusicResponse.data.toList())
                        Log.d("music",MusicResponse.data.toString())
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
    private val onClickMuic : (Int,MutableList<Music>) -> Unit = { pos, data ->
        (activity as MainActivity).startMusicFromService(pos, data)

    }

}