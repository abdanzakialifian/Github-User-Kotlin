package com.application.zaki.githubuser.domain.usecase

import androidx.paging.PagingData
import com.application.zaki.githubuser.domain.interfaces.IGithubRepository
import com.application.zaki.githubuser.domain.interfaces.IGithubUseCase
import com.application.zaki.githubuser.domain.model.DetailUser
import com.application.zaki.githubuser.domain.model.ListUsers
import com.application.zaki.githubuser.domain.model.RepositoriesUser
import com.application.zaki.githubuser.utils.UiState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GithubUseCase @Inject constructor(private val githubRepository: IGithubRepository) :
    IGithubUseCase {
    override fun getUsers(query: String): Flow<PagingData<ListUsers>> =
        githubRepository.getUsers(query)

    override fun getListUsers(): Flow<PagingData<ListUsers>> = githubRepository.getListUsers()

    override fun getDetailUser(username: String): Flow<UiState<DetailUser>> =
        githubRepository.getDetailUser(username)

    override fun getFollowersUser(username: String): Flow<PagingData<ListUsers>> =
        githubRepository.getFollowersUser(username)

    override fun getFollowingUser(username: String): Flow<PagingData<ListUsers>> =
        githubRepository.getFollowingUser(username)

    override fun getRepositoriesUser(username: String): Flow<PagingData<RepositoriesUser>> =
        githubRepository.getRepositoriesUser(username)
}