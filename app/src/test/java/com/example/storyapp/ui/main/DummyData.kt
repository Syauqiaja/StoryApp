package com.example.storyapp.ui.main

import com.example.storyapp.remote.response.StoryItem

object DummyData {
    fun generateDummyStoriesEntity(): List<StoryItem>{
        val newList = ArrayList<StoryItem>()
        for(i in 0..10){
            val story = StoryItem(
                photoUrl = "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png",
                createdAt = "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png",
                name = "dummy $i",
                description = "desc $i",
                lat = 100.0,
                lon = 100.0,
                id = i.toString(),
            )
            newList.add(story)
        }
        return newList
    }
}