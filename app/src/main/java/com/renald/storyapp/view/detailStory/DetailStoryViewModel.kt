package com.renald.storyapp.view.detailStory

import androidx.lifecycle.ViewModel
import com.renald.storyapp.repository.StoryRepository

class DetailStoryViewModel(private val repo: StoryRepository) : ViewModel() {
    fun getDetailStory(id :String) = repo.getDetailStory(id)
}