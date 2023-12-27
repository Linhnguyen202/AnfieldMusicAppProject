package com.example.anfieldmusicapp.layout

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.anfieldmusicapp.R
import com.example.anfieldmusicapp.adapter.PlaylistAdapter
import com.example.anfieldmusicapp.databinding.FragmentMenuScreenBinding
import com.example.anfieldmusicapp.model.Music
import com.example.anfieldmusicapp.model.PlaylistResponse
import com.example.anfieldmusicapp.share.sharePreferenceUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MenuScreen : Fragment() {
    lateinit var binding : FragmentMenuScreenBinding
    lateinit var db : FirebaseDatabase
    lateinit var reference : DatabaseReference
    lateinit var adapter : PlaylistAdapter

    var playlistArray : ArrayList<PlaylistResponse> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMenuScreenBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = FirebaseDatabase.getInstance()
        reference = db.getReference("Playlist")
        adapter = PlaylistAdapter()
        binding.playlistList.adapter = this@MenuScreen.adapter
        getData()


    }

    private fun getData() {
        val user = sharePreferenceUtils.getUser(requireContext()).id
        CoroutineScope(Dispatchers.IO).launch {
            reference.child(user.toString()).addValueEventListener(object  : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(item  in  snapshot.children){
                        reference.child(user.toString()).child(item.key.toString()).child("name").addValueEventListener(object  : ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val playlist = PlaylistResponse(item.key.toString(),snapshot.value.toString())
                                    playlistArray.add(playlist)
                                    Log.d("differ",playlistArray.toString())
                                    adapter.differ.submitList(playlistArray)
                            }

                            override fun onCancelled(error: DatabaseError) {

                            }

                        })
                    }

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }
    }

}