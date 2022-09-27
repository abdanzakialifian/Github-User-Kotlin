package com.application.zaki.githubuser.data.di

import com.application.zaki.githubuser.data.repository.GithubRepository
import com.application.zaki.githubuser.data.repository.IGithubRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class GithubRepositoryModule {
    @Binds
    @Singleton
    abstract fun provideGithubRepository(githubRepository: GithubRepository): IGithubRepository
}