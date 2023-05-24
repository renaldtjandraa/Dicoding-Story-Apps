package com.renald.storyapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.renald.storyapp.controller.local.UserPreference
import com.renald.storyapp.controller.remote.ApiConfig
import com.renald.storyapp.repository.StoryRepository


val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val preferences = UserPreference(context)
        val apiService = ApiConfig.getApiService()
        return StoryRepository.getInstance(preferences, apiService)
    }

}