package com.application.zaki.githubuser.data.repository

import androidx.paging.PagingData
import com.application.zaki.githubuser.domain.model.DetailUser
import com.application.zaki.githubuser.domain.model.ListUsers
import com.application.zaki.githubuser.utils.NetworkResult
import kotlinx.coroutines.flow.Flow

interface IGithubRepository {
    fun getUsers(query: String): Flow<NetworkResult<PagingData<ListUsers>>>
    fun getListUsers(): Flow<NetworkResult<PagingData<ListUsers>>>
    fun getDetailUser(username: String): Flow<NetworkResult<DetailUser>>
}