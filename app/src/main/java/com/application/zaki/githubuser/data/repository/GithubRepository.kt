package com.application.zaki.githubuser.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.application.zaki.githubuser.data.source.remote.RemoteDataSource
import com.application.zaki.githubuser.data.source.remote.paging.UsersPagingSource
import com.application.zaki.githubuser.domain.model.ListUsers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GithubRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource, private val usersPagingSource: UsersPagingSource
) : IGithubRepository {
    override fun getListUsers(): Flow<PagingData<ListUsers>> = Pager(config = PagingConfig(
        pageSize = 10, enablePlaceholders = true, initialLoadSize = 10
    ), pagingSourceFactory = { usersPagingSource }).flow.flowOn(Dispatchers.IO)
}