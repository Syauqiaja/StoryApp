package com.example.storyapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.storyapp.model.UserModel
import com.example.storyapp.remote.response.StoryItem
import com.example.storyapp.remote.retrofit.ApiService

class StoryPagingSource(private val apiService: ApiService, val token:String): PagingSource<Int, StoryItem>() {
    companion object{
        const val INITIAL_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, StoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1 )?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryItem> {
        return try{
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData =
                apiService.getAllStories("Bearer $token", position, params.loadSize)
            LoadResult.Page(
                data = responseData.listStory!!,
                prevKey = if(position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if(responseData.listStory.isNullOrEmpty()) null else position + 1
            )
        }catch (exception: java.lang.Exception){
            return LoadResult.Error(exception)
        }
    }
}