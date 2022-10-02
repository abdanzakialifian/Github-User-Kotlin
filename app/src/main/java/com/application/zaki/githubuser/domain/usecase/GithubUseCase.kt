package com.application.zaki.githubuser.domain.usecase

import androidx.paging.PagingData
import com.application.zaki.githubuser.data.repository.IGithubRepository
import com.application.zaki.githubuser.domain.model.DetailUser
import com.application.zaki.githubuser.domain.model.ListUsers
import com.application.zaki.githubuser.utils.NetworkResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GithubUseCase @Inject constructor(private val githubRepository: IGithubRepository) :
    IGithubUseCase {
    override fun getUsers(query: String): Flow<NetworkResult<PagingData<ListUsers>>> =
        githubRepository.getUsers(query)

    override fun getListUsers(): Flow<NetworkResult<PagingData<ListUsers>>> = githubRepository.getListUsers()

    override fun getDetailUser(username: String): Flow<NetworkResult<DetailUser>> =
        githubRepository.getDetailUser(username)
}