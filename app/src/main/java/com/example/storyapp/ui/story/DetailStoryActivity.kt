package com.example.storyapp.ui.story

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.storyapp.databinding.ActivityDetailStoryBinding
import com.example.storyapp.remote.response.StoryItem

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user: StoryItem? = intent.getParcelableExtra("story")
        if (user != null) {
            binding.tvDetailName.text = user.name
            Glide.with(this).load(user.photoUrl).into(binding.ivDetailPhoto)
            binding.tvDetailDescription.text = user.description
        } else {
            Toast.makeText(this, "Data null", Toast.LENGTH_SHORT).show()
        }

    }
}