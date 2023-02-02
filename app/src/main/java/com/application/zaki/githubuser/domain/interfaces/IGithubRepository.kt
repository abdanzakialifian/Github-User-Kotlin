package com.application.zaki.githubuser.domain.interfaces

import androidx.paging.PagingData
import com.application.zaki.githubuser.domain.model.DetailUser
import com.application.zaki.githubuser.domain.model.ListUsers
import com.application.zaki.githubuser.domain.model.RepositoriesUser
import com.application.zaki.githubuser.domain.model.User
import com.application.zaki.githubuser.utils.UiState
import kotlinx.coroutines.flow.Flow

interface IGithubRepository {
    fun getUsers(query: String): Flow<PagingData<ListUsers>>
    fun getListUsers(): Flow<PagingData<ListUsers>>
    fun getDetailUser(username: String): Flow<UiState<DetailUser>>
    fun getFollowersUser(username: String): Flow<PagingData<ListUsers>>
    fun getFollowingUser(username: String): Flow<PagingData<ListUsers>>
    fun getRepositoriesUser(username: String): Flow<PagingData<RepositoriesUser>>
    fun getAllUser(): Flow<List<User>>
    fun addUser(user: User)
    fun deleteUser(user: User)
    fun getUserById(id: Int): Boolean
}