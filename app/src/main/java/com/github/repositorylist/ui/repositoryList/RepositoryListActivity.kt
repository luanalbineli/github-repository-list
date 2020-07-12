package com.github.repositorylist.ui.repositoryList

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.repositorylist.R
import com.github.repositorylist.extensions.safeNullObserve
import com.github.repositorylist.model.common.Status
import com.github.repositorylist.ui.detail.RepositoryDetailActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_repository_list.*
import javax.inject.Inject

@AndroidEntryPoint
class RepositoryListActivity : AppCompatActivity() {
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
}