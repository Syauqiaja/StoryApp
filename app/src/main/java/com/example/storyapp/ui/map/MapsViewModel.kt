package com.example.storyapp.ui.map

import androidx.lifecycle.*
import com.example.storyapp.model.UserModel
import com.example.storyapp.remote.response.AllStoryResponse
import com.example.storyapp.remote.response.StoryItem
import com.example.storyapp.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Response

class MapsViewModel(): ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _stories = MutableLiveData<List<StoryItem>>()
    val stories : LiveData<List<StoryItem>> = _stories

    fun getAllStories(userModel: UserModel, showToast : (message: String?) -> Unit){
        val apiService = ApiConfig().getApiService()
        val client = apiService.getAllStories("Bearer ${userModel.token}", 1)
        client.enqueue(object : retrofit2.Callback<AllStoryResponse>{
            override fun onResponse(
                call: Call<AllStoryResponse>,
                response: Response<AllStoryResponse>
            ) {
                if(response.isSuccessful){
                    _stories.value = response.body()?.listStory!!
                }else{
                    showToast(response.message())
                }
            }

            override fun onFailure(call: Call<AllStoryResponse>, t: Throwable) {
                showToast(t.message)
            }

        })
    }
}
