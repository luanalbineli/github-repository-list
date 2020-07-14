package com.github.repositorylist.ui.repositoryList

import androidx.lifecycle.*
import com.github.repositorylist.model.common.Result
import com.github.repositorylist.model.common.Status
import com.github.repositorylist.model.response.RepositoryResponseModel
import com.github.repositorylist.model.ui.RepositoryListStateModel
import com.github.repositorylist.repository.github.IGithubRepository
import com.github.repositorylist.ui.common.IRepositoryActions
import com.github.repositorylist.ui.util.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.plus
import timber.log.Timber
import javax.inject.Inject

class RepositoryListViewModel @Inject constructor(
    private val githubRepository: IGithubRepository
) : ViewModel(), IRepositoryActions {
    private val mRepositoryListStateModel = RepositoryListStateModel()
    private val mFullRepositoryList = mutableListOf<RepositoryResponseModel>()

    private var mCancelableJob: CoroutineScope? = null

    private val mRepositoryList = MediatorLiveData<Result<List<RepositoryResponseModel>>>()
    val repositoryList: LiveData<Result<List<RepositoryResponseModel>>>
        get() = mRepositoryList

    private val mShowRepositoryDetail = MutableLiveData<RepositoryResponseModel>()
    val showRepositoryDetail: LiveData<RepositoryResponseModel>
        get() = mShowRepositoryDetail

    private val mOnQueryChanged = MutableLiveData<Unit>()
    val onQueryChanged: LiveData<Unit>
        get() = mOnQueryChanged

    init {
        getRepositoryList()
    }

    fun tryLoadListAgain() = getRepositoryList()

    fun onQueryChanged(query: String) {
        if (mRepositoryListStateModel.query == query) {
            return
        }

        mRepositoryListStateModel.query = query
        mFullRepositoryList.clear()
        mOnQueryChanged.postValue(Unit)

        getRepositoryList()
    }

    fun loadNextRepositoryListPage() {
        Timber.d("showRepositoryList - loadNextRepositoryListPage")
        mRepositoryListStateModel.pageIndex++
        getRepositoryList()
    }

    private fun getRepositoryList() {
        mCancelableJob?.cancel()

        val query = mRepositoryListStateModel.query
        Timber.d("Query: $query")
        val source = if (query.isNullOrEmpty()) {
            githubRepository.getRepositoryList(viewModelScope, mRepositoryListStateModel.since)
        } else {
            getRepositorySearchListSource(query)
        }

        mRepositoryList.addSource(source) {
            Timber.d("showRepositoryList: $it")
            val updatedStatus = if (it.status == Status.SUCCESS) {
                mFullRepositoryList.addAll(it.data!!)
                mRepositoryListStateModel.since = it.data.lastOrNull()?.id
                Result.success(mFullRepositoryList.toList())
            } else {
                it
            }

            mRepositoryList.postValue(updatedStatus)
        }
    }

    private fun getRepositorySearchListSource(query: String): LiveData<Result<List<RepositoryResponseModel>>> {
        return Transformations.map(
            githubRepository.getRepositorySearch(
                getCancelableViewModelScope(),
                query,
                mRepositoryListStateModel.pageIndex,
                Constants.DEFAULT_PAGE_SIZE
            )
        ) {
            when (it.status) {
                Status.LOADING -> Result.loading()
                Status.SUCCESS -> Result.success(it.data!!.items)
                Status.ERROR -> Result.error(it.exception!!)
            }
        }
    }

    override fun showRepositoryDetail(repositoryResponseModel: RepositoryResponseModel) {
        mShowRepositoryDetail.postValue(repositoryResponseModel)
    }

    private fun getCancelableViewModelScope(): CoroutineScope = (viewModelScope + Job()).also {
        mCancelableJob = it
    }
}