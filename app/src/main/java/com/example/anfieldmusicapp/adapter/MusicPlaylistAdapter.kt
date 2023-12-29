package com.example.anfieldmusicapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.anfieldmusicapp.R
import com.example.anfieldmusicapp.databinding.PlaylistItemBinding
import com.example.anfieldmusicapp.databinding.PlaylistMusicItemBinding
import com.example.anfieldmusicapp.layout.PlaylistMusicListScreen
import com.example.anfieldmusicapp.model.Music
import com.example.anfieldmusicapp.model.MusicFirebase
import com.example.anfieldmusicapp.model.PlaylistResponse

class MusicPlaylistAdapter (val onClick : (Int,MutableList<MusicFirebase>)->Unit,val onClickDelete : (MusicFirebase) -> Unit) : RecyclerView.Adapter<MusicPlaylistAdapter.MusicPlaylistViewHolder>() {
    val differ = AsyncListDiffer(this,differCallback)

    inner class MusicPlaylistViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val binding = PlaylistMusicItemBinding.bind(itemView)
        fun inject(music: MusicFirebase,position: Int){
            binding.musicItemTitle.text = music.data.name_music
            binding.artistItemTitle.text = music.data.name_singer
            Glide.with(itemView).load(music.data.image_music).into(binding.songBanner)
            binding.searchItemCard.setOnClickListener {
                onClick.invoke(position,differ.currentList)
            }
            binding.deleteMusicBtn.setOnClickListener {
                onClickDelete.invoke(music)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicPlaylistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlist_music_item,parent,false)
        return MusicPlaylistViewHolder(view)
    }

    override fun onBindViewHolder(holder: MusicPlaylistViewHolder, position: Int) {
        holder.inject(differ.currentList[position],position)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    companion object {
        private val differCallback = object : DiffUtil.ItemCallback<MusicFirebase>(){
            override fun areItemsTheSame(oldItem: MusicFirebase, newItem: MusicFirebase): Boolean {
                return oldItem == newItem

            }

            override fun areContentsTheSame(oldItem: MusicFirebase, newItem: MusicFirebase): Boolean {
                return oldItem == newItem
            }

        }
    }



}
