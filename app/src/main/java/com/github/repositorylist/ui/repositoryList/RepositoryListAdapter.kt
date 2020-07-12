package com.github.repositorylist.ui.repositoryList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.github.repositorylist.databinding.ItemRepositoryBinding
import com.github.repositorylist.model.response.RepositoryResponseModel
import com.github.repositorylist.ui.common.IRepositoryActions
import com.github.repositorylist.widgets.recyclerView.CustomRecyclerViewAdapter

class RepositoryListAdapter constructor(private val repositoryActions: IRepositoryActions) :
    CustomRecyclerViewAdapter<RepositoryResponseModel, RepositoryListViewHolder>(REPOSITORY_DIFF) {
    override fun onCreateItemViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): RepositoryListViewHolder {
        val binding = ItemRepositoryBinding.inflate(layoutInflater, parent, false)
        return RepositoryListViewHolder(binding)
    }

    override fun onBindItemViewHolder(holder: RepositoryListViewHolder, position: Int) {
        val model = getItem(position)
        holder.bind(model, repositoryActions)
    }

    companion object {
        private val REPOSITORY_DIFF = object : DiffUtil.ItemCallback<RepositoryResponseModel>() {
            override fun areItemsTheSame(
                oldItem: RepositoryResponseModel,
                newItem: RepositoryResponseModel
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: RepositoryResponseModel,
                newItem: RepositoryResponseModel
            ): Boolean = oldItem == newItem
        }
    }
}