package com.application.zaki.githubuser.data.source.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.application.zaki.githubuser.data.source.remote.ApiService
import com.application.zaki.githubuser.data.source.remote.response.ListUsersResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FollowingUsersPagingSource @Inject constructor(private val apiService: ApiService) :
    PagingSource<Int, ListUsersResponse>() {
    private var username: String? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListUsersResponse> {
        return try {
            val position = params.key ?: 1
            val response = apiService.getFollowingUser(username ?: "", position, params.loadSize)
            val listFollowing = ArrayList<ListUsersResponse>()
            response.let {
                listFollowing.addAll(it)
            }

            LoadResult.Page(
                data = listFollowing,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (listFollowing.isEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ListUsersResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    fun setUsername(username: String) {
        this.username = username
    }
}