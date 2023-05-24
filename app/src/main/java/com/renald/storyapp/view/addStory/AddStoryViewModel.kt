package com.renald.storyapp.view.addStory

import androidx.lifecycle.ViewModel
import com.renald.storyapp.repository.StoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody


class AddStoryViewModel(private val repo: StoryRepository) : ViewModel() {
    fun uploadImage(
        imageMultipartBody: MultipartBody.Part,
        description: RequestBody
    ) = repo.uploadImage(imageMultipartBody, description)
}