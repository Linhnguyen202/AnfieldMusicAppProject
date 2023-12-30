package com.example.anfieldmusicapp.layout

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.anfieldmusicapp.R
import com.example.anfieldmusicapp.adapter.PlaylistAdapter
import com.example.anfieldmusicapp.bottomView.PlaylistForm
import com.example.anfieldmusicapp.databinding.FragmentMenuScreenBinding
import com.example.anfieldmusicapp.model.Music
import com.example.anfieldmusicapp.model.PlaylistResponse
import com.example.anfieldmusicapp.share.sharePreferenceUtils
import com.google.firebase.database.ChildEventListener
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
    val user by lazy {
        sharePreferenceUtils.getUser(requireContext()).id
    }
    lateinit var broadcastReceiver : BroadcastReceiver
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMenuScreenBinding.inflate(layoutInflater)
        setUpBroadCast()
        requireContext().registerReceiver(broadcastReceiver, IntentFilter("UPDATE_ACTION"),
            Context.RECEIVER_NOT_EXPORTED)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = FirebaseDatabase.getInstance()
        reference = db.getReference("Playlist")
        adapter = PlaylistAdapter(onClick,onClickDelete,onClickUpdate)
        binding.playlistList.adapter = this@MenuScreen.adapter
        getData()
        reference.child(user.toString()).addChildEventListener(object :
            ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                getData()
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }
    private fun setUpBroadCast(){
        broadcastReceiver = object : BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                getData()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        requireContext().unregisterReceiver(broadcastReceiver)
    }
    override fun onDestroy() {
        super.onDestroy()

    }

    private fun getData() {
        var playlistArray : ArrayList<PlaylistResponse> = ArrayList()
        CoroutineScope(Dispatchers.IO).launch {
            reference.child(user.toString()).addValueEventListener(object  : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.hasChildren()){
                        for(item  in  snapshot.children){
                            reference.child(user.toString()).child(item.key.toString()).child("name").addValueEventListener(object  : ValueEventListener{
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    val playlist = PlaylistResponse(item.key.toString(),snapshot.value.toString())
                                    playlistArray.add(playlist)
                                    adapter.differ.submitList(playlistArray)
                                }

                                override fun onCancelled(error: DatabaseError) {

                                }

                            })
                        }

                    }
                    else {
                        adapter.differ.submitList(playlistArray)

                    }


                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }
    }

    val onClick : (String) -> Unit = {
        val bundle = bundleOf(
            "Playlist" to it
        )
        findNavController().navigate(R.id.action_menuScreen_to_playlistMusicListScreen,bundle)
    }
    val onClickUpdate : (String) -> Unit = {
        val playlistForm = PlaylistForm.newInstanceUpdate(it)
        playlistForm.show(parentFragmentManager,"UPDATE_FORM")
    }

    val onClickDelete : (String) -> Unit = {
        reference.child(user.toString()).child(it).removeValue()
    }

}