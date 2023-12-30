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
import com.example.anfieldmusicapp.model.Music

class HomeMusicAdapter(val onClick : (Int,MutableList<Music>)->Unit)  : RecyclerView.Adapter<HomeMusicAdapter.HomeMusicViewHolder>() {
    val differ = AsyncListDiffer(this,differCallback)

    inner class HomeMusicViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val binding = HomeMusicItemBinding.bind(itemView)
        fun inject(music: Music, position: Int){
           binding.songTitle.text = music.name_music
            Glide.with(itemView).load(music.image_music).into(binding.imgMusicPoster)
            binding.songCard.setOnClickListener {
                onClick.invoke(position,differ.currentList)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeMusicViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_music_item,parent,false)
        return HomeMusicViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeMusicViewHolder, position: Int) {
        holder.inject(differ.currentList[position],position)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
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



}


