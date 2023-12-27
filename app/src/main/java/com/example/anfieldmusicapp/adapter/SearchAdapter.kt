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
import com.example.anfieldmusicapp.databinding.SearchItemBinding
import com.example.anfieldmusicapp.model.Music

class SearchAdapter(val onClick : (Int,MutableList<Music>)->Unit) : RecyclerView.Adapter<SearchAdapter.MovieViewHolder>() {
    val differ = AsyncListDiffer(this,differCallback)
    inner class MovieViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val binding = SearchItemBinding.bind(itemView)

        fun inject(music: Music,position: Int){
            binding.searchItemTitle.text = music.name_music
            binding.searchItemArtist.text = music.name_singer
            Glide.with(itemView).load(music.image_music).into(binding.songBanner)
            binding.searchItemCard.setOnClickListener {
                onClick.invoke(position,differ.currentList)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_item,parent,false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.inject(differ.currentList[position],position)
    }



    companion object {
        private val differCallback = object : DiffUtil.ItemCallback<Music>(){
            override fun areItemsTheSame(oldItem: Music, newItem: Music): Boolean {
                return oldItem == newItem

            }

            override fun areContentsTheSame(oldItem: Music, newItem: Music): Boolean {
                return oldItem == newItem
            }

        }
    }


    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}


