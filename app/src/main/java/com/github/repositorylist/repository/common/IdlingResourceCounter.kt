package com.github.repositorylist.repository.common

interface IdlingResourceCounter {
    fun increment()
    fun decrement()
}