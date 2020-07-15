package com.github.repositorylist.ui.repositoryList

import android.os.Bundle
import android.os.Handler
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.repositorylist.R
import com.github.repositorylist.extensions.safeNullObserve
import com.github.repositorylist.model.common.Status
import com.github.repositorylist.model.ui.PaginatedRepositoryListModel
import com.github.repositorylist.ui.detail.RepositoryDetailActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_repository_list.*
import kotlinx.coroutines.CancellationException
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class RepositoryListActivity : AppCompatActivity(), SearchView.OnQueryTextListener {
    @Inject
    lateinit var mViewModel: RepositoryListViewModel

    private val mLinearLayoutManager =
        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

    private val mAdapter by lazy {
        RepositoryListAdapter(
            mViewModel
        ).also {
            it.emptyMessageResId = R.string.text_repository_list_empty
            it.errorMessageResId = R.string.error_repository_list_loading
            it.onTryAgain = {
                mViewModel.tryLoadListAgain()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repository_list)

        list_repository.adapter = mAdapter
        list_repository.layoutManager = mLinearLayoutManager
        list_repository.onLoadMoreItems = {
            mViewModel.loadNextRepositoryListPage()
        }

        list_repository.addItemDecoration(
            DividerItemDecoration(
                this,
                mLinearLayoutManager.orientation
            )
        )

        attachListeners()
    }

    private fun attachListeners() {
        mViewModel.repositoryList.safeNullObserve(this) {
            Timber.d("showRepositoryList - status: ${it.status}")
            list_repository.disableLoadMoreItems()
            when (it.status) {
                Status.LOADING -> mAdapter.showLoadingStatus()
                Status.SUCCESS -> showRepositoryList(it.data!!)
                Status.ERROR -> showError(it.exception)
            }
        }

        mViewModel.showRepositoryDetail.safeNullObserve(this) { repositoryResponseModel ->
            val intent = RepositoryDetailActivity.getIntent(this, repositoryResponseModel)
            startActivity(intent)
        }

        mViewModel.onQueryChanged.safeNullObserve(this) {
            list_repository.disableLoadMoreItems()
            mAdapter.submitList(emptyList())
            mAdapter.showLoadingStatus()
        }
    }

    private fun showError(exception: Exception?) {
        // When a job is cancelled, it raises a CancellationException
        if (exception is CancellationException) {
            return
        }
        mAdapter.showErrorStatus()
    }

    private fun showRepositoryList(paginatedRepositoryListModel: PaginatedRepositoryListModel) {
        Timber.d("showRepositoryList - size: $paginatedRepositoryListModel")
        mAdapter.submitList(paginatedRepositoryListModel.result)
        if (paginatedRepositoryListModel.result.isEmpty()) {
            mAdapter.showEmptyStatus()
        } else if (paginatedRepositoryListModel.hasMorePages) {
            list_repository.enableLoadMoreItems(mLinearLayoutManager)
        }
    }

    private val mSearchDelayHandler: Handler by lazy { Handler() }
    private var mSearchDelayRunnable: Runnable? = null

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_repository_list, menu)

        (menu.findItem(R.id.menu_item_search).actionView as SearchView).apply {
            setOnQueryTextListener(this@RepositoryListActivity)
        }

        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean = false

    override fun onQueryTextChange(query: String): Boolean {
        mSearchDelayRunnable?.let { runnable -> mSearchDelayHandler.removeCallbacks(runnable) }

        val delay = when (query.length) {
            1 -> 1000L
            2, 3 -> 700L
            4, 5 -> 500L
            else -> 300L
        }

        mSearchDelayRunnable = Runnable { mViewModel.onQueryChanged(query) }.also {
            mSearchDelayHandler.postDelayed(it, delay)
        }

        return true
    }
}