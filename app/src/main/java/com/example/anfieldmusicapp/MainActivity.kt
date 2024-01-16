package com.example.anfieldmusicapp

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.anfieldmusicapp.application.MyApplication
import com.example.anfieldmusicapp.bottomView.MusicSettingSheet
import com.example.anfieldmusicapp.databinding.ActivityMainBinding
import com.example.anfieldmusicapp.databinding.PlayerViewBinding
import com.example.anfieldmusicapp.model.Music
import com.example.anfieldmusicapp.service.MediaService
import com.example.anfieldmusicapp.utils.MusicStatus
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    lateinit var bindingPlayerView : PlayerViewBinding
    lateinit var mediaService : MediaService
    private  var isServiceConnected : Boolean = false

    public val auth : FirebaseAuth by lazy {
        (application as MyApplication).auth
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var controller =findNavController(R.id.containerFragment)
        binding.bottomNavigation.setupWithNavController(controller)
        bindingPlayerView = binding.playerView
        registerReceiver(broadcastReceiver, IntentFilter("music"),RECEIVER_NOT_EXPORTED)
        addEvents()
        val intent : Intent = Intent(this, MediaService::class.java)
        bindService(intent,serviceConnection, BIND_AUTO_CREATE)
    }


    override fun onDestroy() {
        super.onDestroy()
        if(isServiceConnected){
            unbindService(serviceConnection)
            val intent : Intent = Intent(this, MediaService::class.java)
            stopService(intent)
        }
        unregisterReceiver(broadcastReceiver)
    }

    val broadcastReceiver : BroadcastReceiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent!!.getStringExtra("action_music")
            when (action){
                MusicStatus.PLAY_ACTION.toString() -> {

                }
                MusicStatus.RESUME_ACTION.toString()-> {
                    mediaService.handleActionMusic(MusicStatus.RESUME_ACTION)
                    binding.hanleStartMusicBottom.setImageResource(R.drawable.pause_icon)
                    bindingPlayerView.playButton.setImageResource(R.drawable.big_pause_icon)
                }
                MusicStatus.PAUSE_ACTION.toString() -> {
                    mediaService.handleActionMusic(MusicStatus.PAUSE_ACTION)
                    binding.hanleStartMusicBottom.setImageResource(R.drawable.play_icon)
                    bindingPlayerView.playButton.setImageResource(R.drawable.big_play_icon)
                }
                MusicStatus.NEXT_ACTION.toString() -> {
                    mediaService.handleActionMusic(MusicStatus.NEXT_ACTION)
                }
                MusicStatus.PRE_ACTION.toString() -> {
                    mediaService.handleActionMusic(MusicStatus.PRE_ACTION)
                }
                else -> {

                }
            }
        }

    }

    val serviceConnection : ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val myBinder : MediaService.MediaServiceBinder = service as MediaService.MediaServiceBinder
            mediaService = myBinder.getService()
            isServiceConnected = true
            handlePlayer()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isServiceConnected = false
        }

    }

    private fun handlePlayer() {
        mediaService.player!!.addListener(object : Player.Listener{
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                super.onMediaItemTransition(mediaItem, reason)
                // change state player
                changeStatePlayView()
            }
            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                if(playbackState == ExoPlayer.STATE_READY){ // when song ready
                    // change state player
                    changeStatePlayView()

                    // update progress time and progress bar
                    updatePLayerPositionProgress()

                    // player view events
                    playerViewEvents()
                }

                if(playbackState == ExoPlayer.STATE_ENDED){
                   // when song end
                    binding.hanleStartMusicBottom.setImageResource(R.drawable.play_icon)
                    bindingPlayerView.playButton.setImageResource(R.drawable.big_play_icon)
                }


            }


        })
    }
    private fun changeStatePlayView(){

        // change view in playerView
        bindingPlayerView.songName.text =   mediaService.player!!.currentMediaItem!!.mediaMetadata.title
        bindingPlayerView.artistTitle.text = mediaService.player!!.currentMediaItem!!.mediaMetadata.artist
        bindingPlayerView.playerToolbar.songTitle.text = mediaService.player!!.currentMediaItem!!.mediaMetadata.title
        Glide.with(this@MainActivity).load(mediaService.player!!.currentMediaItem!!.mediaMetadata.artworkUri).into(bindingPlayerView.imageView)
        bindingPlayerView.mySeekbar.max = mediaService.player!!.duration.toInt()
        bindingPlayerView.mySeekbar.progress = mediaService.player!!.currentPosition.toInt()


        // change view in bottomView
        binding.layoutBottomMusic.visibility = View.VISIBLE
        binding.musicTitle.text =  mediaService.player!!.currentMediaItem!!.mediaMetadata.title
        Glide.with(this@MainActivity).load(mediaService.player!!.currentMediaItem!!.mediaMetadata.artworkUri).into(binding.musicBottomImg)
        binding.authorName.text = mediaService.player!!.currentMediaItem!!.mediaMetadata.artist

        if(mediaService.isPLaying){
            bindingPlayerView.playButton.setImageResource(R.drawable.big_pause_icon)
            binding.hanleStartMusicBottom.setImageResource(R.drawable.pause_icon)
        }
        else{
            bindingPlayerView.playButton.setImageResource(R.drawable.big_play_icon)
            binding.hanleStartMusicBottom.setImageResource(R.drawable.play_icon)
        }
    }

    private fun playerViewEvents() {
        // handle seekbar
        bindingPlayerView.mySeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                mediaService.player!!.seekTo(seekBar!!.progress.toLong())
            }

        })
        // handle play button in player view
        bindingPlayerView.playButton.setOnClickListener {
            if(mediaService.player!!.isPlaying){
                mediaService.handleActionMusic(MusicStatus.PAUSE_ACTION)
                bindingPlayerView.playButton.setImageResource(R.drawable.big_play_icon)
                binding.hanleStartMusicBottom.setImageResource(R.drawable.play_icon)

            }
            else{
                mediaService.handleActionMusic(MusicStatus.RESUME_ACTION)
                bindingPlayerView.playButton.setImageResource(R.drawable.big_pause_icon)
                binding.hanleStartMusicBottom.setImageResource(R.drawable.pause_icon)

            }
        }

        // handle next song
        bindingPlayerView.btnPlayNext.setOnClickListener {
            mediaService.handleActionMusic(MusicStatus.NEXT_ACTION)
        }

        // handle pre song
        bindingPlayerView.btnPlayPre.setOnClickListener {
            mediaService.handleActionMusic(MusicStatus.PRE_ACTION)
        }
        bindingPlayerView.repeatBtn.setOnClickListener {
            if(mediaService.player!!.repeatMode == ExoPlayer.REPEAT_MODE_OFF){
                bindingPlayerView.repeatBtn.setImageResource(R.drawable.ic_baseline_repeat_active_24)
            }
            else{
                bindingPlayerView.repeatBtn.setImageResource(R.drawable.ic_baseline_repeat_24)
            }
            mediaService.handleActionMusic(MusicStatus.REPEAT_MODE)

        }
        bindingPlayerView.randomBtn.setOnClickListener {
            if(mediaService.player!!.shuffleModeEnabled){
                binding.playerView.randomBtn.setImageResource(R.drawable.ic_baseline_shuffle_24)
            }
            else{
                binding.playerView.randomBtn.setImageResource(R.drawable.ic_baseline_shuffle_active_24)
            }
            mediaService.handleActionMusic(MusicStatus.RANDOM_MODE)
        }

        // handle open bottom setting view
        bindingPlayerView.playerToolbar.menuSettingMusic.setOnClickListener {
            MusicSettingSheet().show(supportFragmentManager,"MusicSettingSheet")
        }


    }

    private fun addEvents() {
        // handle play or pause music
        binding.hanleStartMusicBottom.setOnClickListener {
            if(mediaService.player!!.isPlaying){
                mediaService.handleActionMusic(MusicStatus.PAUSE_ACTION)
                binding.hanleStartMusicBottom.setImageResource(R.drawable.play_icon)
                bindingPlayerView.playButton.setImageResource(R.drawable.big_play_icon)
            }
            else{
                mediaService.handleActionMusic(MusicStatus.RESUME_ACTION)
                binding.hanleStartMusicBottom.setImageResource(R.drawable.pause_icon)
                bindingPlayerView.playButton.setImageResource(R.drawable.big_pause_icon)
            }
        }

        binding.layoutBottomMusic.setOnClickListener {
            bindingPlayerView.playerView.visibility = View.VISIBLE
            binding.bottomNavigation.visibility = View.GONE
        }
        bindingPlayerView.playerToolbar.backBtn.setOnClickListener{
            bindingPlayerView.playerView.visibility = View.GONE
            binding.bottomNavigation.visibility = View.VISIBLE
        }
        binding.handleNextMusicBottom.setOnClickListener {
            mediaService.handleActionMusic(MusicStatus.NEXT_ACTION)
        }
        binding.handlePreMusicBottom.setOnClickListener {
            mediaService.handleActionMusic(MusicStatus.PRE_ACTION)
        }
    }

    private fun updatePLayerPositionProgress() {
        Handler().postDelayed(Runnable {
            kotlin.run {
                if(mediaService.player!!.isPlaying){
                    bindingPlayerView.mySeekbar.progress = mediaService.player!!.currentPosition.toInt()
                }
                updatePLayerPositionProgress()
            }
        },1000)
    }




    public fun startMusicFromService(position : Int,dataList: MutableList<Music>){

        // open service
        val intent : Intent = Intent(this, MediaService::class.java)
        var bundle = bundleOf(
            "song" to dataList,
            "position" to position
        )
        intent.putExtras(bundle)
        // foreground service
        startService(intent)
        bindService(intent,serviceConnection, BIND_AUTO_CREATE)
    }


    public fun getMv(){
        if(mediaService.player!!.isPlaying){
            mediaService.handleActionMusic(MusicStatus.PAUSE_ACTION)
            bindingPlayerView.playButton.setImageResource(R.drawable.big_pause_icon)
            binding.hanleStartMusicBottom.setImageResource(R.drawable.ic_baseline_play_arrow_24)

        }
    }
}