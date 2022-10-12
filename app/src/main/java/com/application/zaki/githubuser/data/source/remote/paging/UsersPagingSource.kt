package com.application.zaki.githubuser.data.source.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.application.zaki.githubuser.data.source.remote.ApiService
import com.application.zaki.githubuser.data.source.remote.response.UsersItemResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsersPagingSource @Inject constructor(
    private val apiService: ApiService
) : PagingSource<Int, UsersItemResponse>() {

    private var query: String? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UsersItemResponse> {
        val position = params.key ?: 1
        return try {
            val response = apiService.getUsers(query ?: "", position, params.loadSize)
            val data = response.items
            val listUsers = ArrayList<UsersItemResponse>()
            data?.forEach { list ->
                listUsers.add(list)
            }

            LoadResult.Page(
                data = listUsers,
                prevKey = if (position == 1) null else position,
                nextKey = if (listUsers.isEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, UsersItemResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    fun querySearch(query: String) {
        this.query = query
    }
}