package com.example.storyapp.ui.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.databinding.ItemStoryBinding
import com.example.storyapp.remote.response.StoryItem
import com.example.storyapp.ui.story.DetailStoryActivity

class StoryListAdapter: PagingDataAdapter<StoryItem, StoryListAdapter.MyViewHolder>(DIFF_CALLBACK) {
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryItem>() {
            override fun areItemsTheSame(oldItem: StoryItem, newItem: StoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: StoryItem, newItem: StoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
    class MyViewHolder(private val binding: ItemStoryBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: StoryItem){
            binding.tvItemName.text = data.name
            binding.tvItemDescription.text = data.description
            Glide.with(itemView).load(data.photoUrl).into(binding.ivItemPhoto)

            itemView.setOnClickListener {
                val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    itemView.context as Activity,
                    androidx.core.util.Pair(binding.tvItemName, "storyname"),
                    androidx.core.util.Pair(binding.tvItemDescription, "storydesc"),
                    androidx.core.util.Pair(binding.ivItemPhoto, "storyphoto"),
                )

                val intent = Intent(itemView.context, DetailStoryActivity::class.java)
                intent.putExtra("story", data)
                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if(data != null){
            holder.bind(data)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
}