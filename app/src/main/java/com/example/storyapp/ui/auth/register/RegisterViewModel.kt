package com.example.storyapp.ui.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.remote.response.PostResponse
import com.example.storyapp.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Response

class RegisterViewModel :ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage : LiveData<String?> = _toastMessage

    fun register(name: String, email: String, password: String){
        _isLoading.value = true
        val apiService = ApiConfig().getApiService()
        val client = apiService.register(name, email, password)
        client.enqueue(object : retrofit2.Callback<PostResponse>{
            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                if(response.isSuccessful){
                    val responseBody = response.body()!!
                    if(responseBody.error!!){
                        _toastMessage.value = responseBody.message
                        _isLoading.value = false
                    }else{
                        _isLoading.value = false
                        _toastMessage.value = "Register success"
                    }
                }else{
                    _isLoading.value = false
                    _toastMessage.value = "Register failure : "+response.message()
                }
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                _isLoading.value = false
                _toastMessage.value = "Register failure : "+ t.message
            }

        })
    }

    fun removeToast() {
        _toastMessage.value = null
    }
}