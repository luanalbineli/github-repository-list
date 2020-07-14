package com.github.repositorylist.di

import com.github.repositorylist.repository.github.GithubRepository
import com.github.repositorylist.repository.github.IGithubRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
abstract class BindingModule {
    @Singleton
    @Binds
    abstract fun bindGithubRepository(
        githubRepository: GithubRepository
    ): IGithubRepository
}