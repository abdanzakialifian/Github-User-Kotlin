package com.application.zaki.githubuser.domain.usecase

import androidx.paging.PagingData
import com.application.zaki.githubuser.data.repository.IGithubRepository
import com.application.zaki.githubuser.domain.model.ListUsers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GithubUseCase @Inject constructor(private val githubRepository: IGithubRepository) :
    IGithubUseCase {
    override fun getListUsers(): Flow<PagingData<ListUsers>> = githubRepository.getListUsers()
}