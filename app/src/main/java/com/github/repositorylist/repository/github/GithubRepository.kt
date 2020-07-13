package com.github.repositorylist.repository.github

import androidx.lifecycle.LiveData
import com.github.repositorylist.model.common.Result
import com.github.repositorylist.model.response.RepositoryResponseModel
import com.github.repositorylist.model.response.UserResponseModel
import com.github.repositorylist.repository.common.RepositoryExecutor
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class GithubRepository @Inject constructor(private val repositoryExecutor: RepositoryExecutor) {
    fun getRepositoryList(viewModelScope: CoroutineScope): LiveData<Result<List<RepositoryResponseModel>>>
        = repositoryExecutor.makeRequest(IGithubService::class.java, viewModelScope) {
            it.getRepositoryList()
        }

    fun getUser(viewModelScope: CoroutineScope, login: String): LiveData<Result<UserResponseModel>>
            = repositoryExecutor.makeRequest(IGithubService::class.java, viewModelScope) {
        it.getUser(login)
    }
}