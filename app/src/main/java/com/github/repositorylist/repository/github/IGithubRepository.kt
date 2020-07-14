package com.github.repositorylist.repository.github

import androidx.lifecycle.LiveData
import com.github.repositorylist.model.common.Result
import com.github.repositorylist.model.response.RepositoryResponseModel
import com.github.repositorylist.model.response.RepositorySearchResponseModel
import com.github.repositorylist.model.response.UserResponseModel
import kotlinx.coroutines.CoroutineScope

interface IGithubRepository {
    fun getRepositoryList(viewModelScope: CoroutineScope, since: Int?): LiveData<Result<List<RepositoryResponseModel>>>
    fun getRepositorySearch(viewModelScope: CoroutineScope, query: String, pageIndex: Int, perPage: Int): LiveData<Result<RepositorySearchResponseModel>>
    fun getUser(viewModelScope: CoroutineScope, login: String): LiveData<Result<UserResponseModel>>
}