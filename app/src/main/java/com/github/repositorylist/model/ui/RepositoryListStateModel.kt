package com.github.repositorylist.model.ui

import com.github.repositorylist.ui.util.Constants

data class RepositoryListStateModel(
    var since: Int? = null,
    var pageIndex: Int = Constants.API_INITIAL_PAGE_INDEX
) {
    var query: String? = ""
        set(value) {
            since = null
            pageIndex = Constants.API_INITIAL_PAGE_INDEX
            field = value
        }
}