package com.renald.storyapp.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginResponse(
    @field:SerializedName("loginResult")
    val loginResult: LoginResult? = null,
    @field:SerializedName("error")
    val error: Boolean,
    @field:SerializedName("message")
    val message: String
) : Parcelable

@Parcelize
data class LoginResult(
    @field:SerializedName("name")
    val name: String,
    @field:SerializedName("userId")
    val userId: String,
    @field:SerializedName("token")
    val token: String
) : Parcelable

