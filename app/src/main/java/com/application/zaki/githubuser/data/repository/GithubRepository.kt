package com.application.zaki.githubuser.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.application.zaki.githubuser.data.source.remote.RemoteDataSource
import com.application.zaki.githubuser.data.source.remote.paging.*
import com.application.zaki.githubuser.domain.model.DetailUser
import com.application.zaki.githubuser.domain.model.ListUsers
import com.application.zaki.githubuser.domain.model.RepositoriesUser
import com.application.zaki.githubuser.utils.DataMapper
import com.application.zaki.githubuser.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GithubRepository @Inject constructor(
    private val listUsersPagingSource: ListUsersPagingSource,
    private val usersPagingSource: UsersPagingSource,
    private val remoteDataSource: RemoteDataSource,
    private val followersUsersPagingSource: FollowersUsersPagingSource,
    private val followingUsersPagingSource: FollowingUsersPagingSource,
    private val repositoriesUserPagingSource: RepositoriesUserPagingSource
) : IGithubRepository {
    override fun getUsers(query: String): Flow<NetworkResult<PagingData<ListUsers>>> = flow {
        emit(NetworkResult.Loading(null))

        val pagingSource = Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = true,
                initialLoadSize = 10,
                prefetchDistance = 1
            ),
            pagingSourceFactory = {
                usersPagingSource.querySearch(query)
                usersPagingSource
            }
        ).flow

        val data = pagingSource.map { paging ->
            NetworkResult.Success(paging.map {
                DataMapper.mapUsersItemResponseToListUsers(it)
            })
        }
        emitAll(data)
    }.flowOn(Dispatchers.IO)

    override fun getListUsers(): Flow<NetworkResult<PagingData<ListUsers>>> = flow {
        emit(NetworkResult.Loading(null))

        val pagingSource = Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = true,
                initialLoadSize = 10
            ),
            pagingSourceFactory = { listUsersPagingSource }
        ).flow

        val data = pagingSource.map { paging ->
            NetworkResult.Success(paging.map {
                DataMapper.mapListUsersResponseToListUsers(it)
            })
        }
        emitAll(data)
    }.flowOn(Dispatchers.IO)

    override fun getDetailUser(username: String): Flow<NetworkResult<DetailUser>> = flow {
        when (val response = remoteDataSource.getDetailUser(username).last()) {
            is NetworkResult.Success -> {
                emit(NetworkResult.Success(DataMapper.mapDetailUserResponseToDetailUser(response.data)))
            }
            is NetworkResult.Error -> {
                emit(NetworkResult.Error(response.errorMessage))
            }
            is NetworkResult.Empty -> {
                emit(NetworkResult.Empty)
            }
            is NetworkResult.Loading -> {
                emit(NetworkResult.Loading(null))
            }
        }
    }

    override fun getFollowersUser(username: String): Flow<NetworkResult<PagingData<ListUsers>>> =
        flow {
            emit(NetworkResult.Loading(null))

            val pagingSource = Pager(
                config = PagingConfig(
                    pageSize = 10,
                    enablePlaceholders = true,
                    initialLoadSize = 10
                ),
                pagingSourceFactory = {
                    followersUsersPagingSource.setUsername(username)
                    followersUsersPagingSource
                }
            ).flow

            val data = pagingSource.map { paging ->
                NetworkResult.Success(paging.map {
                    DataMapper.mapListUsersResponseToListUsers(it)
                })
            }
            emitAll(data)
        }.flowOn(Dispatchers.IO)

    override fun getFollowingUser(username: String): Flow<NetworkResult<PagingData<ListUsers>>> =
        flow {
            emit(NetworkResult.Loading(null))

            val pagingSource = Pager(
                config = PagingConfig(
                    pageSize = 10,
                    enablePlaceholders = true,
                    initialLoadSize = 10
                ),
                pagingSourceFactory = {
                    followingUsersPagingSource.setUsername(username)
                    followingUsersPagingSource
                }
            ).flow

            val data = pagingSource.map { paging ->
                NetworkResult.Success(paging.map {
                    DataMapper.mapListUsersResponseToListUsers(it)
                })
            }
            emitAll(data)
        }.flowOn(Dispatchers.IO)

    override fun getRepositoriesUser(username: String): Flow<NetworkResult<PagingData<RepositoriesUser>>> =
        flow {
            emit(NetworkResult.Loading(null))

            val pagingSource = Pager(
                config = PagingConfig(
                    pageSize = 10,
                    enablePlaceholders = true,
                    initialLoadSize = 10
                ),
                pagingSourceFactory = {
                    repositoriesUserPagingSource.setUsername(username)
                    repositoriesUserPagingSource
                }
            ).flow

            val data = pagingSource.map { paging ->
                NetworkResult.Success(paging.map {
                    DataMapper.mapRepositoriesUserResponseToRepositoriesUser(it)
                })
            }

            emitAll(data)
        }.flowOn(Dispatchers.IO)
}