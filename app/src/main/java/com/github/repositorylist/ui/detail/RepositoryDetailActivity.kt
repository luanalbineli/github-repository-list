package com.github.repositorylist.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.repositorylist.model.response.RepositoryResponseModel

class RepositoryDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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