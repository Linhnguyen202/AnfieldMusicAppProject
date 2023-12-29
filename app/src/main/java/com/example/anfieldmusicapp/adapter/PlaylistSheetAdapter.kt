package com.example.anfieldmusicapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.anfieldmusicapp.R
import com.example.anfieldmusicapp.databinding.PlaylistItemBinding
import com.example.anfieldmusicapp.databinding.PlaylistSheetItemBinding
import com.example.anfieldmusicapp.model.Music
import com.example.anfieldmusicapp.model.MusicResponse
import com.example.anfieldmusicapp.model.PlaylistResponse

class PlaylistSheetAdapter(val onclick : (PlaylistResponse) -> Unit)  : RecyclerView.Adapter<PlaylistSheetAdapter.PlaylistSheetViewHolder>() {
    val differ = AsyncListDiffer(this,differCallback)

    inner class PlaylistSheetViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val binding = PlaylistSheetItemBinding.bind(itemView)
        fun inject(playlist: PlaylistResponse){
            binding.playlistItemTitle.text = playlist.name
            binding.itemContainer.setOnClickListener {
                onclick.invoke(playlist)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistSheetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlist_sheet_item,parent,false)
        return PlaylistSheetViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistSheetViewHolder, position: Int) {
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