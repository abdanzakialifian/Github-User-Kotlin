package com.application.zaki.githubuser.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.application.zaki.githubuser.data.source.remote.RemoteDataSource
import com.application.zaki.githubuser.domain.model.DetailUser
import com.application.zaki.githubuser.domain.model.ListUsers
import com.application.zaki.githubuser.domain.model.RepositoriesUser
import com.application.zaki.githubuser.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GithubRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : IGithubRepository {
    override fun getUsers(query: String): Flow<PagingData<ListUsers>> =
        remoteDataSource.getUsers(query).map { pagingData ->
            pagingData.map {
                DataMapper.mapUsersItemResponseToListUsers(it)
            }
        }

    override fun getListUsers(): Flow<PagingData<ListUsers>> =
        remoteDataSource.getListUsers().map { pagingData ->
            pagingData.map {
                DataMapper.mapListUsersResponseToListUsers(it)
            }
        }

    override fun getDetailUser(username: String): Flow<DetailUser> =
        remoteDataSource.getDetailUser(username).map {
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