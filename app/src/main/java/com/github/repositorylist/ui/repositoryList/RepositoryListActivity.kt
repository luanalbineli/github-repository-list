package com.github.repositorylist.ui.repositoryList

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.repositorylist.R
import com.github.repositorylist.extensions.safeNullObserve
import com.github.repositorylist.model.common.Status
import com.github.repositorylist.ui.detail.RepositoryDetailActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_repository_list.*
import java.util.*
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
        list_repository.addItemDecoration(
            DividerItemDecoration(
                this,
                mLinearLayoutManager.orientation
            )
        )

        mViewModel.repositoryList.safeNullObserve(this) {
            when (it.status) {
                Status.LOADING -> mAdapter.showLoadingStatus()
                Status.SUCCESS -> mAdapter.submitList(it.data!!)
                Status.ERROR -> mAdapter.showErrorStatus()
            }
        }

        mViewModel.showRepositoryDetail.safeNullObserve(this) { repositoryResponseModel ->
            val intent = RepositoryDetailActivity.getIntent(this, repositoryResponseModel)
            startActivity(intent)
        }
    }

    private var mSearchTimer: Timer? = null

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_repository_list, menu)

        (menu.findItem(R.id.menu_item_search).actionView as SearchView).apply {
            setOnQueryTextListener(this@RepositoryListActivity)
        }

        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean = false

    override fun onQueryTextChange(query: String): Boolean {
        mSearchTimer?.cancel()
        val delay = when (query.length) {
            1 -> 1000L
            2, 3 -> 700L
            4, 5 -> 500L
            else -> 300L
        }

        mSearchTimer = Timer().also {
            it.schedule(object : TimerTask() {
                override fun run() {
                    mViewModel.onQueryChanged(query)
                }
            }, delay)
        }
        return true
    }
}