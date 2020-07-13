package com.github.repositorylist.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.github.repositorylist.R
import com.github.repositorylist.databinding.ActivityRepositoryDetailBinding
import com.github.repositorylist.model.response.RepositoryResponseModel
import com.github.repositorylist.ui.repositoryList.RepositoryListViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RepositoryDetailActivity : AppCompatActivity() {
    @Inject
    lateinit var mViewModel: RepositoryDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repositoryResponseModel =
            intent.getParcelableExtra<RepositoryResponseModel>(REPOSITORY_RESPONSE_MODEL_BUNDLE_KEY)!!

        mViewModel.init(repositoryResponseModel)
        DataBindingUtil.setContentView<ActivityRepositoryDetailBinding>(
            this,
            R.layout.activity_repository_detail
        ).also { binding ->
            binding.lifecycleOwner = this
            binding.viewModel = mViewModel

            setTitle(R.string.text_repository_detail_title)
        }
    }

    companion object {
        private const val REPOSITORY_RESPONSE_MODEL_BUNDLE_KEY =
            "REPOSITORY_RESPONSE_MODEL_BUNDLE_KEY"

        fun getIntent(context: Context, repositoryResponseModel: RepositoryResponseModel): Intent =
            Intent(context, RepositoryDetailActivity::class.java).also { intent ->
                intent.putExtra(REPOSITORY_RESPONSE_MODEL_BUNDLE_KEY, repositoryResponseModel)
            }
    }

}