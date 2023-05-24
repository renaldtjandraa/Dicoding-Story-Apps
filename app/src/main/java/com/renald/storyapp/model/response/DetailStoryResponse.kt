package com.renald.storyapp.model.response

data class DetailStoryResponse(
    val error: Boolean,
    val message: String,
    val story: StoryResponse
)