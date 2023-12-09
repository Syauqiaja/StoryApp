package com.example.storyapp.data

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.example.storyapp.database.StoryDatabase
import com.example.storyapp.remote.response.StoryItem
import com.example.storyapp.remote.retrofit.ApiService

class StoryRepository(private val storyDatabase: StoryDatabase, private val apiService: ApiService) {
    fun getStory(token: String?): LiveData<PagingData<StoryItem>>{
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService,token),
            pagingSourceFactory  = {
                storyDatabase.getStoryDao().getAllStories()
            }
        ).liveData
    }
}