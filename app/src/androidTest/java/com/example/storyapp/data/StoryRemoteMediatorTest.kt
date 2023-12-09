package com.example.storyapp.data

import androidx.paging.*
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.storyapp.database.StoryDatabase
import com.example.storyapp.model.UserModel
import com.example.storyapp.remote.response.*
import com.example.storyapp.remote.retrofit.ApiConfig
import com.example.storyapp.remote.retrofit.ApiService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.junit.After
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Call

@ExperimentalPagingApi
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class StoryRemoteMediatorTest{
    private var mockApi: ApiService = FakeApiService()
    private var mockDb : StoryDatabase = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        StoryDatabase::class.java
    ).allowMainThreadQueries().build()

    @After
    fun tearDown(){
        mockDb.clearAllTables()
    }

    @Test
    fun refreshLoadReturnsSuccessResultWhenMoreDataIsPresent() = runTest{
        val remoteMediator = StoryRemoteMediator(
            mockDb,
            mockApi,
            ""
        )
        val pagingState = PagingState<Int, StoryItem>(
            listOf(), null, PagingConfig(10), 10
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }
}

class FakeApiService : ApiService{
    override fun register(name: String, email: String, password: String): Call<PostResponse> {
        return ApiConfig().getApiService().register("","","")
    }

    override fun login(email: String, password: String): Call<LoginResponse> {
        return ApiConfig().getApiService().login("","")
    }

    override fun addStories(
        file: MultipartBody.Part,
        description: RequestBody,
        auth: String
    ): Call<PostResponse> {
        return ApiConfig().getApiService().register("","","")
    }

    override fun getAllStories(auth: String, location: Int): Call<AllStoryResponse> {
        return ApiConfig().getApiService().getAllStories("")
    }

    override suspend fun getAllStories(
        auth: String,
        page: Int,
        size: Int,
        location: Int
    ): AllStoryResponse {
        val items: MutableList<StoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = StoryItem(
                photoUrl = "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png",
                createdAt = "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png",
                name = "dummy $i",
                description = "desc $i",
                lat = 100.0,
                lon = 100.0,
                id = i.toString(),
            )
            items.add(story)
        }
        return AllStoryResponse(
            items.subList((page - 1) * size, (page - 1) * size + size),
            false,
            ""
        )
    }
}