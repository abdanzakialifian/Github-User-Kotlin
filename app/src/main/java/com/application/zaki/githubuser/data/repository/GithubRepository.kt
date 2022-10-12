package com.application.zaki.githubuser.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.application.zaki.githubuser.data.source.remote.RemoteDataSource
import com.application.zaki.githubuser.data.source.remote.paging.ListUsersPagingSource
import com.application.zaki.githubuser.data.source.remote.paging.RepositoriesUserPagingSource
import com.application.zaki.githubuser.data.source.remote.paging.UsersPagingSource
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

    override fun getListUsers(): Flow<PagingData<ListUsers>> = flow {
        val pagingSource = Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = true,
                initialLoadSize = 10
            ),
            pagingSourceFactory = { listUsersPagingSource }
        ).flow

        val data = pagingSource.map { paging ->
            paging.map {
                DataMapper.mapListUsersResponseToListUsers(it)
            }
        }

        emitAll(data)
    }.flowOn(Dispatchers.IO)

    override fun getDetailUser(username: String): Flow<DetailUser> =
        remoteDataSource.getDetailUser(username)
            .map {
                DataMapper.mapDetailUserResponseToDetailUser(it)
            }

    override fun getFollowersUser(username: String): Flow<PagingData<ListUsers>> =
        remoteDataSource.getFollowersUser(username).map { pagingData ->
            pagingData.map {
                DataMapper.mapListUsersResponseToListUsers(it)
            }
        }

    override fun getFollowingUser(username: String): Flow<PagingData<ListUsers>> =
        remoteDataSource.getFollowingUser(username).map { pagingData ->
            pagingData.map {
                DataMapper.mapListUsersResponseToListUsers(it)
            }
        }

    override fun getRepositoriesUser(username: String): Flow<PagingData<RepositoriesUser>> =
        remoteDataSource.getRepositoriesUser(username).map { pagingData ->
            pagingData.map {
                DataMapper.mapRepositoriesUserResponseToRepositoriesUser(it)
            }
        }
}