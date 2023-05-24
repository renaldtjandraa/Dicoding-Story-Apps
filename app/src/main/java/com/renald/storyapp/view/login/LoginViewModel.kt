package com.renald.storyapp.view.login


import androidx.lifecycle.ViewModel
import com.renald.storyapp.repository.StoryRepository

class LoginViewModel(private val repo: StoryRepository) : ViewModel() {
    fun login(email: String, password: String) = repo.login(email, password)
}