package com.renald.storyapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.renald.storyapp.controller.local.UserPreference
import com.renald.storyapp.controller.remote.ApiService
import com.renald.storyapp.model.response.StoryResponse

class StoryPagingSource(private val pref: UserPreference, private val apiService: ApiService) :
    PagingSource<Int, StoryResponse>() {
    override fun getRefreshKey(state: PagingState<Int, StoryResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryResponse> {
        return try {
            val token = pref.getUser().token
            val page = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getListStories("Bearer $token", page, params.loadSize, 0)
            LoadResult.Page(
                data = responseData.listStory,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (responseData.listStory.isEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}

