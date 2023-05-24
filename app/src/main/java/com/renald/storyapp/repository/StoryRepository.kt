package com.renald.storyapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.renald.storyapp.controller.local.UserPreference
import com.renald.storyapp.controller.remote.ApiService
import com.renald.storyapp.data.Result
import com.renald.storyapp.data.Result.*
import com.renald.storyapp.data.StoryPagingSource
import com.renald.storyapp.model.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository(private val pref: UserPreference, private val apiService: ApiService) {

    fun signUp(name: String, email: String, password: String): LiveData<Result<GeneralResponse>> =
        liveData {
            emit(Loading)
            try {
                val response = apiService.userSignUp(name, email, password)
                if (response.error) {
                    emit(Error(response.message))
                } else {
                    emit(Success(response))
                }
            } catch (e: Exception) {
                emit(Error(e.message.toString()))
            }
        }

    fun login(email: String, password: String): LiveData<Result<LoginResponse>> = liveData {
        emit(Loading)
        try {
            val response = apiService.login(email, password)
            if (response.error) {
                emit(Error(response.message))
            } else {
                emit(Success(response))
            }
        } catch (e: Exception) {
            emit(Error(e.message.toString()))
        }
    }

    fun getListStories(): LiveData<PagingData<StoryResponse>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(pref, apiService)
            }
        ).liveData
    }

    fun getDetailStory(id: String): LiveData<Result<DetailStoryResponse>> = liveData {
        emit(Loading)
        val token = pref.getUser().token
        try {
            val response = apiService.getDetailStory("Bearer $token", id)
            if (response.error) {
                emit(Error(response.message))
            } else {
                emit(Success(response))
            }
        } catch (e: Exception) {
            emit(Error(e.message.toString()))
        }
    }

    fun uploadImage(
        imageMultipart: MultipartBody.Part,
        description: RequestBody
    ): LiveData<Result<GeneralResponse>> = liveData {
        emit(Loading)
        val token = pref.getUser().token
        try {
            val response = apiService.uploadImage("Bearer $token", imageMultipart, description)
            if (response.error) {
                emit(Error(response.message))
            } else {
                emit(Success(response))
            }
        } catch (e: Exception) {
            emit(Error(e.message.toString()))
        }
    }

    fun getStoryLocation(): LiveData<Result<StoryListResponse>> = liveData {
        emit(Loading)
        val token = pref.getUser().token
        try {
            val response = apiService.getListStories("Bearer $token", 1, 100, 1)
            if (response.error) {
                emit(Error(response.message))
            } else {
                emit(Success(response))
            }
        } catch (e: Exception) {
            emit(Error(e.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            preferences: UserPreference,
            apiService: ApiService
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(preferences, apiService)
            }.also { instance = it }
    }
}