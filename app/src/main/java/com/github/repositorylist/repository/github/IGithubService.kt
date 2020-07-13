package com.github.repositorylist.repository.github

import com.github.repositorylist.model.response.RepositoryResponseModel
import com.github.repositorylist.model.response.UserResponseModel
import retrofit2.http.GET
import retrofit2.http.Path

interface IGithubService {
    @GET("repositories")
    suspend fun getRepositoryList(): List<RepositoryResponseModel>

    @GET("users/{login}")
    suspend fun getUser(@Path("login") login: String): UserResponseModel
}