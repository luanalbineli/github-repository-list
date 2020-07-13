package com.github.repositorylist.model.response

import com.google.gson.annotations.SerializedName

data class UserResponseModel(
    @SerializedName("name")
    val name: String
)