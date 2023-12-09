package com.example.storyapp.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.storyapp.data.StoryRepository
import com.example.storyapp.remote.response.StoryItem
import com.example.storyapp.ui.adapter.StoryListAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest{

    @get:Rule
    val insantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var storyRepository: StoryRepository

    @Test
    fun `when get Story Should Not Null and Return Data`() = runTest {
        val dummyStory = DummyData.generateDummyStoriesEntity()
        val data: PagingData<StoryItem> = StoryPagingSource.snapshot(dummyStory)
        val expectedStory = MutableLiveData<PagingData<StoryItem>>()

        expectedStory.value = data
        Mockito.`when`(storyRepository.getStory("")).thenReturn(expectedStory)

        val mainViewModel = MainViewModel(storyRepository)
        val actualStory : PagingData<StoryItem> = mainViewModel.stories("").getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryListAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(actualStory)

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStory.size, differ.snapshot().size)
        Assert.assertEquals(dummyStory[0], differ.snapshot()[0])
    }

    @Test
    fun `when Get Story Empty Should Return No Data`() = runTest {
        val data: PagingData<StoryItem> = PagingData.from(emptyList())
        val expectedStory = MutableLiveData<PagingData<StoryItem>>()
        expectedStory.value = data
        Mockito.`when`(storyRepository.getStory("")).thenReturn(expectedStory)

        val mainViewModel = MainViewModel(storyRepository)
        val actualStory: PagingData<StoryItem> = mainViewModel.stories("").getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryListAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(actualStory)

        Assert.assertEquals(0, differ.snapshot().size)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback{
    override fun onInserted(position: Int, count: Int) {}

    override fun onRemoved(position: Int, count: Int) {}

    override fun onMoved(fromPosition: Int, toPosition: Int) {}

    override fun onChanged(position: Int, count: Int, payload: Any?) {}

}

class StoryPagingSource: PagingSource<Int, LiveData<List<StoryItem>>>(){
    companion object{
        fun snapshot(items: List<StoryItem>): PagingData<StoryItem>{
            return PagingData.from(items)
        }
    }
    override fun getRefreshKey(state: PagingState<Int, LiveData<List<StoryItem>>>): Int? {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<StoryItem>>> {
        return LoadResult.Page(emptyList(),0,1)
    }

}