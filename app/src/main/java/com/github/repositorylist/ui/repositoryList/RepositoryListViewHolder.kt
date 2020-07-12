package com.github.repositorylist.ui.repositoryList

import com.github.repositorylist.databinding.ItemRepositoryBinding
import com.github.repositorylist.model.response.RepositoryResponseModel
import com.github.repositorylist.ui.common.IRepositoryActions
import com.github.repositorylist.widgets.recyclerView.CustomVH

class RepositoryListViewHolder(private val binding: ItemRepositoryBinding) :
    CustomVH(binding.root) {
    fun bind(
        repositoryResponseModel: RepositoryResponseModel,
        repositoryActions: IRepositoryActions
    ) {
        binding.repositoryResponseModel = repositoryResponseModel
        binding.repositoryActions = repositoryActions
        binding.executePendingBindings()
    }
}