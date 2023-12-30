package com.example.anfieldmusicapp.bottomView

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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
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
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class PlaylistForm : BottomSheetDialogFragment() {
   lateinit var binding: FragmentPlaylistFormBinding
    lateinit var db : FirebaseDatabase
    lateinit var reference : DatabaseReference





    val user by lazy {
        sharePreferenceUtils.getUser(requireContext()).id
    }
    companion object {
        const val ARG_DATA = "key"
        fun newInstance(music: Music): PlaylistForm {
            val fragment = PlaylistForm()
            val args = bundleOf(
                "music" to music
            )
            fragment.arguments = args
            return fragment
        }

        fun newInstanceUpdate(id: String): PlaylistForm {
            val fragment = PlaylistForm()
            val args = Bundle()
            args.putString(ARG_DATA, id)
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
        if(tag == "ADD_FORM"){
            binding.playListEdt.hint = "Add your new playlist"
        }
        else if(tag == "UPDATE_FORM"){
            binding.playListEdt.hint = "Update your playlist"
        }

        db = FirebaseDatabase.getInstance()
        reference = db.getReference("Playlist")

        getData()
        observerData()
        addEvents()


    }



    private fun observerData() {
        reference.child(user.toString()).child(arguments?.getString("key").toString()).addChildEventListener(object : ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                Snackbar.make(getDialog()?.getWindow()!!.getDecorView(),"Update playlist Successfully",
                    Snackbar.LENGTH_SHORT).show()
                binding.playListEdt.text!!.clear()
                this@PlaylistForm.context?.sendBroadcast(Intent("UPDATE_ACTION"))
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

    private fun getData() {


    }

    private fun addEvents() {
        val user = sharePreferenceUtils.getUser(requireContext()).id
        binding.submitBtn.setOnClickListener{
            if(tag == "ADD_FORM"){
                addData()
            }
            else if(tag == "UPDATE_FORM"){
                updateData()
            }
        }

    }
    fun addData(){
        val musicData : Music = arguments?.get("music") as Music
        val playlist = binding.playListEdt.text.toString()
        val dataPlaylist = LinkedHashMap<String,Music>()
        dataPlaylist[musicData!!._id.toString()] = musicData!!

        val playlistData = Playlist(playlist,dataPlaylist)
        reference.child(user.toString()).push().setValue(playlistData).addOnCompleteListener {
            if(it.isSuccessful){
                Snackbar.make(getDialog()?.getWindow()!!.getDecorView(),"Add playlist Successfully",
                    Snackbar.LENGTH_SHORT).show()
                binding.playListEdt.text!!.clear()
            }
            else{

            }
        }
    }
    fun updateData()  {
        val playlist = binding.playListEdt.text.toString()
        val updates = hashMapOf<String, Any>(
            "name" to playlist
        )
        reference.child(user.toString()).child(arguments?.getString("key").toString()).updateChildren(updates)
    }


}