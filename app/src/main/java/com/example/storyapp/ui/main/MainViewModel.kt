package com.example.storyapp.ui.main

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp.data.StoryRepository
import com.example.storyapp.remote.response.StoryItem

class MainViewModel(private val storyRepository: StoryRepository):ViewModel(){
    fun stories(token: String): LiveData<PagingData<StoryItem>> =
        storyRepository.getStory(token).cachedIn(viewModelScope)
}