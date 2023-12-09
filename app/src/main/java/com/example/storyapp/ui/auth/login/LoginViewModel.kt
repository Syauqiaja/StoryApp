package com.example.storyapp.ui.auth.login

import androidx.lifecycle.*
import com.example.storyapp.model.UserModel
import com.example.storyapp.remote.response.LoginResponse
import com.example.storyapp.remote.response.LoginResult
import com.example.storyapp.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Response

class LoginViewModel(): ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage : LiveData<String?> = _toastMessage

    private val _userModel = MutableLiveData<UserModel>()
    val userModel : LiveData<UserModel> = _userModel

    private fun login(loginResult: LoginResult){
        _userModel.value = UserModel(
            loginResult.name!!,
            loginResult.userId!!,
            true,
            loginResult.token!!
        )
    }
    fun login(email: String, password: String){
        _isLoading.value = true
        val apiService = ApiConfig().getApiService()
        val client = apiService.login(email, password)
        client.enqueue(object : retrofit2.Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if(response.isSuccessful){
                    val responseBody = response.body()!!
                    if(responseBody.error!!){
                        _isLoading.value = false
                        _toastMessage.value = "Login failed : ${response.message()}"
                    }else{
                        _isLoading.value = false
                        login(responseBody.loginResult!!)
                    }
                }else{
                    _isLoading.value = false
                    _toastMessage.value = "Login failed : ${response.message()}"
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                _toastMessage.value = "onFailure : ${t.message}"
            }

        })
    }

    fun removeToast() {
        _toastMessage.value = null
    }
}