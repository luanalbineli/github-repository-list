package com.github.repositorylist.repository.common

import javax.inject.Inject

open class DefaultIdlingResourceCounter @Inject constructor(): IdlingResourceCounter {
    override fun increment() { }

    override fun decrement() { }
}