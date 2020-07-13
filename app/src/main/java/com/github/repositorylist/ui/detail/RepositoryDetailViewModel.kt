package com.github.repositorylist.ui.detail

import androidx.lifecycle.*
import com.github.repositorylist.model.common.Result
import com.github.repositorylist.model.response.RepositoryResponseModel
import com.github.repositorylist.model.response.UserResponseModel
import com.github.repositorylist.repository.github.GithubRepository
import javax.inject.Inject

class RepositoryDetailViewModel @Inject constructor(
    private val githubRepository: GithubRepository
) : ViewModel() {
    private val mUser = MediatorLiveData<Result<UserResponseModel>>()
    val userResponseModel: LiveData<Result<UserResponseModel>>
        get() = mUser

    private val mRepositoryResponseModel = MutableLiveData<RepositoryResponseModel>()
    val repositoryResponseModel: LiveData<RepositoryResponseModel>
        get() = mRepositoryResponseModel

    fun init(repositoryResponseModel: RepositoryResponseModel) {
        mRepositoryResponseModel.postValue(repositoryResponseModel)
        mUser.addSource(githubRepository.getUser(viewModelScope, repositoryResponseModel.owner.login)) {
            mUser.postValue(it)
        }
    }
}