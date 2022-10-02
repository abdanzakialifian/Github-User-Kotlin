package com.application.zaki.githubuser.data.source.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.application.zaki.githubuser.data.source.remote.ApiService
import com.application.zaki.githubuser.data.source.remote.response.ListUsersResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ListUsersPagingSource @Inject constructor(private val apiService: ApiService) :
    PagingSource<Int, ListUsersResponse>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListUsersResponse> {
        return try {
            val position = params.key ?: 0
            val response = apiService.getListUsers(position, params.loadSize)
            val listUsers = ArrayList<ListUsersResponse>()
            if (response.isSuccessful) {
                response.body()?.let {
                    listUsers.addAll(it)
                }
            }

            LoadResult.Page(
                data = listUsers,
                prevKey = if (position == 0) null else position - 10,
                nextKey = if (listUsers.isEmpty()) null else position + 10
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ListUsersResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(10)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(10)
        }
    }
}