package com.application.zaki.githubuser.data.repository

import androidx.paging.PagingData
import com.application.zaki.githubuser.domain.model.DetailUser
import com.application.zaki.githubuser.domain.model.ListUsers
import com.application.zaki.githubuser.domain.model.RepositoriesUser
import kotlinx.coroutines.flow.Flow

interface IGithubRepository {
    fun getUsers(query: String): Flow<PagingData<ListUsers>>
    fun getListUsers(): Flow<PagingData<ListUsers>>
    fun getDetailUser(username: String): Flow<DetailUser>
    fun getFollowersUser(username: String): Flow<PagingData<ListUsers>>
    fun getFollowingUser(username: String): Flow<PagingData<ListUsers>>
    fun getRepositoriesUser(username: String): Flow<PagingData<RepositoriesUser>>
}