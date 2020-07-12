package com.github.repositorylist.repository.github

import com.github.repositorylist.model.response.RepositoryResponseModel
import retrofit2.http.GET

interface IGithubService {
    @GET("repositories")
    suspend fun getRepositoryList(): List<RepositoryResponseModel>
}