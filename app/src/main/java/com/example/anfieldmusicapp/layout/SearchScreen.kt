package com.example.anfieldmusicapp.layout

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.example.anfieldmusicapp.MainActivity
import com.example.anfieldmusicapp.R
import com.example.anfieldmusicapp.adapter.HomeMusicAdapter
import com.example.anfieldmusicapp.adapter.SearchAdapter
import com.example.anfieldmusicapp.application.MyApplication
import com.example.anfieldmusicapp.databinding.FragmentSearchScreenBinding
import com.example.anfieldmusicapp.model.Music
import com.example.anfieldmusicapp.repositiory.MusicRepository
import com.example.anfieldmusicapp.utils.Resource
import com.example.anfieldmusicapp.viewModel.MusicViewModel.MusicViewModel
import com.example.anfieldmusicapp.viewModel.MusicViewModel.MusicViewModelFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchScreen : Fragment() {
    lateinit var binding : FragmentSearchScreenBinding
    lateinit var viewModelFactory : MusicViewModelFactory

    lateinit var searchAdapter: SearchAdapter
    lateinit var viewModel: MusicViewModel
    lateinit var repository : MusicRepository
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchScreenBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUp()
        getData()
        observerData()
        addEvents()
    }


    private fun setUp() {
        repository = MusicRepository()
        viewModelFactory = MusicViewModelFactory(MyApplication(),repository)
        viewModel =  ViewModelProvider(this,viewModelFactory)[MusicViewModel::class.java]
        searchAdapter = SearchAdapter(onClickMuic)
        binding.searchList.apply {
            adapter = this@SearchScreen.searchAdapter
        }
    }

    private fun addEvents() {
        var job : Job? = null
       binding.searchEditText.addTextChangedListener {
           job?.cancel()
           job = MainScope().launch {
               delay(500L)
               it?.let {
                   if(it.toString().isNotEmpty()){
                       viewModel.getSearching(it.toString())
                   }

               }
           }
       }

    }
    private fun observerData() {
        viewModel.searchMusic.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success -> {
                    it.data?.let { MusicResponse ->
                        searchAdapter.differ.submitList(MusicResponse.data.toList())
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

    private fun getData() {

    }

    private val onClickMuic : (Int,MutableList<Music>) -> Unit = { pos, data ->
        (activity as MainActivity).startMusicFromService(pos, data)

    }



}