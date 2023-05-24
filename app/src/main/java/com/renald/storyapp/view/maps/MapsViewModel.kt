package com.renald.storyapp.view.maps

import androidx.lifecycle.ViewModel
import com.renald.storyapp.repository.StoryRepository

class MapsViewModel(private val repo: StoryRepository) : ViewModel() {
    fun getListStoryWithLocation() = repo.getStoryLocation()
}