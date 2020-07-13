package com.github.repositorylist.ui.repositoryList

import androidx.lifecycle.*
import com.github.repositorylist.model.common.Result
import com.github.repositorylist.model.response.RepositoryResponseModel
import com.github.repositorylist.repository.github.GithubRepository
import com.github.repositorylist.ui.common.IRepositoryActions
import javax.inject.Inject

class RepositoryListViewModel @Inject constructor(
    private val githubRepository: GithubRepository
) : ViewModel(), IRepositoryActions {
    private var query = ""
    private val mRepositoryList = MediatorLiveData<Result<List<RepositoryResponseModel>>>()
    val repositoryList: LiveData<Result<List<RepositoryResponseModel>>>
        get() = mRepositoryList

    private val mShowRepositoryDetail = MutableLiveData<RepositoryResponseModel>()
    val showRepositoryDetail: LiveData<RepositoryResponseModel>
        get() = mShowRepositoryDetail

    init {
        mRepositoryList.addSource(githubRepository.getRepositoryList(viewModelScope)) {
            mRepositoryList.postValue(it)
        }
    }

    fun onQueryChanged(query: String) {
        if (this.query == query) {
            return
        }

        this.query = query

    }

    fun tryLoadListAgain() {

    }

    override fun showRepositoryDetail(repositoryResponseModel: RepositoryResponseModel) {
        mShowRepositoryDetail.postValue(repositoryResponseModel)
    }
}