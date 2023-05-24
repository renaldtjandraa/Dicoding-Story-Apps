package com.renald.storyapp.controller.local

import android.content.Context
import com.renald.storyapp.model.entity.UserModel


class UserPreference(context: Context) {
    private val preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    fun getUser(): UserModel {
        val userId = preferences.getString(USER_ID, null)
        val name = preferences.getString(NAME, null)
        val token = preferences.getString(TOKEN, null)
        return UserModel(userId, name, token)
    }

    fun saveUser(user: UserModel) {
        val edit = preferences.edit()
        edit.putString(USER_ID, user.userId)
        edit.putString(NAME, user.name)
        edit.putString(TOKEN, user.token)
        edit.apply()
    }

    fun clearUser() {
        preferences.edit().clear().apply()
    }

    companion object {
        private const val USER_ID = "userId"
        private const val NAME = "name"
        private const val TOKEN = "token"
    }
}