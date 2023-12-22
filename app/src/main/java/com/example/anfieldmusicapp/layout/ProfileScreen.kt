package com.example.anfieldmusicapp.layout

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.anfieldmusicapp.MainActivity
import com.example.anfieldmusicapp.R
import com.example.anfieldmusicapp.databinding.FragmentProfileScreenBinding
import com.example.anfieldmusicapp.service.MediaService
import com.example.anfieldmusicapp.share.sharePreferenceUtils


class ProfileScreen : Fragment() {
    lateinit var binding : FragmentProfileScreenBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileScreenBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addData()
        binding.logoutBtn.setOnClickListener {
            sharePreferenceUtils.removeUser(requireContext())
             (activity as MainActivity).finish()
//            (activity as MainActivity).unbindService((activity as MainActivity).serviceConnection)
//            val intent : Intent = Intent(requireContext(), MediaService::class.java)
//            (activity as MainActivity).stopService(intent)
            findNavController().navigate(R.id.action_profileScreen_to_splashScreen)
        }
    }

    private fun addData() {
       val user = sharePreferenceUtils.getUser(requireContext())
        binding.usernameTextView.text = user.firstName.toString()
        Glide.with(this).load(user.image).into(binding.profileImage)
    }
}