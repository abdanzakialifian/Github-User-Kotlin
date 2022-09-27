package com.application.zaki.githubuser.domain.di

import com.application.zaki.githubuser.domain.usecase.GithubUseCase
import com.application.zaki.githubuser.domain.usecase.IGithubUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class GithubUseCaseModule {
    @Binds
    @ViewModelScoped
    abstract fun provideGithubUseCase(githubUseCase: GithubUseCase): IGithubUseCase
}