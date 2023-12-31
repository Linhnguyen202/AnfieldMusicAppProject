package com.example.anfieldmusicapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.anfieldmusicapp.R
import com.example.anfieldmusicapp.databinding.HomeMusicItemBinding
import com.example.anfieldmusicapp.databinding.PlaylistItemBinding
import com.example.anfieldmusicapp.model.Music
import com.example.anfieldmusicapp.model.Playlist
import com.example.anfieldmusicapp.model.PlaylistResponse

class PlaylistAdapter(val onClick : (String)->Unit, val onClickDelete : (String) -> Unit, val onClickUpdate : (String) -> Unit) : RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {
    val differ = AsyncListDiffer(this,differCallback)

    inner class PlaylistViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val binding = PlaylistItemBinding.bind(itemView)
        fun inject(playlist: PlaylistResponse){
            binding.playlistItemTitle.text = playlist.name
            binding.itemContainer.setOnClickListener {
                onClick.invoke(playlist.id)
            }
            binding.deleteBtn.setOnClickListener {
                onClickDelete.invoke(playlist.id)
            }
            binding.editBtn.setOnClickListener {
                onClickUpdate.invoke(playlist.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlist_item,parent,false)
        return PlaylistViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.inject(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    companion object {
        private val differCallback = object : DiffUtil.ItemCallback<PlaylistResponse>(){
            override fun areItemsTheSame(oldItem: PlaylistResponse, newItem: PlaylistResponse): Boolean {
                return oldItem == newItem

            }

            override fun areContentsTheSame(oldItem: PlaylistResponse, newItem: PlaylistResponse): Boolean {
                return oldItem == newItem
            }

        }
    }



}

