package com.github.repositorylist.model.ui

import com.github.repositorylist.model.response.RepositoryResponseModel

data class PaginatedRepositoryListModel constructor(
    val result: List<RepositoryResponseModel>,
    val hasMorePages: Boolean
)