package com.example.storyapp.remote.response

import com.google.gson.annotations.SerializedName

data class AllStoryResponse(

	@field:SerializedName("listStory")
	val listStory: List<StoryItem>? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)


