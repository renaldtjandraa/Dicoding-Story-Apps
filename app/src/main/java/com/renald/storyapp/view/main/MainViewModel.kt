package com.renald.storyapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.renald.storyapp.model.response.StoryResponse
import com.renald.storyapp.repository.StoryRepository

class MainViewModel(repo: StoryRepository) : ViewModel() {
    val stories: LiveData<PagingData<StoryResponse>> = repo.getListStories()
}