package com.github.repositorylist.repository.github

import com.github.repositorylist.model.response.RepositoryResponseModel
import com.github.repositorylist.model.response.RepositorySearchResponseModel
import com.github.repositorylist.model.response.UserResponseModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IGithubService {
    @GET("repositories")
    suspend fun getRepositoryList(@Query("since") since: Int?): List<RepositoryResponseModel>

    @GET("search/repositories")
    suspend fun getRepositorySearch(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): RepositorySearchResponseModel

    @GET("users/{login}")
    suspend fun getUser(@Path("login") login: String): UserResponseModel
}