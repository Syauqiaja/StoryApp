package com.example.storyapp.ui.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.model.UserModel
import com.example.storyapp.remote.response.PostResponse
import com.example.storyapp.remote.retrofit.ApiConfig
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response

class StoryViewModel :ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String?>()
    val message : MutableLiveData<String?> = _message

    fun uploadStory(imageMultipart : MultipartBody.Part, description: RequestBody, userModel: UserModel, onComplete : () -> Unit){
        _isLoading.value = true
        val apiService = ApiConfig().getApiService()
        val client = apiService.addStories(imageMultipart, description, "Bearer ${userModel.token}")
        client.enqueue(object : retrofit2.Callback<PostResponse>{
            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                if(response.isSuccessful){
                    val responseBody = response.body()
                    if(responseBody != null && !responseBody.error){
                        _isLoading.value = false
                        _message.value = responseBody.message
                        onComplete()
                    }
                }else{
                    _isLoading.value = false
                    _message.value = "onFailure : ${response.message()}"
                }
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                _isLoading.value = false
                _message.value = "onFailure : ${t.message}"
            }

        })
    }
    fun removeMessage(){
        _message.value = null
    }
}