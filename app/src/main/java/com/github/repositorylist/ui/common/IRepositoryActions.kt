package com.github.repositorylist.ui.common

import com.github.repositorylist.model.response.RepositoryResponseModel

interface IRepositoryActions {
    fun showRepositoryDetail(repositoryResponseModel: RepositoryResponseModel)
}