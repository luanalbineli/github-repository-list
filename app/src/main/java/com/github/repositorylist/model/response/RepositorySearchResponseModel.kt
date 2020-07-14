package com.github.repositorylist.model.response

import com.google.gson.annotations.SerializedName

data class RepositorySearchResponseModel(
    @SerializedName("items")
    val items: List<RepositoryResponseModel>
)