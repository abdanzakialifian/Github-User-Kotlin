package com.application.zaki.githubuser.data.source.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.application.zaki.githubuser.data.source.remote.paging.FollowersUsersPagingSource
import com.application.zaki.githubuser.data.source.remote.paging.FollowingUsersPagingSource
import com.application.zaki.githubuser.data.source.remote.paging.RepositoriesUserPagingSource
import com.application.zaki.githubuser.data.source.remote.response.DetailUserResponse
import com.application.zaki.githubuser.data.source.remote.response.ListUsersResponse
import com.application.zaki.githubuser.data.source.remote.response.RepositoriesUserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(
    private val apiService: ApiService,
    private val followersUsersPagingSource: FollowersUsersPagingSource,
    private val followingUsersPagingSource: FollowingUsersPagingSource,
    private val repositoriesUserPagingSource: RepositoriesUserPagingSource
) {
    fun getDetailUser(username: String): Flow<DetailUserResponse> = flow {
        val response = apiService.getDetailUser(username)
        emit(response)
    }.flowOn(Dispatchers.IO)

    fun getFollowersUser(username: String): Flow<PagingData<ListUsersResponse>> = Pager(
        config = PagingConfig(
            pageSize = 10,
            enablePlaceholders = true,
            initialLoadSize = 10
        ),
        pagingSourceFactory = {
            followersUsersPagingSource.setUsername(username)
            followersUsersPagingSource
        }
    ).flow.flowOn(Dispatchers.IO)

    fun getFollowingUser(username: String): Flow<PagingData<ListUsersResponse>> = Pager(
        config = PagingConfig(
            pageSize = 10,
            enablePlaceholders = true,
            initialLoadSize = 10
        ),
        pagingSourceFactory = {
            followingUsersPagingSource.setUsername(username)
            followingUsersPagingSource
        }
    ).flow.flowOn(Dispatchers.IO)

    fun getRepositoriesUser(username: String): Flow<PagingData<RepositoriesUserResponse>> =
        Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = true,
                initialLoadSize = 10
            ),
            pagingSourceFactory = {
                repositoriesUserPagingSource.setUsername(username)
                repositoriesUserPagingSource
            }
        ).flow.flowOn(Dispatchers.IO)
}