package com.example.storyapp.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.storyapp.remote.response.StoryItem

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: List<StoryItem>)

    @Query("SELECT * FROM listStory ORDER BY createdAt DESC")
    fun getAllStories(): PagingSource<Int, StoryItem>

    @Query("DELETE FROM listStory")
    suspend fun deleteAll()
}