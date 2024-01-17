package com.example.anfieldmusicapp.bottomView

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import com.example.anfieldmusicapp.MainActivity
import com.example.anfieldmusicapp.R
import com.example.anfieldmusicapp.adapter.PlaylistAdapter
import com.example.anfieldmusicapp.adapter.PlaylistSheetAdapter
import com.example.anfieldmusicapp.application.MyApplication
import com.example.anfieldmusicapp.databinding.FragmentMusicSettingSheetBinding
import com.example.anfieldmusicapp.databinding.FragmentPlaylistSheetBinding
import com.example.anfieldmusicapp.model.Music
import com.example.anfieldmusicapp.model.Playlist
import com.example.anfieldmusicapp.model.PlaylistResponse
import com.example.anfieldmusicapp.repositiory.MusicRepository
import com.example.anfieldmusicapp.share.sharePreferenceUtils
import com.example.anfieldmusicapp.utils.Resource
import com.example.anfieldmusicapp.viewModel.MusicViewModel.MusicViewModel
import com.example.anfieldmusicapp.viewModel.MusicViewModel.MusicViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
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
    lateinit var db : FirebaseDatabase // real time db
    lateinit var reference : DatabaseReference // tool truy van voi db
    lateinit var adapter : PlaylistSheetAdapter


    val user by lazy {
        sharePreferenceUtils.getUser(requireContext()).id // lay thong tin nguoi dung tu sharePreference (id)
    }
    val data by lazy {
        (activity as MainActivity).mediaService.player!!.currentMediaItem!!.mediaId //(lay id bai hat hien tai tu service (mainACtivity))
    }
    var playlistArray : ArrayList<PlaylistResponse> = ArrayList() // mang chua playlist tu db cho vao recycleView

    companion object {
        const val ARG_DATA = "key"
        // de gui du lieu tu MusicSettingSheet sang ben PlaylistSheet
        fun newInstance(music: Music): PlaylistSheet {
            val fragment = PlaylistSheet()
            val args = bundleOf(
                "music" to music
            )
            fragment.arguments = args
            return fragment
        }
    }
    // Nhan du lieu tu MusicSheetingSheet gan vao musicData
    val musicData by lazy {
        arguments?.get("music") as Music
    }
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
        // khai bao db
        db = FirebaseDatabase.getInstance()
        reference = db.getReference("Playlist") //https://anfieldauth-default-rtdb.firebaseio.com/Playlist
        // adapter la cau noi giua data va recycleView
        adapter = PlaylistSheetAdapter(onClickMuic) // tao adapter de gui du lieu vao recycleView
        binding.playlistList.adapter = this@PlaylistSheet.adapter // gan adapter vao reycleView
        getData()
        observerData()
        addEvents()
    }

    private fun observerData() {

    }

    private fun getData() = CoroutineScope(Dispatchers.IO).launch  {
        withContext(Dispatchers.Main){
            binding.loadingPlaylist.visibility = View.VISIBLE
        }

        // ham lay du lieu tu firebase (chay background)x
        //https://anfieldauth-default-rtdb.firebaseio.com/Playlist/dkFQBzjxYXQJ3G3kSfjsdGKHY3e2Y3e2
        reference.child(user.toString()).addValueEventListener(object  : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // snapShot.children la mang chua cac playlist cua user
                for(item  in  snapshot.children){

                    //lay tung thang va cho vao mang
                    val playlist = PlaylistResponse(item.key.toString(),item.child("name").value.toString())

                    playlistArray.add(playlist)
                }
                // cho cai mang playlistArray vao reycleView
                adapter.differ.submitList(playlistArray)
                //loading tat
                binding.loadingPlaylist.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun addEvents() {
        // tao moi playlist
        binding.addPlaylistBtn.setOnClickListener{
            val playlistForm = PlaylistForm.newInstance(musicData)
            playlistForm.show(parentFragmentManager,"ADD_FORM")
            this.dismiss()
        }
    }

    // an vao playlist hien tai de them vao
    private val onClickMuic : (PlaylistResponse) -> Unit = { playlist ->
        // lay id ng dung vao tu sharePreference
        val user = sharePreferenceUtils.getUser(requireContext()).id
        // tao du lieu dang key: value
        val dataPlaylist = LinkedHashMap<String,Music>()
        // GAN DU LIEU VOI KEY LA ID BAI HAT, VALUE LA MUSIC
        dataPlaylist[musicData!!._id.toString()] = musicData
        //https://anfieldauth-default-rtdb.firebaseio.com/Playlist/dkFQBzjxYXQJ3G3kSfjsdGKHY3e2/-Nmv9QtljrJtxGBu22Go/music/idMusic
        reference.child(user.toString()).child(playlist.id.toString()).child("music").child(musicData._id.toString()).setValue(musicData).addOnCompleteListener {
            if(it.isSuccessful){
                Snackbar.make(getDialog()?.getWindow()!!.getDecorView(),"Add playlist Successfully",
                    Snackbar.LENGTH_SHORT).show()
            }
            else{

            }
        }
    }




}