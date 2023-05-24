package com.renald.storyapp.view.signup

import androidx.lifecycle.ViewModel
import com.renald.storyapp.repository.StoryRepository

class SignUpViewModel(private val repo: StoryRepository) : ViewModel() {
    fun signUp(name: String, email: String, password: String) = repo.signUp(name, email, password)
}