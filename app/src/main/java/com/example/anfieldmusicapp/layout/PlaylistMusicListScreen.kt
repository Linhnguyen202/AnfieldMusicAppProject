package com.example.anfieldmusicapp.layout

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.anfieldmusicapp.MainActivity
import com.example.anfieldmusicapp.R
import com.example.anfieldmusicapp.adapter.MusicPlaylistAdapter
import com.example.anfieldmusicapp.databinding.FragmentPlaylistMusicListScreenBinding
import com.example.anfieldmusicapp.model.Music
import com.example.anfieldmusicapp.model.MusicFirebase
import com.example.anfieldmusicapp.model.MusicResponse
import com.example.anfieldmusicapp.model.PlaylistResponse
import com.example.anfieldmusicapp.repositiory.MusicRepository
import com.example.anfieldmusicapp.share.sharePreferenceUtils
import com.example.anfieldmusicapp.utils.MusicStatus
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PlaylistMusicListScreen : Fragment() {
    lateinit var binding : FragmentPlaylistMusicListScreenBinding
    val args :  PlaylistMusicListScreenArgs by navArgs()
    lateinit var db : FirebaseDatabase
    lateinit var reference : DatabaseReference

    lateinit var adapter : MusicPlaylistAdapter

    val user by lazy {
        sharePreferenceUtils.getUser(requireContext()).id
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPlaylistMusicListScreenBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = FirebaseDatabase.getInstance()
        val user = sharePreferenceUtils.getUser(requireContext()).id
        val playlistId  = args.Playlist.toString()
        reference = db.getReference("Playlist")
        adapter = MusicPlaylistAdapter(onClickMuic,onClickDeleteMusic)
        binding.musicList.adapter = this@PlaylistMusicListScreen.adapter
        getData()
        addEvents()
        reference.child(user.toString()).child(playlistId).child("music").addChildEventListener(object : ChildEventListener{
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

    private fun addEvents() {
        binding.customToolbar.backBtn.setOnClickListener{
            findNavController().popBackStack()
        }
    }


    private fun getData() {
        val playlistId  = args.Playlist.toString()
        var musicArray : ArrayList<MusicFirebase> = ArrayList()
        reference.child(user.toString()).child(playlistId).child("music").addValueEventListener(object  : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.hasChildren()){
                    for(item  in  snapshot.children){
                        val musicFirebase = MusicFirebase(item!!.key.toString(), item!!.getValue(Music::class.java)!!)
                        musicArray.add(musicFirebase)
                        adapter.differ.submitList(musicArray)
                    }
                }
                else{
                    adapter.differ.submitList(musicArray)
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

    private val onClickMuic : (Int,MutableList<MusicFirebase>) -> Unit = { pos, data ->
        val musicArray :  ArrayList<Music> = arrayListOf()
        for(item in data){
            musicArray.add(item.data)
        }
        (activity as MainActivity).startMusicFromService(pos, musicArray)

    }
    private val onClickDeleteMusic : (MusicFirebase) -> Unit = {
        deleteDataMusic(it)
    }

    private fun deleteDataMusic(music : MusicFirebase){
        val user = sharePreferenceUtils.getUser(requireContext()).id
        val playlistId  = args.Playlist.toString()
        reference.child(user.toString()).child(playlistId).child("music").child(music.id).removeValue()
    }


}