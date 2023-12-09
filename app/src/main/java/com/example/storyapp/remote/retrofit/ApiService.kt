package com.example.storyapp.remote.retrofit

import com.example.storyapp.remote.response.AllStoryResponse
import com.example.storyapp.remote.response.LoginResponse
import com.example.storyapp.remote.response.PostResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name:String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): Call<PostResponse>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @Multipart
    @POST("stories")
    fun addStories(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Header("Authorization") auth: String
    ): Call<PostResponse>

    @GET("stories")
    fun getAllStories(@Header("Authorization") auth: String, @Query("location") location: Int = 0) : Call<AllStoryResponse>

    @GET("stories")
    suspend fun getAllStories(
        @Header("Authorization")auth: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("location") location: Int = 0
    ):AllStoryResponse
}