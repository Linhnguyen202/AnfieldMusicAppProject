package com.example.anfieldmusicapp.bottomView

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.anfieldmusicapp.R
import com.example.anfieldmusicapp.adapter.PlaylistAdapter
import com.example.anfieldmusicapp.databinding.FragmentMusicSettingSheetBinding
import com.example.anfieldmusicapp.databinding.FragmentPlaylistSheetBinding
import com.example.anfieldmusicapp.model.Playlist
import com.example.anfieldmusicapp.model.PlaylistResponse
import com.example.anfieldmusicapp.share.sharePreferenceUtils
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class PlaylistSheet : BottomSheetDialogFragment() {
    lateinit var binding : FragmentPlaylistSheetBinding
    lateinit var db : FirebaseDatabase
    lateinit var reference : DatabaseReference
    lateinit var adapter : PlaylistAdapter
    var playlistArray : ArrayList<PlaylistResponse> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPlaylistSheetBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = FirebaseDatabase.getInstance()
        reference = db.getReference("Playlist")
        adapter = PlaylistAdapter()

        binding.playlistList.adapter = this@PlaylistSheet.adapter
        getData()
        addEvents()

    }

    private fun getData() = CoroutineScope(Dispatchers.IO).launch  {
        val user = sharePreferenceUtils.getUser(requireContext()).id
        withContext(Dispatchers.Main){
            binding.loadingPlaylist.visibility = View.VISIBLE
        }
        reference.child(user.toString()).addValueEventListener(object  : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(item  in  snapshot.children){
                    val playlist = PlaylistResponse(item.key.toString(),item.child("name").value.toString())
                    playlistArray.add(playlist)
                    binding.loadingPlaylist.visibility = View.GONE

                }
                adapter.differ.submitList(playlistArray)

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
        withContext(Dispatchers.Main){

        }



    }

    private fun addEvents() {
        binding.addPlaylistBtn.setOnClickListener{
            PlaylistForm().show(parentFragmentManager,"ADD_FORM")
            this.dismiss()
        }
    }


}